package auction.controllers;

import auction.security.KeyController;
import java.security.KeyPair;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerController {

    private static final Logger logger = LoggerFactory.getLogger(ServerController.class);
    private final SecretKey symmetricKey;
    private final IvParameterSpec iv;
    private final KeyPair keyPair;

    public ServerController(KeyController keyController) {
        // Carrega ou gera chave assimétrica (RSA)
        KeyPair existingAsymmetricKeys = keyController.loadAsymmetricKeys();
        if (existingAsymmetricKeys == null) {
            logger.warn("No existing keys found. Generating new keys...");
            keyController.generateAsymmetricKeys();
            existingAsymmetricKeys = keyController.loadAsymmetricKeys();
        } else {
            logger.info("Existing Asymmetric Keys loaded successfully.");
        }
        this.keyPair = existingAsymmetricKeys;

        // Carrega ou gera chave simétrica (AES)
        SecretKey existingSymmetricKey = keyController.loadSymmetricKey();
        if (existingSymmetricKey == null) {
            logger.warn("No existing key found. Generating new key...");
            keyController.generateSymmetricKey();
            existingSymmetricKey = keyController.loadSymmetricKey();
        } else {
            logger.info("Existing Symmetric Key loaded successfully.");
        }
        this.symmetricKey = existingSymmetricKey;
        
        // Carrega ou gera IV
        IvParameterSpec existingIV = keyController.loadIV();
        if (existingIV == null) {
            logger.warn("No existing IV found. Generating new IV...");
            keyController.generateIV();
            existingIV = keyController.loadIV();
        } else {
            logger.info("Existing IV loaded successfully.");
        }
        this.iv = existingIV;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public SecretKey getSymmetricKey() {
        return symmetricKey;
    }

    public IvParameterSpec getIv() {
        return iv;
    }
}