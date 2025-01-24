package auction.services;

import auction.models.Item;
import auction.repositories.ItemRepository;

public class ItemService {
    
    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }
    
    public boolean insert(Item newItem) {
        if (findById(newItem.getId().toString()) != null) {
            return false;
        }
        
        return repository.addItem(newItem);
    }
    
    public Item findByTitle(String title) {
        return repository.getItems().values()
                .stream()
                .filter(item -> item.getData().getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }
    
    public Item findById(String id) {
        return repository.getItems().values()
                .stream()
                .filter(item -> item.getId().toString().equals(id))
                .findFirst()
                .orElse(null);
    }
    
}
