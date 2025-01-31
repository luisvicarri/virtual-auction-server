package auction.services;

import auction.models.dtos.Response;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MulticastService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MulticastService.class);

    private final String MULTICAST_ADDRESS;
    private final int PORT;
    private final ObjectMapper mapper;

    private MulticastSocket socket;
    private InetAddress group;
    private NetworkInterface networkInterface;

    public MulticastService() {
        this.MULTICAST_ADDRESS = ConfigManager.get("MULTICAST_ADDRESS");
        this.PORT = Integer.parseInt(ConfigManager.get("MULTICAST_PORT"));
        this.mapper = JsonUtil.getObjectMapper();
    }

    /**
     * Conecta-se ao grupo multicast.
     */
    public void connect() {
        try {
            LOGGER.info("Trying to connect to multicast group at {}:{}", MULTICAST_ADDRESS, PORT);

            this.group = InetAddress.getByName(MULTICAST_ADDRESS);
            this.networkInterface = getNetworkInterface();

            if (networkInterface == null) {
                throw new IOException("Could not find a valid network interface.");
            }

            this.socket = new MulticastSocket(PORT);
            this.socket.setNetworkInterface(networkInterface);
            this.socket.joinGroup(new InetSocketAddress(group, PORT), networkInterface);

            LOGGER.info("Connection to multicast group {}:{} succeeded.", MULTICAST_ADDRESS, PORT);
        } catch (IOException e) {
            LOGGER.error("Error connecting to multicast group.", e);
            throw new RuntimeException(e); // Propaga o erro para que o chamador saiba que não foi possível conectar
        }
    }

    /**
     * Desconecta-se do grupo multicast.
     */
    public void disconnect() {
        if (socket != null) {
            try {
                LOGGER.info("Leaving multicast group {}:{}.", MULTICAST_ADDRESS, PORT);
                socket.leaveGroup(new InetSocketAddress(group, PORT), networkInterface);
                socket.close();
                LOGGER.info("Successfully disconnected from multicast group.");
            } catch (IOException e) {
                LOGGER.error("Error disconnecting from multicast group.", e);
            }
        }
    }

    /**
     * Envia uma mensagem como string para o grupo multicast.
     */
    public void send(String msg) {
        try {
            LOGGER.info("Sending message: {}", msg);
            byte[] data = msg.getBytes();
            sendData(data);
        } catch (Exception e) {
            LOGGER.error("Error sending message.", e);
        }
    }

    /**
     * Envia um objeto para o grupo multicast, serializando-o como JSON.
     */
    public void send(Object obj) {
        try {
            String json = mapper.writeValueAsString(obj);
            LOGGER.info("Sending serialized object as JSON: {}", json);
            sendData(json.getBytes());
        } catch (IOException e) {
            LOGGER.error("Error converting object to JSON.", e);
        }
    }

    /**
     * Recebe uma mensagem do grupo multicast como string.
     */
    public String receiveString() {
        try {
            LOGGER.info("Waiting for multicast message...");
            String data = receiveData();
            LOGGER.info("Message received: {}", data);
            return data;
        } catch (IOException e) {
            LOGGER.error("Error receiving message.", e);
        }
        return null;
    }

    /**
     * Recebe um objeto do grupo multicast, desserializando-o de JSON.
     */
    public <T> T receiveObject(Class<T> type) {
        try {
            String receivedJson = receiveData();
            if (receivedJson != null) {
                LOGGER.info("Received JSON message: {}", receivedJson);
                return mapper.readValue(receivedJson, type);
            }
        } catch (IOException e) {
            LOGGER.error("Error deserializing JSON.", e);
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
            LOGGER.error("Error sending multicast packet.", e);
        }
    }

    /**
     * Recebe dados brutos (byte array) do grupo multicast.
     */
    private String receiveData() throws IOException {
        byte[] buffer = new byte[1024];
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
                    LOGGER.info("Network interface found: {}", netIf.getDisplayName());
                    return netIf;
                }
            }
        } catch (SocketException e) {
            LOGGER.error("Error searching for network interfaces.", e);
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
            System.out.println("MulticastController enviando atualização de tempo: \n" + message);
            send(message);
        } catch (JsonProcessingException e) {
            System.err.println("Erro ao serializar mensagem de tempo.");
        }
    }
}