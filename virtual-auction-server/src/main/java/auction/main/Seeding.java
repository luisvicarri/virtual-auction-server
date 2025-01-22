package auction.main;

import auction.repositories.UserRepository;
import auction.services.UserService;
import auction.stubs.UserServiceStub;
import java.io.IOException;

public class Seeding {

    public void start() {
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        UserServiceStub serverStub = new UserServiceStub(userService);

        int port = 8080;
        try {
            System.out.println("Starting server...");
            serverStub.start(port);
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
        
    }
    
}
