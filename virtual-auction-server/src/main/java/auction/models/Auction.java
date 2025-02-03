package auction.models;

import auction.enums.AuctionStatus;
import java.util.Optional;

public class Auction {
    
    private AuctionStatus status;
    private Optional<Item> currentAuctionItem;

    public Auction() {
        this.status = AuctionStatus.WAITING;
        this.currentAuctionItem = Optional.empty();
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    public Optional<Item> getCurrentAuctionItem() {
        return currentAuctionItem;
    }

    public void setCurrentAuctionItem(Item currentAuctionItem) {
        this.currentAuctionItem = Optional.ofNullable(currentAuctionItem);
    }
}