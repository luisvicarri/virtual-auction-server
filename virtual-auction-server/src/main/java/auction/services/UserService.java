    package auction.services;

import auction.models.User;
import auction.repositories.UserRepository;
import auction.services.interfaces.UserServiceInterface;
import java.util.UUID;

public class UserService implements UserServiceInterface {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UUID insert(String name, String hashedPassword) {
        if (findByUsername(name)!= null) {
            return null;
        }
        
        User newUser = new User(name, hashedPassword);
        return repository.addUser(newUser);
    }

    public User findByUsername(String name) {
        return repository.getUsers().values()
                .stream()
                .filter(user -> user.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

}
