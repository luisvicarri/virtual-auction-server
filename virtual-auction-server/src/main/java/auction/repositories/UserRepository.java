package auction.repositories;

import auction.models.User;
import auction.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private final File file = new File("repositories/users/users.json");
    private Map<UUID, User> users = new HashMap<>();
    private final ObjectMapper mapper = JsonUtil.getObjectMapper();

    public UserRepository() {
        loadUsers();
    }

    public Map<UUID, User> getUsers() {
        return users;
    }

    public void setUsers(Map<UUID, User> users) {
        this.users = users;
    }

    public Optional<User> findById(UUID id) {
        return getUsers().values()
                .stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }
    
    public Optional<User> findByUsername(String name) {
        return getUsers().values()
                .stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }
            
    /**
     * (pt-BR) Adiciona um usuário ao repositório e salva no arquivo
     * (en-US) Add a user to the repository and save to file
     */
    public UUID addUser(User newUser) {
        if (users.containsKey(newUser.getId())) {
            logger.warn("Attempt to add user with existing UUID: {}", newUser.getId());
            return null;
        }
        
        UUID id = UUID.randomUUID();
        newUser.setId(id);
        users.put(id, newUser);
        logger.info("User successfully added: {}", newUser.getName());
        
        try {
            saveUsers();
            logger.info("Users successfully saved after adding: {}", newUser.getId());
            return id;
        } catch (Exception ex) {
            users.remove(newUser.getId());
            logger.error("Error saving users. Removing newly added user: {}", newUser.getId(), ex);
            return null;
        }
    }
    
    /**
     * (pt-BR) Salva os usuários no arquivo JSON
     * (en-US) Save users to JSON file
     */
    private void saveUsers() {
        try {
            if (users.containsKey(null)) {
                users.remove(null);
                logger.warn("Removed null key from users map before saving.");
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, users);
        } catch (IOException ex) {
            logger.error("Error saving users to JSON file", ex);
        }
    }
    
    /**
     * (pt-BR) Carrega os usuários do arquivo JSON para a memória.
     * (en-US) Loads users from the JSON file into memory.
     */
    private void loadUsers() {
        if (file.exists()) {
            try {
                users = mapper.readValue(file, new TypeReference<Map<UUID, User>>() {});
            } catch (IOException ex) {
                logger.error("Error loading users", ex);
            }
        }
    }
    
}