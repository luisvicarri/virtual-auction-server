package auction.services;

import auction.models.Bid;
import auction.repositories.BiddingRepository;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BiddingService {
    
    private static final Logger logger = LoggerFactory.getLogger(BiddingService.class);
    private final BiddingRepository repository;

    public BiddingService(BiddingRepository repository) {
        this.repository = repository;
    }
    
    public List<Bid> getBidsForItem(UUID itemId) {
        return repository.getBidsForItem(itemId);
    }
    
    public boolean addBid(UUID itemId, Bid bid) {
        return repository.addBid(itemId, bid);
    }
}