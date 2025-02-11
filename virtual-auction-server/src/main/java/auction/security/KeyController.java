package auction.security;

import java.security.KeyPair;
import java.security.PublicKey;
import javax.crypto.SecretKey;

public class KeyController {

    private final KeyService service;

    public KeyController(KeyService service) {
        this.service = service;
    }
    
    public void generateAsymmetricKeys() {
        if (service.loadAsymmetricKeys() == null) {
            service.generateAsymmetricKeys();
        }
    }
    
    public KeyPair loadAsymmetricKeys() {
        return service.loadAsymmetricKeys();
    }
    
    public PublicKey getPublicKey(String encodedPublicKey) {
        return service.getPublicKey(encodedPublicKey);
    }
    
    public void generateSymmetricKey() {
        if (service.loadSymmetricKey() == null) {
            service.generateSymmetricKey();
        }
    }
    
    public SecretKey loadSymmetricKey() {
        return service.loadSymmetricKey();
    }
    
    public void generateIV() {
        if (service.loadIV() == null) {
            service.generateIV();
        }
    }
    
    public byte[] loadIV() {
        return service.loadIV();
    }
}