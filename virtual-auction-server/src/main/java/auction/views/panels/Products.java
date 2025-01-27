package auction.views.panels;

import auction.models.Item;
import auction.models.ItemData;
import auction.utils.FontUtil;
import auction.utils.ImageUtil;
import auction.views.components.ScrollBarCustom;
import auction.views.panels.templates.Product;
import java.awt.Dimension;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class Products extends javax.swing.JPanel {

    private final ImageUtil imageUtil;
    private final FontUtil fontUtil;

    public Products() {
        initComponents();
        imageUtil = new ImageUtil();
        fontUtil = new FontUtil();
        customizeComponents();

        productsDisplay.setLayout(null);
        spDisplay.setVerticalScrollBar(new ScrollBarCustom());
        List<Item> items = loadItems();
        addProductTemplates(items);
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

    private List<Item> loadItems() {
        List<Item> products = new ArrayList<>();

        // Produto 1: Livro Harry Potter
        double reservePrice1 = 600;
        ItemData data1 = new ItemData(
                "Tênis Nike Air Max",
                "",
                (reservePrice1 * 0.1),
                reservePrice1,
                Duration.ofMinutes(5),
                "/views/products/imProduct01.png"
        );
        Item item1 = new Item(
                data1,
                0.0,
                null,
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
        products.add(item1);

        // Produto 2: Tênis Nike Air Max
        double reservePrice2 = 800;
        ItemData data2 = new ItemData(
                "Xbox Series S",
                "",
                (reservePrice2 * 0.1),
                reservePrice2,
                Duration.ofHours(1),
                "/views/products/imProduct02.png"
        );
        Item item2 = new Item(
                data2,
                0.0,
                null,
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
        products.add(item2);

        // Produto 3: Smartwatch Apple
        double reservePrice3 = 1200;
        ItemData data3 = new ItemData(
                "Apple Watch Series 10",
                "",
                (reservePrice3 * 0.1),
                reservePrice3,
                Duration.ofHours(2),
                "/views/products/imProduct03.png"
        );
        Item item3 = new Item(
                data3,
                0.0,
                null,
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
        products.add(item3);

        // Produto 4: Fone de Ouvido Bluetooth
        double reservePrice4 = 300;
        ItemData data4 = new ItemData(
                "iPhone 15",
                "",
                (reservePrice4 * 0.1),
                reservePrice4,
                Duration.ofMinutes(30),
                "/views/products/imProduct04.png"
        );
        Item item4 = new Item(
                data4,
                0.0,
                null,
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
        products.add(item4);

        // Produto 4: Fone de Ouvido Bluetooth
        double reservePrice5 = 300;
        ItemData data5 = new ItemData(
                "Rolex",
                "",
                (reservePrice5 * 0.1),
                reservePrice5,
                Duration.ofMinutes(30),
                "/views/products/imProduct05.png"
        );
        Item item5 = new Item(
                data5,
                0.0,
                null,
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );
        products.add(item5);

        return products;
    }

    private void addProductTemplates(List<Item> items) {
        int colCount = 4; // Número de colunas
        int rowSpacing = 20; // Espaçamento entre linhas
        int colSpacing = 20; // Espaçamento entre colunas
        int templateWidth = 263; // Largura do template
        int templateHeight = 401; // Altura do template

        int totalRows = (int) Math.ceil((double) items.size() / colCount);

        // Ajusta o tamanho do painel "auction"
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

            // Calcula a posição
            int x = (i % colCount) * (templateWidth + colSpacing);
            int y = (i / colCount) * (templateHeight + rowSpacing);

            // Define a posição e o tamanho do template
            template.setBounds(x, y, templateWidth, templateHeight);

            // Adiciona o template ao painel "auction"
            productsDisplay.add(template);
        }

        // Atualiza o painel
        productsDisplay.revalidate();
        productsDisplay.repaint();

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
        add(lbDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 15, 120, 30));

        lbAuction.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbAuction.setForeground(new java.awt.Color(255, 255, 255));
        lbAuction.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbAuction.setText("Auction");
        lbAuction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbAuction.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
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
