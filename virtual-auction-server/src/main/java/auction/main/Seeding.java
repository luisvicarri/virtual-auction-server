package auction.main;

import auction.models.Item;
import auction.models.ItemData;
import auction.repositories.ItemRepository;
import auction.repositories.UserRepository;
import auction.services.ItemService;
import auction.services.UserService;
import auction.stubs.UserServiceStub;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

public class Seeding {

    public void start() {
        
//        ItemRepository itemRepository = new ItemRepository();
//        ItemService itemService = new ItemService(itemRepository);
//        
//        
//        double reservePrice = 600;
//        ItemData data = new ItemData(
//                "Livro Harry Potter - Edição Limitada",
//                "Versão limitada do livro Harry Potter e a Pedra Filosofal",
//                (reservePrice * 0.1),
//                reservePrice,
//                Duration.ofMinutes(5)
//                
//        );
//        
//        Item item = new Item(
//                data,
//                0.0,
//                null,
//                Optional.empty(),
//                Optional.empty(),
//                Optional.empty()               
//        );
//        
//        System.out.println("Registering item...");
//        boolean isRegistered = itemService.insert(item);
//
//        if (isRegistered) {
//            System.out.println("Item registered successfully!");
//        } else {
//            System.out.println("Failed to register item.");
//        }
        
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
