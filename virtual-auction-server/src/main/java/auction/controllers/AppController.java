package auction.controllers;

import auction.repositories.UserRepository;
import auction.services.UserService;
import auction.stubs.UserServiceStub;

public final class AppController {
    
    private final UserController userController;
    private final UserServiceStub userStub;

    public AppController() {
        this.userController = configUserController();
        this.userStub = configUserStub();
    }
    
    private UserController configUserController() {
        UserRepository repository = new UserRepository();
        UserService service = new UserService(repository);
        return new UserController(service);
    }
    
    private UserServiceStub configUserStub() {
        UserRepository repository = new UserRepository();
        UserService service = new UserService(repository);
        return new UserServiceStub(service);
    }
    
    public UserController getUserController() {
        return userController;
    }

    public UserServiceStub getUserStub() {
        return userStub;
    }
    
}