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
    private static final Set<String> connectedClients = new HashSet<>();

    public Seeding(AppController appController) {
        this.appController = appController;
    }

    public void start() {
//        MessageDispatcher dispatcher = appController.getMulticastController().getDispatcher();
//        dispatcher.registerHandler("CLIENT_CONNECTED", new ClientConnected(new AuctionService()));
        
        new Thread(() -> {
            try {
                appController.getUserStub().startListening();
            } catch (IOException ex) {
                Logger.getLogger(Seeding.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();

//        new Thread(() -> {
//            appController.getMulticastController().connect();
//            
//            System.out.println("Servidor aguardando conexões de clientes...");
//            while (connectedClients.isEmpty()) {
//                String message = appController.getMulticastController().receiveString();
//                if ("CLIENT_CONNECTED".equals(message)) {
//                    connectedClients.add(message); // Registra o cliente conectado
//                    System.out.println("Cliente conectado! Total de clientes: " + connectedClients.size());
//                    
//                }
//            }
//        }).start();

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
