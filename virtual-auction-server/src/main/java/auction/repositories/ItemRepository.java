package auction.repositories;

import auction.models.Item;
import auction.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemRepository {

    private static final Logger logger = LoggerFactory.getLogger(ItemRepository.class);
    private final File file = new File("repositories/items/items.json");
    private Map<UUID, Item> items = new HashMap<>();
    private final ObjectMapper mapper = JsonUtil.getObjectMapper();
    private List<Item> itemsList = new ArrayList<>();

    public ItemRepository() {
        loadItems();
    }

    public Map<UUID, Item> getItems() {
        return items;
    }

    public void setItems(Map<UUID, Item> items) {
        this.items = items;
    }

    public List<Item> getItemsList() {
        return itemsList;
    }
    
    public boolean addItem(Item newItem) {
        if (items.containsKey(newItem.getId())) {
            logger.warn("Attempt to add item with existing UUID: {}", newItem.getId());
            return false;
        }

        items.put(newItem.getId(), newItem);
        itemsList.add(newItem);
        logger.info("Item successfully added: {}", newItem.getData().getTitle());

        try {
            saveItems();
            logger.info("Items successfully saved after adding: {}", newItem.getId());
            return true;
        } catch (Exception ex) {
            items.remove(newItem.getId());
            logger.error("Error saving items. Removing newly added item: {}", newItem.getId(), ex);
            return false;
        }

    }

    private void saveItems() {
        try {
            if (items.containsKey(null)) {
                items.remove(null);
                logger.warn("Removed null key from items map before saving.");
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, items);
        } catch (IOException ex) {
            logger.error("Error saving items to JSON file", ex);
        }
    }

    /**
     * Atualiza um item já existente e salva as alterações no arquivo. Retorna
     * true se a atualização for bem-sucedida.
     */
    public boolean updateItem(Item updatedItem) {
        if (updatedItem == null || updatedItem.getId() == null) {
            logger.warn("Invalid item provided for update.");
            return false;
        }

        if (!items.containsKey(updatedItem.getId())) {
            logger.warn("Item not found for update: {}", updatedItem.getId());
            return false;
        }

        // Atualiza o item no mapa
        items.put(updatedItem.getId(), updatedItem);
        logger.info("Item updated: {}", updatedItem.getData().getTitle());

        try {
            saveItems();
            logger.info("Items successfully saved after update: {}", updatedItem.getId());
            return true;
        } catch (Exception ex) {
            logger.error("Error saving updated item: {}", updatedItem.getId(), ex);
            return false;
        }
    }

    private void loadItems() {
        if (file.exists()) {
            try {
                items = mapper.readValue(file, new TypeReference<Map<UUID, Item>>() {});
                itemsList = new ArrayList<>(items.values());
            } catch (IOException ex) {
                logger.error("Error loading items", ex);
            }
        }
    }
}