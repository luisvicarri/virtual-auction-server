package auction.controllers;

import auction.services.MulticastService;

public class MulticastController {
    
    private final MulticastService service;

    public MulticastController(MulticastService service) {
        this.service = service;
    }
    
    public void connect() {
        service.connect();
    }
    
    public void disconnect() {
        service.disconnect();
    }
    
    public void send(String msg) {
        service.send(msg);
    }
    
    public void send(Object obj) {
        service.send(obj);
    }
    
    public String receiveString() {
        return service.receiveString();
    }
    
    public <T> T receiveObject(Class<T> type) {
        return service.receiveObject(type);
    }
    
}
