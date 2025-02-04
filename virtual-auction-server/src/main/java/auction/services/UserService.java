package auction.services;

import auction.models.User;
import auction.repositories.UserRepository;
import auction.services.interfaces.UserServiceInterface;
import auction.utils.PasswordUtil;
import java.util.Optional;
import java.util.UUID;

public class UserService implements UserServiceInterface {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<User> findByUsername(String name) {
        return repository.findByUsername(name);
    }

    @Override
    public boolean signIn(UUID id, String name, String password) {
        Optional<User> userOptional = repository.findById(id);
        if (userOptional.isPresent()) {
            User userFinded = userOptional.get();
            if (PasswordUtil.checkPassword(password, userFinded.getHashedPassword())) {
                return true;
            }

        }
        return false;
    }

    @Override
    public UUID insert(String name, String hashedPassword, String encodedPublicKey) {
        if (repository.findByUsername(name).isPresent()) {
            return null;
        }

        User newUser = new User(name, hashedPassword, encodedPublicKey);
        return repository.addUser(newUser);
    }
}
