package auction.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    
    /**
     * Gera um hash para a senha usando o algoritmo bcrypt.
     * 
     * @param password A senha em texto plano.
     * @return O hash da senha.
     */
    public static String hashPassword(String password) {
        // Gera o hash com um fator de complexidade padrão
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * Verifica se a senha fornecida corresponde ao hash armazenado.
     * 
     * @param password A senha em texto plano.
     * @param hashedPassword O hash da senha armazenado.
     * @return true se a senha for válida; false caso contrário.
     */
    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
    
}
