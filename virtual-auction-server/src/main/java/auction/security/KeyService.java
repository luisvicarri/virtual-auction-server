package auction.security;

import java.security.KeyPair;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyService {
    
    private static final Logger logger = LoggerFactory.getLogger(KeyService.class);
    private final KeyRepository repository;
    private final AsymmetricUtil asymmetricUtil;
    private final SymmetricUtil symmetricUtil;

    public KeyService(KeyRepository repository) {
        this.repository = repository;
        this.asymmetricUtil = new AsymmetricUtil();
        this.symmetricUtil = new SymmetricUtil();
    }
    
    public void generateAsymmetricKeys() {
        try {
            KeyPair keyPair = asymmetricUtil.generateRSAKeyPair();
            repository.saveAsymmetricKeys(keyPair);
            logger.info("RSA key pair successfully generated and saved.");
        } catch (Exception ex) {
            logger.error("Failed to generate RSA key pair", ex);
        }
    }
    
    public KeyPair loadAsymmetricKeys() {
        return repository.loadAsymmetricKeys();
    }
    
    public PublicKey getPublicKey(String encodedPublicKey) {
        try {
            return asymmetricUtil.decodePublicKey(encodedPublicKey);
        } catch (Exception ex) {
            logger.error("Failed to decode public key", ex);
            return null;
        }
    }
    
    public void generateSymmetricKey() {
        try {
            SecretKey secretKey = symmetricUtil.generateAESKey();
            repository.saveSymmetricKey(secretKey);
            logger.info("AES symmetric key successfully generated and saved.");
        } catch (Exception ex) {
            logger.error("Failed to generate AES symmetric key", ex);
        }
    }

    public SecretKey loadSymmetricKey() {
        return repository.loadSymmetricKey();
    }
    
    public IvParameterSpec loadIV() {
        return repository.loadIV();
    }
    
    public void generateIV() {
        try {
            IvParameterSpec newIV = symmetricUtil.generateIV();
            repository.saveIV(newIV.getIV());
            logger.info("IV successfully generated and saved.");
        } catch (Exception ex) {
            logger.error("Failed to generate IV", ex);
        }
    }
}