package auction.views.panels.templates;

import auction.utils.FontUtil;
import auction.utils.ImageUtil;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class Product extends javax.swing.JPanel {

    private final FontUtil fontUtil;
    private final ImageUtil imageUtil;

    public Product() {
        initComponents();
        fontUtil = new FontUtil();
        imageUtil = new ImageUtil();
        customizeComponents();
    }

    private void customizeComponents() {
        String path = "views/fonts/Questrial-Regular.ttf";

        Map<JComponent, Float> components = Map.of(
                lbTitle, 16f,
                lbBidNow, 16f,
                lbCurrentBid, 12f,
                lbDuration, 12f,
                lbOpenningBid, 12f,
                lbReservePrice, 12f,
                lbStatus, 12f
        );

        fontUtil.applyFont(components, path);
        
        ImageIcon originalIcon = imageUtil.createImageIcon("/views/icons/icWhiteTimer.png");
        ImageIcon resizedIcon = imageUtil.resizeIcon(originalIcon, 19, 19);
        lbDuration.setIcon(resizedIcon);
    }

    public JLabel getLbBidNow() {
        return lbBidNow;
    }

    public JLabel getLbCurrentBid() {
        return lbCurrentBid;
    }

    public JLabel getLbDuration() {
        return lbDuration;
    }

    public JLabel getLbOpenningBid() {
        return lbOpenningBid;
    }

    public JLabel getLbReservePrice() {
        return lbReservePrice;
    }

    public JLabel getLbStatus() {
        return lbStatus;
    }

    public JLabel getLbTitle() {
        return lbTitle;
    }

    public JLabel getLbPhoto() {
        return lbPhoto;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbTitle = new javax.swing.JLabel();
        lbDuration = new javax.swing.JLabel();
        lbBidNow = new javax.swing.JLabel();
        lbCurrentBid = new javax.swing.JLabel();
        lbOpenningBid = new javax.swing.JLabel();
        lbReservePrice = new javax.swing.JLabel();
        lbStatus = new javax.swing.JLabel();
        lbPhoto = new javax.swing.JLabel();
        lbBackground = new javax.swing.JLabel();

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(255, 255, 255));
        lbTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTitle.setText("<title>");
        add(lbTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 263, 50));

        lbDuration.setForeground(new java.awt.Color(255, 255, 255));
        lbDuration.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbDuration.setText("<int>hrs : <int>mins : <int>secs ");
        add(lbDuration, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 350, 144, 50));

        lbBidNow.setForeground(new java.awt.Color(0, 0, 0));
        lbBidNow.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbBidNow.setText("BID NOW");
        lbBidNow.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        add(lbBidNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 350, 119, 50));

        lbCurrentBid.setForeground(new java.awt.Color(0, 0, 0));
        lbCurrentBid.setText("  Current Bid: <double>");
        add(lbCurrentBid, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 131, 25));

        lbOpenningBid.setForeground(new java.awt.Color(0, 0, 0));
        lbOpenningBid.setText("  Openning Bid: <double>");
        add(lbOpenningBid, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 326, 131, 25));

        lbReservePrice.setForeground(new java.awt.Color(0, 0, 0));
        lbReservePrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbReservePrice.setText("Reserve Price: <double> ");
        add(lbReservePrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 326, 131, 25));

        lbStatus.setForeground(new java.awt.Color(0, 0, 0));
        lbStatus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbStatus.setText("Status: <String>");
        add(lbStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 300, 131, 25));

        lbPhoto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        add(lbPhoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 263, 250));

        lbBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/backgrounds/bgTemplate.png"))); // NOI18N
        add(lbBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 263, 401));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbBackground;
    public javax.swing.JLabel lbBidNow;
    public javax.swing.JLabel lbCurrentBid;
    public javax.swing.JLabel lbDuration;
    public javax.swing.JLabel lbOpenningBid;
    public javax.swing.JLabel lbPhoto;
    public javax.swing.JLabel lbReservePrice;
    public javax.swing.JLabel lbStatus;
    public javax.swing.JLabel lbTitle;
    // End of variables declaration//GEN-END:variables
}