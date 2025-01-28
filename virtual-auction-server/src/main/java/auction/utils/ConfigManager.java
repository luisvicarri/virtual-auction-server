package auction.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties config = new Properties();
    private static final String CONFIG_FILE = "repositories/config/config.properties";

    static {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            config.load(input);
        } catch (IOException e) {
            System.err.println("Erro ao carregar configurações: " + e.getMessage());
        }
    }

    // Método para obter uma configuração pelo nome da chave
    public static String get(String key) {
        return config.getProperty(key);
    }

    // Método para definir uma configuração e salvar no arquivo
    public static void set(String key, String value) {
        config.setProperty(key, value);
        saveProperties();
    }

    // Método para salvar as propriedades de volta no arquivo
    private static void saveProperties() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            config.store(output, "Arquivo de Configurações");
        } catch (IOException e) {
            System.err.println("Erro ao salvar configurações: " + e.getMessage());
        }
    }

}
