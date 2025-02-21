package auction.models;

import auction.enums.ItemStatus;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class Item {

    private UUID id;
    private ItemData data;
    private double currentBid;
    private double openingBid;
    private ItemStatus status;
    private Optional<Instant> auctionStart;
    private Optional<Instant> auctionEnd;
    private Optional<UUID> winningBidder;

    public Item() {
    }

    public Item(ItemData data, double openingBid, ItemStatus status, Optional<Instant> auctionStart, Optional<Instant> auctionEnd, Optional<UUID> winningBidder) {
        this.id = UUID.randomUUID();
        this.data = data;
        this.openingBid = openingBid;
        this.currentBid = openingBid;
        this.status = status != null ? status : ItemStatus.CREATED;
        this.auctionStart = auctionStart != null ? auctionStart : Optional.empty();
        this.auctionEnd = auctionEnd != null ? auctionEnd : Optional.empty();
        this.winningBidder = winningBidder != null ? winningBidder : Optional.empty();
    }

    public UUID getId() {
        return id;
    }

    public ItemData getData() {
        return data;
    }

    public void setData(ItemData data) {
        this.data = data;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public double getOpeningBid() {
        return openingBid;
    }

    public void setOpeningBid(double openingBid) {
        this.openingBid = openingBid;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public Optional<Instant> getAuctionStart() {
        return auctionStart;
    }

    public void setAuctionStart(Optional<Instant> auctionStart) {
        this.auctionStart = auctionStart;
    }

    public Optional<Instant> getAuctionEnd() {
        return auctionEnd;
    }

    public void setAuctionEnd(Optional<Instant> auctionEnd) {
        this.auctionEnd = auctionEnd;
    }

    public Optional<UUID> getWinningBidder() {
        return winningBidder;
    }

    public void setWinningBidder(Optional<UUID> winningBidder) {
        this.winningBidder = winningBidder;
    }
}