package auction.controllers;

import auction.models.User;
import auction.services.UserService;

public class UserController {
    
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }
    
    public void signUp(User newUser) {
        
    }
    
}
