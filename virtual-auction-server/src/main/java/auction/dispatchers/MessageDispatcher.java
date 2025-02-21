package auction.dispatchers;

import auction.services.interfaces.MessageHandler;
import auction.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(MessageDispatcher.class);
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private final Map<String, MessageHandler> handlers = new HashMap<>();

    public MessageDispatcher() {
        // Thread que processa mensagens continuamente
        new Thread(() -> {
            while (true) {
                try {
                    String message = messageQueue.take(); // Bloqueia até receber uma mensagem
                    dispatch(message);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    logger.error("Dispatcher stopped", ex);
                    break;
                }
            }
        }).start();
    }

    public void registerHandler(String type, MessageHandler handler) {
        handlers.put(type, handler);
    }

    public void addMessage(String message) {
        messageQueue.offer(message);
    }

    private void dispatch(String message) {
        String messageType = extractMessageType(message);
        MessageHandler handler = handlers.get(messageType);
        if (handler != null) {
            handler.handle(message);
        } else {
            logger.warn("No handler found for message type: {}", messageType);
        }
    }

    private String extractMessageType(String message) {
        try {
            System.out.println("");
            logger.debug("Received message to extract status: {}", message);
            JsonNode jsonNode = JsonUtil.getObjectMapper().readTree(message);

            JsonNode statusNode = jsonNode.get("status");
            if (statusNode != null) {
                return statusNode.asText();
            }

            JsonNode typeNode = jsonNode.get("type");
            if (typeNode != null) {
                return typeNode.asText();
            }

            logger.error("Message without 'status' or 'type' field: {}", message);
            return "";
        } catch (JsonProcessingException ex) {
            logger.error("Error processing JSON: {}", message, ex);
            return "";
        }
    }
}