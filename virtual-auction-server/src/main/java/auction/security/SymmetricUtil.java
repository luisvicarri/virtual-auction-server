package auction.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SymmetricUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(SymmetricUtil.class);

    public SymmetricUtil() {}

    /**
     * Gera uma chave AES de 256 bits.
     * 
     * @return Chave AES gerada.
     */
    public SecretKey generateAESKey() {
        try {
            logger.info("Generating AES key...");
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256, new SecureRandom());
            SecretKey key = keyGen.generateKey();
            logger.info("AES key successfully generated.");
            return key;
        } catch (NoSuchAlgorithmException e) {
            logger.error("Failed to generate AES key", e);
            throw new RuntimeException("AES key generation failed", e);
        }
    }

    /**
     * Gera um vetor de inicialização (IV) de 16 bytes.
     * 
     * @return Vetor de Inicialização (IV).
     */
    public IvParameterSpec generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /**
     * Criptografa um texto utilizando AES/CBC/PKCS5Padding.
     * 
     * @param plainText Texto a ser criptografado.
     * @param secretKey Chave AES utilizada na criptografia.
     * @param iv Vetor de inicialização (IV).
     * @return Texto criptografado em Base64.
     */
    public String encrypt(String plainText, SecretKey secretKey, IvParameterSpec iv) {
        try {
            logger.info("Encrypting data using AES...");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] cipherText = cipher.doFinal(plainText.getBytes());
            String encryptedData = Base64.getEncoder().encodeToString(cipherText);
            logger.info("Encryption successful.");
            return encryptedData;
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            logger.error("AES encryption failed", e);
            throw new RuntimeException("AES encryption failed", e);
        }
    }

    /**
     * Descriptografa um texto criptografado usando AES/CBC/PKCS5Padding.
     * 
     * @param cipherText Texto criptografado em Base64.
     * @param secretKey Chave AES utilizada na criptografia.
     * @param iv Vetor de inicialização (IV) utilizado na criptografia.
     * @return Texto descriptografado.
     */
    public String decrypt(String cipherText, SecretKey secretKey, IvParameterSpec iv) {
        try {
            logger.info("Decrypting data using AES...");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            logger.info("Decryption successful.");
            return new String(plainText);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            logger.error("AES decryption failed", e);
            throw new RuntimeException("AES decryption failed", e);
        }
    }

    /**
     * Codifica uma chave AES para Base64.
     * 
     * @param key Chave AES.
     * @return Representação em Base64 da chave AES.
     */
    public String encodeKey(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * Decodifica uma chave AES a partir de sua representação em Base64.
     * 
     * @param encodedKey String Base64 da chave AES.
     * @return Objeto SecretKey representando a chave AES.
     */
    public SecretKey decodeKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, "AES");
    }

    /**
     * Codifica um IV para Base64.
     * 
     * @param iv Vetor de Inicialização.
     * @return Representação em Base64 do IV.
     */
    public String encodeIV(IvParameterSpec iv) {
        return Base64.getEncoder().encodeToString(iv.getIV());
    }

    /**
     * Decodifica um IV a partir de sua representação em Base64.
     * 
     * @param encodedIV String Base64 do IV.
     * @return Objeto IvParameterSpec representando o IV.
     */
    public IvParameterSpec decodeIV(String encodedIV) {
        byte[] decodedIV = Base64.getDecoder().decode(encodedIV);
        return new IvParameterSpec(decodedIV);
    }
}