package auction.utils;

public class ValidatorUtil {
    
    public boolean samePassword(String passwordStored, String passwordToCompare) {
        return passwordToCompare.equals(passwordStored);
    }
    
}