package auction.security;

import java.security.KeyPair;
import java.security.PublicKey;

public class KeyController {

    private final KeyService service;

    public KeyController(KeyService service) {
        this.service = service;
    }
    
    public void generateKeyPair() {
        service.generateKeyPair();
    }
    
    public KeyPair loadKeys() {
        return service.loadKeys();
    }
    
    public PublicKey getPublicKey(String encodedPublicKey) {
        return service.getPublicKey(encodedPublicKey);
    }
}