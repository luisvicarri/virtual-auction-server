package auction.repositories;

import auction.models.Bid;
import auction.utils.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BiddingRepository {

    private static final Logger logger = LoggerFactory.getLogger(BiddingRepository.class);
    private final Map<UUID, List<Bid>> bidsByItem = new HashMap<>();
    private final File file = new File("repositories/bidding/bids.json");
    private final ObjectMapper mapper = JsonUtil.getObjectMapper();

    public BiddingRepository() {
        clearBidsFile();
    }

    public Map<UUID, List<Bid>> getBidsByItem() {
        return bidsByItem;
    }

    /**
     * Retorna os lances de um item específico.
     */
    public synchronized List<Bid> getBidsForItem(UUID itemId) {
        return bidsByItem.getOrDefault(itemId, new ArrayList<>());
    }

    /**
     * Adiciona um lance para um item e salva no arquivo JSON.
     */
    public synchronized boolean addBid(UUID itemId, Bid bid) {
        List<Bid> bids = bidsByItem.computeIfAbsent(itemId, k -> new ArrayList<>());
        bids.add(bid);

        try {
            saveBids();
            logger.info("Saved bid for item {}: {}", itemId, bid.getAmount());
            return true;
        } catch (Exception ex) {
            bids.remove(bid);
            logger.error("Error saving bids. Removing newly added bid: {}", bid.getId(), ex);
            return false;
        }
    }

    /**
     * Salva os lances no arquivo JSON.
     */
    private void saveBids() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, bidsByItem);
        } catch (IOException ex) {
            logger.error("Error saving bids to JSON file.", ex);
        }
    }

    private void clearBidsFile() {
        try {
            if (file.exists()) {
                if (file.delete()) {
                    logger.info("Bids file deleted when starting repository.");
                } else {
                    logger.warn("Unable to delete bid file.");
                }
            }
            file.createNewFile();
        } catch (IOException ex) {
            logger.error("Error cleaning bid file.", ex);
        }
    }

}
