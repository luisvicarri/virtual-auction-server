package auction.views.panels;

import auction.main.ServerAuctionApp;
import auction.models.Item;
import auction.models.ItemData;
import auction.utils.FontUtil;
import auction.utils.ImageUtil;
import auction.utils.ValidatorUtil;
import auction.views.frames.Frame;
import java.io.File;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PnAddItem extends javax.swing.JPanel {

    private static final Logger logger = LoggerFactory.getLogger(PnAddItem.class);
    private final ValidatorUtil validator;
    private final ImageUtil imageUtil;
    private final FontUtil fontUtil;
    private String imagePath = "";

    public PnAddItem() {
        initComponents();
        validator = new ValidatorUtil();
        imageUtil = new ImageUtil();
        fontUtil = new FontUtil();
        customizeComponents();
    }

    private void customizeComponents() {
        String path = "views/fonts/Questrial-Regular.ttf";

        Map<JComponent, Float> components = Map.ofEntries(
                Map.entry(lbText, 32f),
                Map.entry(lbTitle, 14f),
                Map.entry(lbDescription, 14f),
                Map.entry(lbReservePrice, 14f),
                Map.entry(lbDuration, 14f),
                Map.entry(lbAdd, 14f),
                Map.entry(lbCancel, 14f),
                Map.entry(lbPhoto, 14f),
                Map.entry(tfTitle, 12f),
                Map.entry(tfDescription, 12f),
                Map.entry(tfReservePrice, 12f),
                Map.entry(tfDuration, 12f)
        );

        fontUtil.applyFont(components, path);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbTitle = new javax.swing.JLabel();
        tfTitle = new javax.swing.JTextField();
        lbDescription = new javax.swing.JLabel();
        tfDescription = new javax.swing.JTextField();
        lbReservePrice = new javax.swing.JLabel();
        tfReservePrice = new javax.swing.JTextField();
        lbDuration = new javax.swing.JLabel();
        tfDuration = new javax.swing.JTextField();
        lbPhoto = new javax.swing.JLabel();
        lbCancel = new javax.swing.JLabel();
        lbAdd = new javax.swing.JLabel();
        lbText = new javax.swing.JLabel();
        lbBackground = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTitle.setFont(new java.awt.Font("Leelawadee UI", 0, 12)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(0, 0, 0));
        lbTitle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbTitle.setText("Title");
        lbTitle.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        add(lbTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(646, 123, 359, 30));

        tfTitle.setBackground(new java.awt.Color(255, 255, 255));
        tfTitle.setForeground(new java.awt.Color(0, 0, 0));
        tfTitle.setBorder(null);
        add(tfTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(646, 155, 359, 35));

        lbDescription.setFont(new java.awt.Font("Leelawadee UI", 0, 12)); // NOI18N
        lbDescription.setForeground(new java.awt.Color(0, 0, 0));
        lbDescription.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbDescription.setText("Description");
        lbDescription.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        add(lbDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(646, 205, 359, 30));

        tfDescription.setBackground(new java.awt.Color(255, 255, 255));
        tfDescription.setForeground(new java.awt.Color(0, 0, 0));
        tfDescription.setBorder(null);
        add(tfDescription, new org.netbeans.lib.awtextra.AbsoluteConstraints(646, 237, 359, 35));

        lbReservePrice.setFont(new java.awt.Font("Leelawadee UI", 0, 12)); // NOI18N
        lbReservePrice.setForeground(new java.awt.Color(0, 0, 0));
        lbReservePrice.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbReservePrice.setText("Reserve Price");
        lbReservePrice.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        add(lbReservePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(646, 287, 359, 30));

        tfReservePrice.setBackground(new java.awt.Color(255, 255, 255));
        tfReservePrice.setForeground(new java.awt.Color(0, 0, 0));
        tfReservePrice.setBorder(null);
        add(tfReservePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(646, 319, 359, 35));

        lbDuration.setFont(new java.awt.Font("Leelawadee UI", 0, 12)); // NOI18N
        lbDuration.setForeground(new java.awt.Color(0, 0, 0));
        lbDuration.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbDuration.setText("Duration");
        lbDuration.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        add(lbDuration, new org.netbeans.lib.awtextra.AbsoluteConstraints(646, 369, 359, 30));

        tfDuration.setBackground(new java.awt.Color(255, 255, 255));
        tfDuration.setForeground(new java.awt.Color(0, 0, 0));
        tfDuration.setBorder(null);
        add(tfDuration, new org.netbeans.lib.awtextra.AbsoluteConstraints(646, 401, 359, 35));

        lbPhoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbPhoto.setText("Select an Image");
        lbPhoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lbPhoto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbPhoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbPhotoMouseClicked(evt);
            }
        });
        add(lbPhoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 123, 330, 375));

        lbCancel.setFont(new java.awt.Font("Leelawadee UI", 0, 18)); // NOI18N
        lbCancel.setForeground(new java.awt.Color(255, 255, 255));
        lbCancel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbCancel.setText("Cancel");
        lbCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbCancelMouseClicked(evt);
            }
        });
        add(lbCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(646, 454, 135, 45));

        lbAdd.setFont(new java.awt.Font("Leelawadee UI", 0, 18)); // NOI18N
        lbAdd.setForeground(new java.awt.Color(255, 255, 255));
        lbAdd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbAdd.setText("Add");
        lbAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbAddMouseClicked(evt);
            }
        });
        add(lbAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(871, 454, 135, 45));

        lbText.setFont(new java.awt.Font("Leelawadee UI", 1, 24)); // NOI18N
        lbText.setForeground(new java.awt.Color(0, 0, 0));
        lbText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbText.setText("Sign Up");
        add(lbText, new org.netbeans.lib.awtextra.AbsoluteConstraints(407, 50, 466, 67));

        lbBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/backgrounds/bcAddItem.png"))); // NOI18N
        add(lbBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 600));
    }// </editor-fold>//GEN-END:initComponents

    private void lbAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbAddMouseClicked
        String reservePriceStr = tfReservePrice.getText().trim();
        String durationStr = tfDuration.getText().trim();

        if (validator.areFieldsEmpty(tfTitle, tfDescription, tfReservePrice, tfDuration)) {
            logger.warn("The form contains unfilled fields");
            JOptionPane.showMessageDialog(null, "The form contains unfilled fields", "WARNING", JOptionPane.INFORMATION_MESSAGE);
        } else if (!validator.isValidReservePrice(reservePriceStr)) {
            JOptionPane.showMessageDialog(null, "Invalid Reserve Price! Must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!validator.isValidTime(durationStr)) {
            JOptionPane.showMessageDialog(null, "Invalid Duration! Must be in hh:mm:ss format.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (imagePath.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select an image!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            double reservePrice = Double.parseDouble(reservePriceStr);
            Duration duration = parseDuration(durationStr);

            // Criando o item corretamente com sua função `createItem`
            Item newItem = createItem(
                    tfTitle.getText(),
                    tfDescription.getText(),
                    reservePrice,
                    duration,
                    imagePath
            );
            boolean success = ServerAuctionApp.frame.getAppController()
                    .getItemController()
                    .addItem(newItem);

            if (success) {
                JOptionPane.showMessageDialog(null, "Item successfully added!", "Success", JOptionPane.INFORMATION_MESSAGE);
                Frame.pnProducts = new PnProducts();
                ServerAuctionApp.frame.initNewPanel(Frame.pnProducts);
            } else {
                JOptionPane.showMessageDialog(null, "Error adding item! Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                ServerAuctionApp.frame.clearForm(tfTitle, tfDescription, tfReservePrice, tfDuration);
            }
        }
        ServerAuctionApp.frame.clearForm(tfTitle, tfDescription, tfReservePrice, tfDuration);
    }//GEN-LAST:event_lbAddMouseClicked

    private static Duration parseDuration(String time) {
        String[] parts = time.split(":");
        return Duration.ofHours(Integer.parseInt(parts[0]))
                .plusMinutes(Integer.parseInt(parts[1]))
                .plusSeconds(Integer.parseInt(parts[2]));
    }

    private static Item createItem(String title, String description, double reservePrice, Duration duration, String image) {
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

    private void lbCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbCancelMouseClicked
        Frame.pnProducts = new PnProducts();
        ServerAuctionApp.frame.initNewPanel(Frame.pnProducts);
    }//GEN-LAST:event_lbCancelMouseClicked

    private void lbPhotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbPhotoMouseClicked
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an Image");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            imagePath = selectedFile.getAbsolutePath(); // Salva o caminho absoluto

            ImageIcon originalIcon = imageUtil.createImageIconAbsolute(imagePath);  // Usa o caminho absoluto
            ImageIcon resizedIcon = imageUtil.resizeIcon(originalIcon, 330, 375);

            lbPhoto.setIcon(resizedIcon);
            lbPhoto.setText("");
            lbPhoto.setBorder(null);

        }
    }//GEN-LAST:event_lbPhotoMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbAdd;
    private javax.swing.JLabel lbBackground;
    private javax.swing.JLabel lbCancel;
    private javax.swing.JLabel lbDescription;
    private javax.swing.JLabel lbDuration;
    private javax.swing.JLabel lbPhoto;
    private javax.swing.JLabel lbReservePrice;
    private javax.swing.JLabel lbText;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JTextField tfDescription;
    private javax.swing.JTextField tfDuration;
    private javax.swing.JTextField tfReservePrice;
    private javax.swing.JTextField tfTitle;
    // End of variables declaration//GEN-END:variables
}
