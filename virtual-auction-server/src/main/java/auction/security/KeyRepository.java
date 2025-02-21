package auction.security;

import auction.utils.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyRepository {

    private static final Logger logger = LoggerFactory.getLogger(KeyRepository.class);
    private final File file = new File("repositories/security/server_keys.json");
    private final ObjectMapper mapper = JsonUtil.getObjectMapper();

    public KeyRepository() {
    }

    public void saveAsymmetricKeys(KeyPair keyPair) {
        try {
            String encodedPrivateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            String encodedPublicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            if (file.exists()) {
                Map<String, String> existingKeys = mapper.readValue(file, Map.class);
                if (encodedPrivateKey.equals(existingKeys.get("privateKey"))
                        && encodedPublicKey.equals(existingKeys.get("publicKey"))) {
                    logger.info("Server keys are already saved. No action required.");
                    return;
                }
            }

            Map<String, String> keysMap = Map.of(
                    "privateKey", encodedPrivateKey,
                    "publicKey", encodedPublicKey
            );

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, keysMap);
            logger.info("Server keys saved successfully.");

        } catch (IOException ex) {
            logger.error("Error saving server keys: {}", ex.getMessage(), ex);
        }
    }

    public KeyPair loadAsymmetricKeys() {
        if (!file.exists()) {
            logger.warn("Server key file does not exist.");
            return null;
        }

        try {
            Map<String, String> keysMap = mapper.readValue(file, Map.class);

            String encodedPrivateKey = keysMap.get("privateKey");
            String encodedPublicKey = keysMap.get("publicKey");

            if (encodedPrivateKey == null || encodedPublicKey == null) {
                logger.warn("Server key file is incomplete.");
                return null;
            }

            byte[] privateKeyBytes = Base64.getDecoder().decode(encodedPrivateKey);
            byte[] publicKeyBytes = Base64.getDecoder().decode(encodedPublicKey);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

            logger.info("Server keys loaded successfully.");
            return new KeyPair(publicKey, privateKey);

        } catch (IOException ex) {
            logger.error("Error loading keys from server: {}", ex.getMessage(), ex);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("Error processing keys from server: {}", ex.getMessage(), ex);
        }

        return null;
    }

    public void saveSymmetricKey(SecretKey secretKey) {
        try {
            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

            Map<String, String> keysMap;
            if (file.exists()) {
                keysMap = mapper.readValue(file, Map.class);
            } else {
                keysMap = new HashMap<>();
            }

            keysMap.put("symmetricKey", encodedKey);

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, keysMap);
            logger.info("AES symmetric key saved successfully.");
        } catch (IOException ex) {
            logger.error("Error saving AES symmetric key: {}", ex.getMessage(), ex);
        }
    }

    public SecretKey loadSymmetricKey() {
        if (!file.exists()) {
            logger.warn("Server key file does not exist.");
            return null;
        }

        try {
            Map<String, String> keysMap = mapper.readValue(file, Map.class);
            String encodedKey = keysMap.get("symmetricKey");

            if (encodedKey == null) {
                logger.warn("No symmetric key found in file.");
                return null;
            }

            return new SymmetricUtil().decodeKey(encodedKey);
        } catch (IOException ex) {
            logger.error("Error loading AES symmetric key: {}", ex.getMessage(), ex);
        }

        return null;
    }

    public void saveIV(byte[] iv) {
        try {
            String encodedIV = Base64.getEncoder().encodeToString(iv);

            Map<String, String> keysMap;
            if (file.exists()) {
                keysMap = mapper.readValue(file, Map.class);
            } else {
                keysMap = new HashMap<>();
            }

            keysMap.put("iv", encodedIV);

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, keysMap);
            logger.info("AES IV saved successfully.");
        } catch (IOException ex) {
            logger.error("Error saving IV: {}", ex.getMessage(), ex);
        }
    }

    public IvParameterSpec loadIV() {
        if (!file.exists()) {
            logger.warn("Server IV's file does not exist.");
            return null;
        }
        try {
            Map<String, String> keyMap = mapper.readValue(file, Map.class);
            String encodedIVBase64 = keyMap.get("iv");
            if (encodedIVBase64 == null || encodedIVBase64.isEmpty()) return null;
            byte[] decodedIV = Base64.getDecoder().decode(encodedIVBase64);
            return new IvParameterSpec(decodedIV);
        } catch (IOException ex) {
            logger.error("Error loading IV: {}", ex.getMessage(), ex);
        }
        return null;
    }
}