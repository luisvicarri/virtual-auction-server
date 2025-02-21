package auction.main;

import auction.controllers.AppController;
import auction.controllers.ItemController;
import auction.models.Item;
import auction.models.ItemData;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Seeding {

    private final AppController appController;

    public Seeding(AppController appController) {
        this.appController = appController;
    }

    public void insertItems() {
        ItemController itemController = appController.getItemController();
        itemController.addItem(createItem("Tênis Nike Air Max", "Tênis esportivo de alta performance da Nike", 100, Duration.ofSeconds(20), "/views/products/imProduct01.png"));
        itemController.addItem(createItem("Xbox Series S", "Console de videogame Xbox Series S com armazenamento de 512GB", 800, Duration.ofHours(1), "/views/products/imProduct02.png"));
        itemController.addItem(createItem("Apple Watch Series 10", "Relógio inteligente da Apple com monitoramento de saúde avançado", 1200, Duration.ofHours(2), "/views/products/imProduct03.png"));
    }
    
    private Item createItem(String title, String description, double reservePrice, Duration duration, String image) {
        ItemData data = new ItemData(
                title,
                description,
                (reservePrice * 0.5), // 50% do preço de reserva como lance inicial
                reservePrice,
                duration,
                image
        );

        return new Item(
                data,
                0.0, // Lance inicial como 0
                null, // Foto ou outro dado inicial como nulo
                Optional.empty(), // Outros valores opcionais
                Optional.empty(),
                Optional.empty()
        );
    }
    
    public void start() {
        insertItems();

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
                String message = appController.getMulticastController().receive();
                if (message != null) {
                    // Adiciona a mensagem ao dispatcher
                    appController.getMulticastController().getDispatcher().addMessage(message);
                }
            }
        }).start();
    }
}