package auction.controllers;

import auction.models.User;
import auction.services.UserService;
import java.util.Optional;
import java.util.UUID;

public class UserController {
    
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }
    
    public void signUp(User newUser) {
        
    }
    
    public Optional<User> findById(UUID id) {
        return service.findById(id);
    }
}
