package auction.controllers;

import auction.models.Item;
import auction.services.ItemService;
import java.util.Map;
import java.util.UUID;

public class ItemController {
    
    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }
    
    public Item findById(UUID id) {
        return service.findById(id);
    }
    
    public boolean addItem(Item newItem) {
        return service.insert(newItem);
    }
    
    public Map<UUID, Item> getItems() {
        return service.getItems();
    }
}