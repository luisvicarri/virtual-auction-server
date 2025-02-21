package auction.handlers;

import auction.services.AuctionService;
import auction.services.interfaces.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientConnected implements MessageHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(ClientConnected.class);
    private final AuctionService service;

    public ClientConnected(AuctionService service) {
        this.service = service;
    }

    @Override
    public void handle(String message) {
        logger.info("Processing client connected message: {}", message);
        service.clientConnected(message);
    }
}