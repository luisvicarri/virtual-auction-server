package auction.services.interfaces;

import java.util.UUID;

public interface UserServiceInterface {
    
    UUID insert(String name, String password);
    
}
