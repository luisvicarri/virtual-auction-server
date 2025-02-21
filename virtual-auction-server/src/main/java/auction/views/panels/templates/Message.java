package auction.views.panels.templates;

import auction.utils.FontUtil;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class Message extends javax.swing.JPanel {

    private final FontUtil fontUtil;

    public Message() {
        initComponents();
        fontUtil = new FontUtil();
        customizeComponents();
    }

    private void customizeComponents() {
        String path = "views/fonts/Questrial-Regular.ttf";

        Map<JComponent, Float> components = Map.of(
                lbMessage, 16f,
                lbTimestamp, 10f
        );

        fontUtil.applyFont(components, path);
    }

    public JLabel getLbMessage() {
        return lbMessage;
    }

    public JLabel getLbTimestamp() {
        return lbTimestamp;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbMessage = new javax.swing.JLabel();
        lbTimestamp = new javax.swing.JLabel();
        lbBackground = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(291, 60));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(291, 60));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbMessage.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lbMessage.setForeground(new java.awt.Color(0, 0, 0));
        lbMessage.setText("<user> has the highest bid.");
        lbMessage.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        add(lbMessage, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 271, 30));

        lbTimestamp.setForeground(new java.awt.Color(0, 0, 0));
        lbTimestamp.setText("11hrs : 54mins : 56secs");
        lbTimestamp.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lbTimestamp.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        add(lbTimestamp, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 271, 30));

        lbBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/views/backgrounds/bgMessage.png"))); // NOI18N
        add(lbBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lbBackground;
    public javax.swing.JLabel lbMessage;
    public javax.swing.JLabel lbTimestamp;
    // End of variables declaration//GEN-END:variables
}