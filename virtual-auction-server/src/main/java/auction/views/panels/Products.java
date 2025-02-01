package auction.views.panels;

import auction.controllers.BiddingController;
import auction.controllers.ItemController;
import auction.dispatchers.MessageDispatcher;
import auction.handlers.PlaceBid;
import auction.main.ServerAuctionApp;
import auction.models.Bid;
import auction.models.Item;
import auction.models.ItemData;
import auction.models.dtos.Response;
import auction.services.AuctionService;
import auction.utils.FontUtil;
import auction.utils.ImageUtil;
import auction.utils.JsonUtil;
import auction.views.components.ScrollBarCustom;
import auction.views.panels.templates.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.awt.Dimension;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.slf4j.LoggerFactory;

public class Products extends javax.swing.JPanel {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Products.class);
    private final ImageUtil imageUtil;
    private final FontUtil fontUtil;

    public Products() {
        initComponents();
        imageUtil = new ImageUtil();
        fontUtil = new FontUtil();
        customizeComponents();

        productsDisplay.setLayout(null);
        spDisplay.setVerticalScrollBar(new ScrollBarCustom());

        initializeProducts();
    }

    private void customizeComponents() {
        String path = "views/fonts/Questrial-Regular.ttf";

        Map<JComponent, Float> components = Map.of(
                lbName, 20f,
                lbTitle, 16f,
                lbAuction, 14f,
                lbDashboard, 14f,
                lbOptions, 14f,
                lbProducts, 14f
        );

        fontUtil.applyFont(components, path);

        ImageIcon originalIcon = imageUtil.createImageIcon("/views/icons/icAdmin.png");
        ImageIcon resizedIcon = imageUtil.resizeIcon(originalIcon, 30, 30);
        lbUser.setIcon(resizedIcon);

        originalIcon = imageUtil.createImageIcon("/views/icons/icAdd.png");
        resizedIcon = imageUtil.resizeIcon(originalIcon, 30, 30);
        lbAdd.setIcon(resizedIcon);
    }

    private Item createItem(String title, String description, double reservePrice, Duration duration, String image) {
        ItemData data = new ItemData(
                title,
                description,
                (reservePrice * 0.1), // Calcula o lance inicial como 10% do preço de reserva
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

    private void initializeProducts() {
        ItemController controller = ServerAuctionApp.frame.getAppController().getItemController();

        // Carrega os produtos do repositório (se não houver, adiciona os padrões)
        List<Item> items = loadItemsFromRepository(controller);

        // Exibe os produtos na interface
        addProductTemplates(items);
    }

    private List<Item> loadItemsFromRepository(ItemController controller) {
        Map<UUID, Item> itemsMap = controller.getItems();

        // Se já houver produtos no repositório, retorna a lista
        if (!itemsMap.isEmpty()) {
            logger.info("Produtos existentes no repositório");
            return new ArrayList<>(itemsMap.values());
        }

        // Se o repositório estiver vazio, carrega os produtos padrões
        List<Item> products = new ArrayList<>();

        products.add(createItem("Tênis Nike Air Max", "Tênis esportivo de alta performance da Nike", 600, Duration.ofMinutes(5), "/views/products/imProduct01.png"));
        products.add(createItem("Xbox Series S", "Console de videogame Xbox Series S com armazenamento de 512GB", 800, Duration.ofHours(1), "/views/products/imProduct02.png"));
        products.add(createItem("Apple Watch Series 10", "Relógio inteligente da Apple com monitoramento de saúde avançado", 1200, Duration.ofHours(2), "/views/products/imProduct03.png"));
        products.add(createItem("iPhone 15", "Smartphone Apple com design moderno e câmera aprimorada", 900, Duration.ofMinutes(30), "/views/products/imProduct04.png"));
        products.add(createItem("Rolex", "Relógio de luxo da marca Rolex", 1300, Duration.ofMinutes(30), "/views/products/imProduct05.png"));

        // Adiciona os produtos padrões ao repositório
        for (Item item : products) {
            controller.addItem(item);
        }

        return products;
    }

    private void addProductTemplates(List<Item> items) {
        int colCount = 4; // Número de colunas
        int rowSpacing = 20; // Espaçamento entre linhas
        int colSpacing = 20; // Espaçamento entre colunas
        int templateWidth = 263; // Largura do template
        int templateHeight = 401; // Altura do template

        int totalRows = (int) Math.ceil((double) items.size() / colCount);

        // Ajusta o tamanho do painel "productsDisplay"
        int panelWidth = colCount * (templateWidth + colSpacing) - colSpacing;
        int panelHeight = totalRows * (templateHeight + rowSpacing) - rowSpacing;
        productsDisplay.setPreferredSize(new Dimension(panelWidth, panelHeight));

        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);

            // Cria uma nova instância do TemplatePanel
            Product template = new Product();

            // Preenche os dados no template
            template.getLbTitle().setText(item.getData().getTitle());
            template.getLbCurrentBid().setText("Current Bid: " + String.valueOf(item.getCurrentBid()));
            template.getLbOpenningBid().setText("Openning Bid: " + String.valueOf(item.getOpeningBid()));
            template.getLbReservePrice().setText("Reserve Price: " + String.valueOf(item.getData().getReservePrice()));
            template.getLbStatus().setText("Status: " + item.getStatus().toString());

            Duration time = item.getData().getAuctionDuration();
            long hours = time.toHours();
            long minutes = time.toMinutes() % 60; // Obtém os minutos restantes
            long seconds = time.getSeconds() % 60; // Obtém os segundos restantes
            template.getLbDuration().setText(hours + "hrs : " + minutes + "mins : " + seconds + "secs");

            ImageIcon originalIcon = imageUtil.createImageIcon(item.getData().getItemImage());
            ImageIcon resizedIcon = imageUtil.resizeIcon(originalIcon, 263, 250);
            template.getLbPhoto().setIcon(resizedIcon);

            // Adiciona um listener à label de bid
            JLabel bidNowLabel = template.getLbBidNow();

            // Associa o item à label usando putClientProperty
            bidNowLabel.putClientProperty("item", item);

            bidNowLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    JLabel clickedLabel = (JLabel) e.getSource();

                    Item associatedItem = (Item) clickedLabel.getClientProperty("item");
                    Response response = generateResponse(associatedItem);

                    // Enviar a resposta serializada como JSON
                    ServerAuctionApp.frame.getAppController().getMulticastController().send(response);

                    MessageDispatcher dispatcher = ServerAuctionApp.frame.getAppController().getMulticastController().getDispatcher();
                    ServerAuctionApp.frame.getAppController().getMulticastController().startListening(dispatcher::addMessage);

                    Duration auctionDuration = associatedItem.getData().getAuctionDuration();
                    ServerAuctionApp.frame.getAppController().getTimeController().startTimer(auctionDuration);
                    ServerAuctionApp.frame.getAppController().getMulticastController().send(associatedItem);

                }
            });

            // Calcula a posição
            int x = (i % colCount) * (templateWidth + colSpacing);
            int y = (i / colCount) * (templateHeight + rowSpacing);

            // Define a posição e o tamanho do template
            template.setBounds(x, y, templateWidth, templateHeight);

            // Adiciona o template ao painel "productsDisplay"
            productsDisplay.add(template);
        }

        // Atualiza o painel
        productsDisplay.revalidate();
        productsDisplay.repaint();
    }

    private Response generateResponse(Item associatedItem) {
        if (associatedItem == null) {
            logger.error("Associated item is null.");
            return new Response("error", "Associated item is null");
        }

        // Criar a resposta com status, mensagem e o item dentro do "data"
        Response response = new Response("AUCTION-STARTED", "The auction has started!");
        response.addData("item", associatedItem);

        BiddingController biddingController = ServerAuctionApp.frame.getAppController().getBiddingController();
        List<Bid> bids = biddingController.getBidsForItem(associatedItem.getId());
        response.addData("bids", bids);
        
        return response;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spDisplay = new javax.swing.JScrollPane();
        productsDisplay = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        lbAdd = new javax.swing.JLabel();
        lbName = new javax.swing.JLabel();
        lbDashboard = new javax.swing.JLabel();
        lbAuction = new javax.swing.JLabel();
        lbProducts = new javax.swing.JLabel();
        lbOptions = new javax.swing.JLabel();
        lbUser = new javax.swing.JLabel();
        lbBackground = new javax.swing.JLabel();

        setOpaque(false);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        spDisplay.setBackground(new java.awt.Color(255, 255, 255));
        spDisplay.setBorder(null);
        spDisplay.setForeground(new java.awt.Color(255, 255, 255));

        productsDisplay.setBackground(new java.awt.Color(255, 255, 255));
        productsDisplay.setForeground(new java.awt.Color(255, 255, 255));
        productsDisplay.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        spDisplay.setViewportView(productsDisplay);

        add(spDisplay, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 139, 1126, 405));

        lbTitle.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(0, 0, 0));
        lbTitle.setText("Registered Products");
        add(lbTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 79, 1066, 60));

        lbAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbAddMouseClicked(evt);
            }
        });
        add(lbAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(1143, 79, 60, 60));

        lbName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbName.setForeground(new java.awt.Color(255, 255, 255));
        lbName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbName.setText("Auction");
        lbName.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        add(lbName, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 15, 120, 30));

        lbDashboard.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbDashboard.setForeground(new java.awt.Color(255, 255, 255));
        lbDashboard.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbDashboard.setText("Dashboard");
        lbDashboard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbDashboard.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lbDashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbDashboardMouseClicked(evt);
            }
        });
        add(lbDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 15, 120, 30));

        lbAuction.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbAuction.setForeground(new java.awt.Color(255, 255, 255));
        lbAuction.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbAuction.setText("Auction");
        lbAuction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbAuction.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lbAuction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbAuctionMouseClicked(evt);
            }
        });
        add(lbAuction, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 15, 120, 30));

        lbProducts.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbProducts.setForeground(new java.awt.Color(255, 255, 255));
        lbProducts.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbProducts.setText("Products");
        lbProducts.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbProducts.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        add(lbProducts, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 15, 120, 30));

        lbOptions.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbOptions.setForeground(new java.awt.Color(255, 255, 255));
        lbOptions.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbOptions.setText("Options");
        lbOptions.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbOptions.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lbOptions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbOptionsMouseClicked(evt);
            }
        });
        add(lbOptions, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 15, 120, 30));

        lbUser.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbUser.setForeground(new java.awt.Color(255, 255, 255));
        lbUser.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbUser.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        add(lbUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(1181, 15, 30, 30));

        lbBackground.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/backgrounds/bgProducts.png"))); // NOI18N
        add(lbBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 600));
    }// </editor-fold>//GEN-END:initComponents

    private void lbDashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbDashboardMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lbDashboardMouseClicked

    private void lbAuctionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbAuctionMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lbAuctionMouseClicked

    private void lbOptionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbOptionsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lbOptionsMouseClicked

    private void lbAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbAddMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lbAddMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbAdd;
    private javax.swing.JLabel lbAuction;
    private javax.swing.JLabel lbBackground;
    private javax.swing.JLabel lbDashboard;
    private javax.swing.JLabel lbName;
    private javax.swing.JLabel lbOptions;
    private javax.swing.JLabel lbProducts;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbUser;
    private javax.swing.JPanel productsDisplay;
    private javax.swing.JScrollPane spDisplay;
    // End of variables declaration//GEN-END:variables
}
