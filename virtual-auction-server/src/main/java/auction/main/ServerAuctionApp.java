package auction.main;

import auction.utils.MulticastUtil;
import auction.views.frames.Frame;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerAuctionApp {

    public static Frame frame;
    private static final Set<String> connectedClients = new HashSet<>();

    public static void main(String[] args) {
        //        Seeding seed = new Seeding();
//        seed.start();

//        ServerAuctionApp.frame = new Frame();
//        ServerAuctionApp.frame.start();
        
        MulticastUtil multicastUtil = new MulticastUtil();

        // Conecta-se ao grupo multicast
        multicastUtil.connect();

        try {
            System.out.println("Servidor aguardando conexões de clientes...");
            while (connectedClients.isEmpty()) {
                String message = multicastUtil.receiveString();
                if ("CLIENT_CONNECTED".equals(message)) {
                    connectedClients.add(message); // Registra o cliente conectado
                    System.out.println("Cliente conectado! Total de clientes: " + connectedClients.size());
                }
            }

            System.out.println("Iniciando envio de mensagens...");
            // Começa a enviar mensagens
            for (int i = 1; i <= 5; i++) {
                String serverMessage = "Mensagem do servidor #" + i;
                multicastUtil.send(serverMessage);
                System.out.println("Servidor enviou: " + serverMessage);

                // Aguarda 2 segundos antes de enviar a próxima mensagem
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        } finally {
            // Desconecta-se do grupo multicast
            multicastUtil.disconnect();
        }

    }

}
