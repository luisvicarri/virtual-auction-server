package auction.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;

public class User {
    
    private UUID id;
    private String name;
    private String hashedPassword;
    
//    @JsonIgnore
    private String plainPassword;

    public User() {
    }

    public User(String name, String hashedPassword) {
        this.name = name;
        this.hashedPassword = hashedPassword;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

}