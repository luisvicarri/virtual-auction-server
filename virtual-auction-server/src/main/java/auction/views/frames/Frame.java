package auction.views.frames;

import auction.controllers.AppController;
import auction.models.Auction;
import auction.views.panels.PnAddItem;
import auction.views.panels.PnAuction;
import auction.views.panels.PnProducts;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Frame extends javax.swing.JFrame {

    private final AppController appController;
    private final Auction auction;
    
    public static PnProducts pnProducts;
    public static PnAuction pnAuction;
    public static PnAddItem pnAddItem;

    public Frame(AppController appController) {
        initComponents();
        this.appController = appController;
        auction = new Auction();
    }

    public AppController getAppController() {
        return appController;
    }

    public Auction getAuction() {
        return auction;
    }
    
    public void start() {
        this.setLayout(new BorderLayout());

        pnProducts = new PnProducts();
        initNewPanel(pnProducts);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void initNewPanel(JPanel newJPanel) {
        this.getContentPane().removeAll();
        this.add(newJPanel, BorderLayout.CENTER);
        this.pack();
    }

    public void clearForm(JComponent... components) {
        for (JComponent component : components) {
            if (component instanceof JTextField jTextField) {
                jTextField.setText("");
            } else if (component instanceof JPasswordField jPasswordField) {
                jPasswordField.setText("");
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}