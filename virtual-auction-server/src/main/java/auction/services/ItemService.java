package auction.services;

import auction.models.Item;
import auction.repositories.ItemRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public Map<UUID, Item> getItems() {
        return repository.getItems();
    }
    
    public boolean insert(Item newItem) {
        if (findById(newItem.getId()) != null) {
            return false;
        }

        return repository.addItem(newItem);
    }
    
    public boolean updateItem(Item updatedItem) {
        return repository.updateItem(updatedItem);
    }

    public Item findByTitle(String title) {
        return repository.getItems().values()
                .stream()
                .filter(item -> item.getData().getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    public Item findById(UUID id) {
        logger.info("Itens disponíveis no repositório:");
        repository.getItems().values().forEach(item -> logger.info("Item: " + item.getId()));

        return repository.getItems().values()
                .stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Item> getItemsList() {
        return repository.getItemsList();
    }
}