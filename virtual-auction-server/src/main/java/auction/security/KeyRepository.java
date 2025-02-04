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

    public void saveKeys(KeyPair keyPair) {
        try {
            Map<String, String> keysMap = Map.of(
                    "privateKey", Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()),
                    "publicKey", Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded())
            );

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, keysMap);
            logger.info("Server keys saved successfully.");
        } catch (IOException ex) {
            logger.error("Error saving server keys: ", ex.getMessage());
        }
    }

    public KeyPair loadKeys() {
        if (!file.exists()) {
            return null;
        }

        try {
            Map<String, String> keysMap = mapper.readValue(file, Map.class);

            byte[] privateKeyBytes = Base64.getDecoder().decode(keysMap.get("privateKey"));
            byte[] publicKeyBytes = Base64.getDecoder().decode(keysMap.get("publicKey"));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

            return new KeyPair(publicKey, privateKey);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            logger.error("Error loading server keys: " + ex.getMessage());
            return null;
        }
    }   
}