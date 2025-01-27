package auction.utils;

import java.awt.Image;
import javax.swing.ImageIcon;

public class ImageUtil {
    
    public ImageIcon resizeIcon(ImageIcon icon, int maxWidth, int maxHeight) {
        Image img = icon.getImage();
        int originalWidth = icon.getIconWidth();
        int originalHeight = icon.getIconHeight();

        // Calcula a proporção
        double aspectRatio = (double) originalWidth / originalHeight;

        int newWidth = maxWidth;
        int newHeight = maxHeight;

        // Ajusta as dimensões mantendo a proporção
        if (originalWidth > originalHeight) {
            newHeight = (int) (maxWidth / aspectRatio);
        } else {
            newWidth = (int) (maxHeight * aspectRatio);
        }

        // Redimensiona a imagem
        Image resizedImg = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    public ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}