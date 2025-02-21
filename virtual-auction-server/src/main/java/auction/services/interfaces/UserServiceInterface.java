package auction.services.interfaces;

import java.util.UUID;

public interface UserServiceInterface { 
    boolean signIn(UUID id, String name, String password);
    UUID insert(String name, String password, String encodedPublicKey);
}