package auction.handlers;

import auction.services.AuctionService;
import auction.services.interfaces.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeUpdate implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(TimeUpdate.class);
    private final AuctionService service;

    public TimeUpdate(AuctionService service) {
        this.service = service;
    }

    @Override
    public void handle(String message) {
        logger.info("Processing time update message: {}", message);
        service.updateTime(message);
    }

}
