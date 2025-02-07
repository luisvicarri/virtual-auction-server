package auction.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DigitalSignatureUtil {

    private static final Logger logger = LoggerFactory.getLogger(DigitalSignatureUtil.class);

    public String signData(String data, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes());
            String signedData = Base64.getEncoder().encodeToString(signature.sign());
            return signedData;
        } catch (NoSuchAlgorithmException ex) {
            logger.error("Signing failed: SHA256withRSA algorithm not found.", ex);
        } catch (InvalidKeyException ex) {
            logger.error("Signing failed: Invalid private key.", ex);
        } catch (SignatureException ex) {
            logger.error("Signing failed: Error during signing process.", ex);
        }
        return null;
    }

    public boolean verifySignature(String data, String signatureStr, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(data.getBytes());
            boolean isValid = signature.verify(Base64.getDecoder().decode(signatureStr));
            if (isValid) {
                logger.info("Signature verification successful.");
            } else {
                logger.warn("Signature verification failed: Invalid signature.");
            }
            return isValid;
        } catch (NoSuchAlgorithmException ex) {
            logger.error("Verification failed: SHA256withRSA algorithm not found.", ex);
        } catch (InvalidKeyException ex) {
            logger.error("Verification failed: Invalid public key.", ex);
        } catch (SignatureException ex) {
            logger.error("Verification failed: Error during verification process.", ex);
        }
        return false;
    }
}