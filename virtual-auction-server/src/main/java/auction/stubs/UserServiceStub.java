package auction.stubs;

import auction.models.dtos.Request;
import auction.models.dtos.Response;
import auction.services.UserService;
import auction.utils.ConfigManager;
import auction.utils.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceStub {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceStub.class);
    private final UserService service;
    private final ObjectMapper mapper = JsonUtil.getObjectMapper();
    private final int port;

    public UserServiceStub(UserService service) {
        this.service = service;
        this.port = Integer.parseInt(ConfigManager.get("PORT"));
    }

    public void startListening() throws IOException {
        try ( ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server started on port {}", port);

            while (true) {
                try ( Socket clientSocket = serverSocket.accept();  BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    logger.info("Connection established with client: {}", clientSocket.getRemoteSocketAddress());

                    String requestJson = in.readLine();
                    logger.info("Received request: {}", requestJson);

                    String responseJson = handleRequest(requestJson);
                    logger.info("Sending response: {}", responseJson);
                    out.println(responseJson);

                } catch (Exception ex) {
                    logger.error("Error processing client request", ex);
                }
            }
        } catch (IOException ex) {
            logger.error("Error starting server on port {}", port, ex);
            throw ex;
        } catch (RuntimeException ex) {
            logger.error("Server interrupted", ex);
            throw ex;
        }

    }

    private String handleRequest(String requestJson) {
        try {
            Request request = mapper.readValue(requestJson, Request.class);

            if ("SIGN-UP".equals(request.getAction())) {
                UUID id = service.insert(request.getName(), request.getPassword());
                logger.info("User registered successfully: {}", id.toString());
                return mapper.writeValueAsString(new Response("SUCCESS", id.toString()));

            }

            if ("SIGN-IN".equals(request.getAction())) {
                if (request.getId().isPresent()) {
                    boolean finded = service.signIn(
                            request.getId().get(),
                            request.getName(),
                            request.getPassword()
                    );
                    
                    if (finded) {
                        logger.info("User found successfully: {}", request.getId().toString());
                        return mapper.writeValueAsString(new Response("SUCCESS", request.getId().toString()));
                    }
                    
                    logger.info("Failed to find user: {}", request.getId().toString());
                    return mapper.writeValueAsString(new Response("FAILED", request.getId().toString()));
                    
                }

            }

            logger.warn("Unknown action received: {}", request.getAction());
            return mapper.writeValueAsString(new Response("FAILED", "Unknown action"));

        } catch (IOException e) {
            logger.error("Invalid request format: {}", requestJson, e);
            try {
                return mapper.writeValueAsString(new Response("FAILED", "Invalid request"));
            } catch (IOException ex) {
                logger.error("Critical failure serializing error response", ex);
                return "{\"status\": \"FAILED\", \"message\": \"Critical failure\"}";
            }
        }
    }

}