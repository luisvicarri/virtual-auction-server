package auction.main;

import auction.controllers.AppController;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Seeding {

    private final AppController appController;
    private static final Set<String> connectedClients = new HashSet<>();

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
            
            System.out.println("Servidor aguardando conexões de clientes...");
            while (connectedClients.isEmpty()) {
                String message = appController.getMulticastController().receiveString();
                if ("CLIENT_CONNECTED".equals(message)) {
                    connectedClients.add(message); // Registra o cliente conectado
                    System.out.println("Cliente conectado! Total de clientes: " + connectedClients.size());
                    
                }
            }
        }).start();
    }

}
