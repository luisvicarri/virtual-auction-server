package auction.utils;

import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ValidatorUtil {

   public boolean samePassword(String passwordStored, String passwordToCompare) {
        return passwordToCompare.equals(passwordStored);
    }

    public boolean areFieldsEmpty(JComponent... components) {
        for (JComponent component : components) {
            if (component instanceof JTextField jTextField) {
                if (jTextField.getText().trim().isEmpty()) {
                    return true; // Campo vazio
                }
            } else if (component instanceof JPasswordField jPasswordField) {
                if (new String(jPasswordField.getPassword()).trim().isEmpty()) {
                    return true; // Campo vazio
                }
            }
        }
        return false; // Nenhum campo vazio
    }
    
    // Validação para Reserve Price (deve ser um número válido)
    public boolean isValidReservePrice(String price) {
        return price.matches("^\\d+(\\.\\d{1,2})?$"); // Aceita números inteiros e decimais com até 2 casas
    }

    // Validação Duration (hh:mm:ss)
    public boolean isValidTime(String time) {
        return time.matches("^(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$");
    }
}