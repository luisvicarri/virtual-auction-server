package auction.controllers;

import auction.security.KeyController;
import java.security.KeyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerController {

    private static final Logger logger = LoggerFactory.getLogger(ServerController.class);
    private final KeyPair keyPair;

    public ServerController(KeyController keyController) {
        KeyPair existingKeyPair = keyController.loadKeys();

        if (existingKeyPair == null) {
            logger.warn("No existing key found. Generating new key...");
            keyController.generateKeyPair();
            existingKeyPair = keyController.loadKeys();
        } else {
            logger.info("Existing key loaded successfully.");
        }

        this.keyPair = existingKeyPair;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

}
