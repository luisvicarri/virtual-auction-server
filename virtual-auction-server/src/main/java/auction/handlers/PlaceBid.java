package auction.handlers;

import auction.services.AuctionService;
import auction.services.interfaces.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlaceBid implements MessageHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(PlaceBid.class);
    private final AuctionService service;

    public PlaceBid(AuctionService service) {
        this.service = service;
    }

    @Override
    public void handle(String message) {
        logger.info("Processing place bid message: {}", message);
        service.processBid(message);
    }
    
}