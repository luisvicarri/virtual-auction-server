package auction.security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyService {
    
    private static final Logger logger = LoggerFactory.getLogger(KeyService.class);
    private final KeyRepository repository;

    public KeyService(KeyRepository repository) {
        this.repository = repository;
    }
    
    public void generateKeyPair() {
        try {
            logger.info("Generating RSA key pair...");
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(4096);
            KeyPair keyPair = keyGen.generateKeyPair();
            
            repository.saveKeys(keyPair);
            logger.info("RSA key pair successfully generated and saved.");
        } catch (NoSuchAlgorithmException ex) {
            logger.error("Failed to generate RSA key pair: Algorithm not found", ex);
        }
    }
    
    public KeyPair loadKeys() {
        return repository.loadKeys();
    }
    
    public PublicKey getPublicKey(String encodedPublicKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(encodedPublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (IllegalArgumentException ex) {
            logger.error("Failed to decode public key: Invalid Base64 encoding", ex);
        } catch (NoSuchAlgorithmException ex) {
            logger.error("Failed to get public key: RSA algorithm not found", ex);
        } catch (InvalidKeySpecException ex) {
            logger.error("Failed to generate public key: Invalid key specification", ex);
        }
        return null;
    }
}