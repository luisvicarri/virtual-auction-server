package auction.controllers;

import auction.repositories.UserRepository;
import auction.services.MulticastService;
import auction.services.UserService;
import auction.stubs.UserServiceStub;

public final class AppController {
    
    private final UserController userController;
    private final UserServiceStub userStub;
    private final MulticastController multicastController;

    public AppController() {
        this.userController = configUserController();
        this.userStub = configUserStub();
        this.multicastController = configMulticastController();
    }
    
    private MulticastController configMulticastController() {
        MulticastService service = new MulticastService();
        return new MulticastController(service);
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

    public MulticastController getMulticastController() {
        return multicastController;
    }
    
}