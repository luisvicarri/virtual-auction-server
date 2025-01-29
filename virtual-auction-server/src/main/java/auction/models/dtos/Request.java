package auction.models.dtos;

import java.util.Optional;
import java.util.UUID;

public class Request {
    
    private String action;
    private String name;
    private String password;
    private Optional<UUID> id;

    public Request() {
    }

    public Request(String action, String name, String password, Optional<UUID> id) {
        this.action = action;
        this.name = name;
        this.password = password;
        this.id = id != null ? id : Optional.empty();
    }
    
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Optional<UUID> getId() {
        return id;
    }

    public void setId(Optional<UUID> id) {
        this.id = id;
    }
    
}