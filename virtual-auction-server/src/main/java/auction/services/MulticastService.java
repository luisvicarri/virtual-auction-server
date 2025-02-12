package auction.services;

import auction.main.ServerAuctionApp;
import auction.models.dtos.Response;
import auction.security.SymmetricUtil;
import auction.utils.ConfigManager;
import auction.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.Duration;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MulticastService {

    private static final Logger logger = LoggerFactory.getLogger(MulticastService.class);

    private final String MULTICAST_ADDRESS;
    private final int PORT;
    private final ObjectMapper mapper;

    private MulticastSocket socket;
    private InetAddress group;
    private NetworkInterface networkInterface;

    private final SymmetricUtil symmetricUtil;
    private SecretKey symmetricKey;
    private IvParameterSpec iv;

    public MulticastService() {
        this.MULTICAST_ADDRESS = ConfigManager.get("MULTICAST_ADDRESS");
        this.PORT = Integer.parseInt(ConfigManager.get("MULTICAST_PORT"));
        this.mapper = JsonUtil.getObjectMapper();
        this.symmetricUtil = new SymmetricUtil();
    }

    private SecretKey getSymmetricKey() {
        if (symmetricKey == null) {
            symmetricKey = ServerAuctionApp.frame.getAppController()
                    .getKeyController()
                    .loadSymmetricKey();
        }
        return symmetricKey;
    }

    private IvParameterSpec getIV() {
        if (iv == null) {
            iv = ServerAuctionApp.frame.getAppController()
                    .getKeyController()
                    .loadIV();
        }
        return iv;
    }

    /**
     * Conecta-se ao grupo multicast.
     */
    public void connect() {
        try {
            logger.info("Trying to connect to multicast group at {}:{}", MULTICAST_ADDRESS, PORT);

            this.group = InetAddress.getByName(MULTICAST_ADDRESS);
            this.networkInterface = getNetworkInterface();

            if (networkInterface == null) {
                throw new IOException("Could not find a valid network interface.");
            }

            this.socket = new MulticastSocket(PORT);
            this.socket.setNetworkInterface(networkInterface);
            this.socket.joinGroup(new InetSocketAddress(group, PORT), networkInterface);

            logger.info("Connection to multicast group {}:{} succeeded.", MULTICAST_ADDRESS, PORT);
        } catch (IOException e) {
            logger.error("Error connecting to multicast group.", e);
            throw new RuntimeException(e); // Propaga o erro para que o chamador saiba que não foi possível conectar
        }
    }

    /**
     * Desconecta-se do grupo multicast.
     */
    public void disconnect() {
        if (socket != null) {
            try {
                logger.info("Leaving multicast group {}:{}.", MULTICAST_ADDRESS, PORT);
                socket.leaveGroup(new InetSocketAddress(group, PORT), networkInterface);
                socket.close();
                logger.info("Successfully disconnected from multicast group.");
            } catch (IOException e) {
                logger.error("Error disconnecting from multicast group.", e);
            }
        }
    }

    /**
     * Envia uma mensagem como string para o grupo multicast.
     */
    public void send(String msg) {
        try {
            String encryptedMsg = symmetricUtil.encrypt(
                    msg,
                    getSymmetricKey(),
                    getIV()
            );
            sendData(encryptedMsg.getBytes());
        } catch (Exception e) {
            logger.error("Error sending message.", e);
        }
    }

    /**
     * Envia um objeto para o grupo multicast, serializando-o como JSON.
     */
    public void send(Object obj) {
        try {
            String json = mapper.writeValueAsString(obj);
            String encryptedMsg = symmetricUtil.encrypt(
                    json,
                    getSymmetricKey(),
                    getIV()
            );
            sendData(encryptedMsg.getBytes());
        } catch (IOException e) {
            logger.error("Error converting object to JSON.", e);
        }
    }

    /**
     * Recebe uma mensagem do grupo multicast como string.
     */
    public String receiveString() {
        try {
            String encryptedData = receiveData();
            String decryptedData = symmetricUtil.decrypt(
                    encryptedData,
                    getSymmetricKey(),
                    getIV()
            );
            return decryptedData;
        } catch (IOException e) {
            logger.error("Error receiving message.", e);
        }
        return null;
    }

    /**
     * Recebe um objeto do grupo multicast, desserializando-o de JSON.
     */
    public <T> T receiveObject(Class<T> type) {
        try {
            String encryptedJson = receiveData();
            if (encryptedJson != null) {
                String decryptedJson = symmetricUtil.decrypt(
                        encryptedJson,
                        getSymmetricKey(),
                        getIV()
                );
                return mapper.readValue(decryptedJson, type);
            }
        } catch (IOException e) {
            logger.error("Error deserializing JSON.", e);
        }
        return null;
    }

    /**
     * Envia dados brutos (byte array) para o grupo multicast.
     */
    private void sendData(byte[] data) {
        try ( DatagramSocket sendSocket = new DatagramSocket()) {
            DatagramPacket packet = new DatagramPacket(data, data.length, group, PORT);
            sendSocket.send(packet);
        } catch (IOException e) {
            logger.error("Error sending multicast packet.", e);
        }
    }

    /**
     * Recebe dados brutos (byte array) do grupo multicast.
     */
    private String receiveData() throws IOException {
        byte[] buffer = new byte[4096];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        return new String(packet.getData(), 0, packet.getLength());
    }

    /**
     * Obtém a primeira interface de rede válida disponível.
     */
    private NetworkInterface getNetworkInterface() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netIf = interfaces.nextElement();
                if (!netIf.isLoopback() && netIf.isUp()) {
                    logger.info("Network interface found: {}", netIf.getDisplayName());
                    return netIf;
                }
            }
        } catch (SocketException e) {
            logger.error("Error searching for network interfaces.", e);
        }
        return null;
    }

    public void onTimeUpdate(Duration timeLeft) {
        Map<String, Object> data = new HashMap<>();
        data.put("timeLeft", timeLeft.getSeconds());

        Response response = new Response("TIME-UPDATE", "Time updating", data);

        try {
            ObjectMapper objectMapper = JsonUtil.getObjectMapper();
            String message = objectMapper.writeValueAsString(response);
            send(message);
        } catch (JsonProcessingException ex) {
            logger.error("Error serializing time message.");
        }
    }

    public void startListening(Consumer<String> onMessageReceived) {
        new Thread(() -> {
            try {
                while (true) {
                    String message = receiveString();
                    if (message != null) {
                        ServerAuctionApp.frame.getAppController().getMulticastController().getDispatcher().addMessage(message); // Encaminha para o dispatcher
                    }
                }
            } catch (Exception ex) {
                logger.error("Error listening to multicast messages: {}", ex);
            }
        }).start();
    }
}
