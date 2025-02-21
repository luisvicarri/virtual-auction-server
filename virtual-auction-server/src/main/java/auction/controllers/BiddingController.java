package auction.controllers;

import auction.models.Bid;
import auction.services.BiddingService;
import java.util.List;
import java.util.UUID;

public class BiddingController {
    
    private final BiddingService service;

    public BiddingController(BiddingService service) {
        this.service = service;
    }
    
    public List<Bid> getBidsForItem(UUID itemId) {
        return service.getBidsForItem(itemId);
    }

    public boolean addBid(UUID itemId, Bid bid) {
        return service.addBid(itemId, bid);
    }
}