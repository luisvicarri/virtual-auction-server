package auction.handlers;

import auction.services.AuctionService;
import auction.services.interfaces.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BidUpdated implements MessageHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(PlaceBid.class);
    private final AuctionService service;

    public BidUpdated(AuctionService service) {
        this.service = service;
    }

    @Override
    public void handle(String message) {
        logger.info("Processing bid updated message: {}", message);
        service.displayNewBid(message);
    }
    
}
