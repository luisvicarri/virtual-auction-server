package auction.main;

import auction.controllers.AppController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Seeding {

    private final AppController appController;

    public Seeding(AppController appController) {
        this.appController = appController;
    }
    
    public void start() {
        new Thread(() -> {
            try {
                appController.getUserStub().startListening();
            } catch (IOException ex) {
                Logger.getLogger(Seeding.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

}
