package auction.main;

import auction.controllers.AppController;
import auction.dispatchers.MessageDispatcher;
import auction.handlers.ClientConnected;
import auction.services.AuctionService;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
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

        new Thread(() -> {
            appController.getMulticastController().connect();
            while (true) {
                String message = appController.getMulticastController().receiveString();
                if (message != null) {
                    // Adiciona a mensagem ao dispatcher
                    appController.getMulticastController().getDispatcher().addMessage(message);
                }
            }
        }).start();
    }
}