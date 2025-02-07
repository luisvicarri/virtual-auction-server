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
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyRepository {

    private static final Logger logger = LoggerFactory.getLogger(KeyRepository.class);
    private final File file = new File("repositories/security/server_keys.json");
    private final ObjectMapper mapper = JsonUtil.getObjectMapper();

    public KeyRepository() {
    }
  
    /**
     * Saves the server keys to a JSON file if necessary.
     *
     * @param keyPair The server key pair.
     */
    public void saveKeys(KeyPair keyPair) {
        try {
            // Converting the keys to Base64
            String encodedPrivateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
            String encodedPublicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            // If the file already exists, check if the keys are already saved
            if (file.exists()) {
                Map<String, String> existingKeys = mapper.readValue(file, Map.class);
                if (encodedPrivateKey.equals(existingKeys.get("privateKey"))
                        && encodedPublicKey.equals(existingKeys.get("publicKey"))) {
                    logger.info("Server keys are already saved. No action required.");
                    return; // Avoid unnecessary rewriting
                }
            }

            // Creating and saving JSON with keys
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

    /**
     * Loads server keys from the JSON file, if available.
     *
     * @return The server key pair or {@code null} if there is an error.
     */
    public KeyPair loadKeys() {
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

            // Decoding the keys
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
}
