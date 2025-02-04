package auction.controllers;

import auction.security.KeyController;
import java.security.KeyPair;

public class ServerController {
    
    private final KeyPair keyPair;

    public ServerController(KeyController keyController) {
        keyController.generateKeyPair();
        this.keyPair = keyController.loadKeys();
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }
    
}