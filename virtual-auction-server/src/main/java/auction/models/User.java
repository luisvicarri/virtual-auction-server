package auction.models;

import auction.main.ServerAuctionApp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.security.PublicKey;
import java.util.UUID;

public class User {

    private UUID id;
    private String name;
    private String hashedPassword;
    
    @JsonIgnore
    private String plainPassword;
    private String encodedPublicKey;

    public User() {
    }

    public User(String name, String hashedPassword, String encodedPublicKey) {
        this.name = name;
        this.hashedPassword = hashedPassword;
        this.encodedPublicKey = encodedPublicKey;
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

    public String getHashedPassword() {
        return hashedPassword;
    }
    
    @JsonIgnore
    public PublicKey getPublicKey() {
        return ServerAuctionApp.frame.getAppController().getKeyController().getPublicKey(encodedPublicKey);
    }

    public String getEncodedPublicKey() {
        return encodedPublicKey;
    }
    
}