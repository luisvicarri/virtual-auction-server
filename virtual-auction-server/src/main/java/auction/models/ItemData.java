package auction.models;

import java.time.Duration;

public class ItemData {

    private String title;
    private String description;
    private double bidIncrement;
    private double reservePrice;
    private Duration auctionDuration;
    private String itemImage;

    public ItemData() {
    }

    public ItemData(String title, String description, double bidIncrement, double reservePrice, Duration auctionDuration, String itemImage) {
        this.title = title;
        this.description = description;
        this.bidIncrement = bidIncrement;
        this.reservePrice = reservePrice;
        this.auctionDuration = auctionDuration;
        this.itemImage = itemImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBidIncrement() {
        return bidIncrement;
    }

    public void setBidIncrement(double bidIncrement) {
        this.bidIncrement = bidIncrement;
    }

    public double getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(double reservePrice) {
        this.reservePrice = reservePrice;
    }

    public Duration getAuctionDuration() {
        return auctionDuration;
    }

    public void setAuctionDuration(Duration auctionDuration) {
        this.auctionDuration = auctionDuration;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
}