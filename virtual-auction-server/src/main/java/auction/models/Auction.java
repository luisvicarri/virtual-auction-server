package auction.models;

import auction.enums.AuctionStatus;

public class Auction {
    
    private AuctionStatus status;
    private Item currentAuctionItem;

    public Auction() {
        this.status = AuctionStatus.WAITING;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    public Item getCurrentAuctionItem() {
        return currentAuctionItem;
    }

    public void setCurrentAuctionItem(Item currentAuctionItem) {
        this.currentAuctionItem = currentAuctionItem;
    }
    
}