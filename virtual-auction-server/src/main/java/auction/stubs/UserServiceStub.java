package auction.stubs;

import auction.main.ServerAuctionApp;
import auction.models.dtos.Request;
import auction.models.dtos.Response;
import auction.security.SecurityMiddleware;
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
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceStub {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceStub.class);
    private final ObjectMapper mapper = JsonUtil.getObjectMapper();
    private final SecurityMiddleware securityMiddleware;
    private final UserService service;
    private final int port;

    public UserServiceStub(UserService service) {
        this.service = service;
        this.port = Integer.parseInt(ConfigManager.get("PORT"));
        this.securityMiddleware = new SecurityMiddleware();
    }

    public void startListening() throws IOException {
        try ( ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server started on port {}", port);

            while (true) {
                try ( Socket clientSocket = serverSocket.accept();  BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                    logger.info("Connection established with client: {}", clientSocket.getRemoteSocketAddress());

                    String requestJson = in.readLine();
                    String signature = in.readLine();
                    logger.info("Received request: {}", requestJson);
                    logger.info("Received signature: {}", signature);

                    String responseJson = handleRequest(requestJson, signature);
//                    // Assinar a resposta
//                    String responseSignature = signResponse(responseJson);

                    logger.info("Sending response: {}", responseJson);
                    out.println(responseJson);
//                    if (signature != null) {
//                        out.println(responseSignature);
//                    } else {
//                        out.println();
//                    }

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

    private String handleRequest(String requestJson, String signature) {
        try {
            Request request = mapper.readValue(requestJson, Request.class);

            String status = request.getStatus();
            if ("SIGN-UP".equals(status)) {
                return handleSignUp(request);
            }

            if ("SIGN-IN".equals(status)) {
                // Validação da assinatura
                if (signature == null || !validateSignature(requestJson, signature, request)) {
                    logger.warn("Invalid signature for request: {}", requestJson);
                    return mapper.writeValueAsString(new Response("FAILED", "Invalid signature"));
                }

                return handleSignIn(request);
            }

            logger.warn("Unknown action received: {}", status);
            return mapper.writeValueAsString(new Response("FAILED", "Unknown action"));

        } catch (IOException e) {
            logger.error("Invalid request format: {}", requestJson, e);
            return createErrorResponse("Invalid request");
        }
    }

    private boolean validateSignature(String requestJson, String signature, Request request) {
        return request.getData()
                .map(data -> data.get("user_id"))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .map(UUID::fromString)
                .map(userId -> securityMiddleware.verifyRequest(requestJson, signature, userId))
                .orElse(false);
    }

    private String handleSignUp(Request request) throws IOException {
        Map<String, Object> data = request.getData().orElseThrow(() -> new IllegalArgumentException("Missing data"));
        String username = (String) data.get("username");
        String passwordHash = (String) data.get("password_hash");
        String encodedPublicKey = (String) data.get("public_key");

        if (username == null || passwordHash == null || encodedPublicKey == null) {
            return createErrorResponse("Missing required fields");
        }

        UUID id = service.insert(username, passwordHash, encodedPublicKey);
        logger.info("User registered successfully: {}", id);

        Response response = new Response(
                "SUCCESS",
                "User registered successfully"
        );
        response.addData("userId", id.toString());
        response.addData("server-public-key", getEncodedServerPublicKey());

        return mapper.writeValueAsString(response);
    }

    private String handleSignIn(Request request) throws IOException {
        Map<String, Object> data = request.getData().orElseThrow(() -> new IllegalArgumentException("Missing data"));
        String username = (String) data.get("username");
        String password = (String) data.get("password");
        UUID userId = data.containsKey("user_id") ? UUID.fromString(data.get("user_id").toString()) : null;

        if (userId == null) {
            logger.warn("Missing user_id in request");
            return createErrorResponse("Missing user ID");
        }

        if (username == null || password == null) {
            return createErrorResponse("Missing required fields");
        }

        boolean found = service.signIn(userId, username, password);
        if (found) {
            logger.info("User found successfully: {}", userId.toString());
            Response response = new Response(
                    "SUCCESS",
                    "User found successfully"
            );
            response.addData("MULTICAST_ADDRESS", ConfigManager.get("MULTICAST_ADDRESS"));
            return mapper.writeValueAsString(response);
        }

        logger.info("Failed to authenticate user: {}", username);
        return mapper.writeValueAsString(new Response("FAILED", "Invalid credentials"));
    }

    private String signResponse(String responseJson) {
        KeyPair serverKeyPair = ServerAuctionApp.frame.getAppController()
                .getServerController()
                .getKeyPair();
        return securityMiddleware.signRequest(responseJson, serverKeyPair.getPrivate());
    }

    private String createErrorResponse(String message) {
        try {
            return mapper.writeValueAsString(new Response("FAILED", message));
        } catch (IOException ex) {
            logger.error("Critical failure serializing error response", ex);
            return "{\"status\": \"FAILED\", \"message\": \"Critical failure\"}";
        }
    }

    private String getEncodedServerPublicKey() {
        return Base64.getEncoder().encodeToString(
                ServerAuctionApp.frame.getAppController()
                        .getServerController()
                        .getKeyPair()
                        .getPublic()
                        .getEncoded()
        );
    }
}
