package auction.security;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsymmetricUtil {

    private static final Logger logger = LoggerFactory.getLogger(AsymmetricUtil.class);

    public AsymmetricUtil() {}

    public KeyPair generateRSAKeyPair() {
        try {
            logger.info("Generating RSA key pair...");
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(4096);
            KeyPair keyPair = keyGen.generateKeyPair();
            logger.info("RSA key pair successfully generated.");
            return keyPair;
        } catch (NoSuchAlgorithmException ex) {
            logger.error("Failed to generate RSA key pair: Algorithm not found", ex);
            throw new RuntimeException("RSA key generation failed.", ex);
        }
    }

    public String encrypt(String plainText, PublicKey publicKey) {
        try {
            logger.info("Plain Text: {}", plainText);
            logger.info("Encrypting data using RSA...");
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            String encryptedData = Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
            logger.info("Encryption successful.");
            return encryptedData;
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            logger.error("RSA encryption failed", e);
            throw new RuntimeException("RSA encryption failed", e);
        }
    }

    public String decrypt(String cipherText, PrivateKey privateKey) {
        try {
            logger.info("Decrypting data using RSA...");
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            String decryptedData = new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
            logger.info("Decryption successful.");
            logger.info("Decrypted message: {}", decryptedData);
            return decryptedData;
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            logger.error("RSA decryption failed", e);
            throw new RuntimeException("RSA decryption failed", e);
        }
    }

    public PublicKey decodePublicKey(String encodedKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(encodedKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("Failed to decode public key", e);
            throw new RuntimeException("Failed to decode public key", e);
        }
    }
}