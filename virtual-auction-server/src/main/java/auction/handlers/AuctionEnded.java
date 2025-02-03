package auction.handlers;

import auction.services.AuctionService;
import auction.services.interfaces.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuctionEnded implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(PlaceBid.class);
    private final AuctionService service;

    public AuctionEnded(AuctionService service) {
        this.service = service;
    }

    @Override
    public void handle(String message) {
        logger.info("Processing auction ended message: {}", message);
        service.auctionEnded(message);
    }
}