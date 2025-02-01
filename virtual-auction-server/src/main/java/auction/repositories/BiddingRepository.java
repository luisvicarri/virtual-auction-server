package auction.repositories;

import auction.models.Bid;
import auction.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
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
        loadBids();
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
            logger.info("Lance salvo para o item {}: {}", itemId, bid.getAmount());
            return true;
        } catch (Exception ex) {
            bids.remove(bid);
            logger.error("Erro ao salvar lances. Removendo o lance recém-adicionado: {}", bid.getId(), ex);
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
            logger.error("Erro ao salvar lances no arquivo JSON", ex);
        }
    }

    /**
     * Carrega os lances do arquivo JSON para a memória.
     */
    private void loadBids() {
        if (file.exists()) {
            try {
                bidsByItem.clear();
                bidsByItem.putAll(mapper.readValue(file, new TypeReference<Map<UUID, List<Bid>>>() {}));
                logger.info("Lances carregados do arquivo com sucesso.");
            } catch (IOException ex) {
                logger.error("Erro ao carregar lances do arquivo JSON", ex);
            }
        } else {
            logger.warn("Arquivo de lances não encontrado. Criando novo repositório.");
        }
    }
}