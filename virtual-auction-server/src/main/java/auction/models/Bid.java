package auction.models;

import auction.main.ServerAuctionApp;
import java.time.Instant;
import java.util.UUID;

public class Bid {

    private UUID id;
    private UUID itemId;
    private UUID bidderId;
    private String bidderName;
    private double amount;
    private Instant timestamp;

    public Bid() {
    }

    public Bid(UUID itemId, UUID bidderId, double amount) {
        this.id = UUID.randomUUID();
        this.itemId = itemId;
        this.bidderId = bidderId;
        this.amount = amount;
        this.timestamp = Instant.now();
        User user = ServerAuctionApp.frame.getAppController().getUserController().findById(bidderId).orElse(null);
        this.bidderName = user.getName();
    }

    public UUID getId() {
        return id;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public UUID getBidderId() {
        return bidderId;
    }

    public void setBidderId(UUID bidderId) {
        this.bidderId = bidderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getBidderName() {
        return bidderName;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }
    
}