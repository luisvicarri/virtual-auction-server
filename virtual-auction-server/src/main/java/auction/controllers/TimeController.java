package auction.controllers;

import auction.services.TimeService;
import auction.services.interfaces.TimeListener;
import java.time.Duration;

public class TimeController {
    
    private final TimeService service;

    public TimeController(TimeService service) {
        this.service = service;
    }
    
    public void startTimer(Duration duration) {
        service.startTimer(duration);
    }
    
    public void addListener(TimeListener listener) {
        service.addListener(listener);
    }
    
    public void notifyTimeUpdate(Duration timeLeft) {
        service.notifyTimeUpdate(timeLeft);
    }
}