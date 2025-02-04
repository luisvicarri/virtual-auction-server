package auction.models.dtos;

import java.util.Optional;
import java.util.UUID;

public class Request {
    
    private String action;
    private String name;
    private String password;
    private Optional<UUID> id;
    private Optional<String> encodedPublicKey;

    public Request() {
    }

    public Request(String action, String name, String password, Optional<UUID> id, Optional<String> encodedPublicKey) {
        this.action = action;
        this.name = name;
        this.password = password;
        this.id = id != null ? id : Optional.empty();
        this.encodedPublicKey = encodedPublicKey != null ? encodedPublicKey : Optional.empty();
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

    public Optional<String> getEncodedPublicKey() {
        return encodedPublicKey;
    }

    public void setEncodedPublicKey(Optional<String> encodedPublicKey) {
        this.encodedPublicKey = encodedPublicKey;
    }

}