package auction.services;

import auction.controllers.BiddingController;
import auction.enums.AuctionStatus;
import auction.enums.ItemStatus;
import auction.main.ServerAuctionApp;
import auction.models.Bid;
import auction.models.Item;
import auction.models.dtos.Response;
import auction.utils.JsonUtil;
import auction.utils.UIUpdateManager;
import auction.views.frames.Frame;
import auction.views.panels.PnProducts;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuctionService {

    private static final Logger logger = LoggerFactory.getLogger(AuctionService.class);
    private final ObjectMapper mapper = JsonUtil.getObjectMapper();

    public void processBid(String message) {
        try {
            // Verifica se a mensagem é válida
            if (!message.trim().startsWith("{")) {
                logger.info("Mensagem ignorada: " + message);
                return;
            }

            // Desserializa a mensagem para um objeto Response
            Response response = mapper.readValue(message, Response.class);

            // Verifica se a mensagem é um lance válido
            if (!"NEW-BID".equals(response.getStatus())) {
                logger.info("Mensagem ignorada (status diferente de NEW-BID): " + response.getStatus());
                return;
            }

            // Obtém os dados da resposta
            Optional<Map<String, Object>> optionalData = response.getData();
            if (optionalData.isEmpty()) {
                logger.warn("Nenhum dado encontrado na resposta.");
                return;
            }
            Map<String, Object> data = optionalData.get();

            // Verifica se os campos essenciais estão presentes
            Object bidObject = data.get("bid");
            Object itemIdObject = data.get("itemId");

            if (bidObject == null || itemIdObject == null) {
                logger.warn("Campos 'bid' ou 'itemId' ausentes.");
                return;
            }

            // Converte itemId para UUID
            UUID itemId;
            try {
                itemId = mapper.convertValue(itemIdObject, UUID.class);
            } catch (IllegalArgumentException e) {
                logger.warn("Formato inválido do itemId: " + itemIdObject);
                return;
            }

            // Verifica se o item existe
            Item associatedItem = ServerAuctionApp.frame.getAppController()
                    .getItemController().findById(itemId);

            if (associatedItem == null) {
                logger.warn("Item não encontrado: " + itemId);
                return;
            }

            // Converte o objeto bid
            if (!(bidObject instanceof Map<?, ?> bidMap)) {
                logger.warn("Formato inválido do campo 'bid'.");
                return;
            }
            Bid newBid = mapper.convertValue(bidMap, Bid.class);

            // Armazena o lance
            BiddingController biddingController = ServerAuctionApp.frame.getAppController().getBiddingController();

            if (biddingController.addBid(itemId, newBid)) {
                logger.debug("The bid was successfully registered.");
            } else {
                logger.warn("There was an error registering the bid.");
            }

            // Cria uma nova resposta para enviar aos clientes
            Response bidUpdateResponse = new Response("BID-UPDATED", "Bid has been updated.");
            bidUpdateResponse.addData("bids", biddingController.getBidsForItem(itemId));
            bidUpdateResponse.addData("itemId", itemId.toString());

            // Envia para os clientes
            ServerAuctionApp.frame.getAppController().getMulticastController().send(bidUpdateResponse);

            logger.info("Lance processado e enviado aos clientes.");
        } catch (JsonProcessingException e) {
            logger.error("Erro ao processar JSON recebido.", e);
        } catch (IllegalArgumentException e) {
            logger.error("Erro inesperado ao processar lance.", e);
        }
    }

    public void clientConnected(String message) {
        if (ServerAuctionApp.frame.getAuction().getStatus() == AuctionStatus.ONGOING) {

            ServerAuctionApp.frame.getAuction().getCurrentAuctionItem().ifPresent(currentItem -> {
                Response response = new Response("AUCTION-INFO", "Auction in progress");
                response.addData("item", currentItem);

                BiddingController biddingController = ServerAuctionApp.frame.getAppController().getBiddingController();
                List<Bid> bids = biddingController.getBidsForItem(currentItem.getId());
                response.addData("bids", bids);

                ServerAuctionApp.frame.getAppController().getMulticastController().send(response);
            });
        }

        try {
            JsonNode jsonNode = mapper.readTree(message);
            logger.info("New client connected: {}", jsonNode);
        } catch (JsonProcessingException e) {
            logger.error("Error processing CLIENT-CONNECTED message: {}", message, e);
        }
    }

    public void displayNewBid(String message) {
        try {
            Response response = mapper.readValue(message, Response.class);

            response.getData().ifPresent(data -> {

                UUID itemId = mapper.convertValue(data.get("itemId"), UUID.class);
                List<Map<String, Object>> bids = (List<Map<String, Object>>) data.get("bids");

                if (bids != null) {
                    // Atualizar a interface do leilão do servidor
                    List<Bid> updatedBids = ServerAuctionApp.frame.getAppController().getBiddingController().getBidsForItem(itemId);
                    UIUpdateManager.getBidListUpdater().accept(updatedBids);

                    Bid lastBid = updatedBids.get(updatedBids.size() - 1);
                    UIUpdateManager.getWinningBidderUpdater().accept("Winning Bidder: " + lastBid.getBidderName());
                    UIUpdateManager.getCurrentBidUpdater().accept("Current Bid: " + lastBid.getAmount());

                    // Atualizar o valor atual do item no servidor
                    Item currentItem = ServerAuctionApp.frame.getAppController().getItemController().findById(itemId);
                    if (currentItem != null) {
                        double currentBid = currentItem.getCurrentBid();
                        double bidIncrement = currentItem.getData().getBidIncrement();
                        currentItem.setCurrentBid(currentBid + bidIncrement);
                        logger.info("Current bid updated to {}", currentItem.getCurrentBid());
                    }

                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "User " + lastBid.getBidderName() + " placed a bid", "INFO", JOptionPane.INFORMATION_MESSAGE));
                    logger.debug("User: {} placed a bid.", lastBid.getBidderName());

                }
            });
        } catch (JsonProcessingException ex) {
            logger.error("Error parsing message {} to json", message, ex);
        }
    }

    public void updateTime(String message) {
        try {
            Response response = mapper.readValue(message, Response.class);
            response.getData().ifPresent(data -> {
                Object timeObj = data.get("timeLeft");
                if (timeObj instanceof Number number) {
                    long timeLeftSeconds = number.longValue(); // Obtém o tempo em segundos

                    // Converte para Duration
                    Duration duration = Duration.ofSeconds(timeLeftSeconds);

                    // Formata para o padrão hh:mm:ss
                    String formattedDuration = String.format("%02dhrs : %02dmins : %02dsecs",
                            duration.toHours(),
                            duration.toMinutesPart(),
                            duration.toSecondsPart()
                    );

                    UIUpdateManager.getTimeUpdater().accept(formattedDuration);

                    if (duration.isZero()) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                                null,
                                "The auction has closed.",
                                "INFO",
                                JOptionPane.INFORMATION_MESSAGE)
                        );
                    }
                }
            });
        } catch (JsonProcessingException ex) {
            logger.error("Error desserializing json", ex);
        }
    }

    public void auctionEnded(String message) {
        try {
            if (!message.trim().startsWith("{")) {
                logger.info("Message ignored:" + message);
                return;
            }

            Response response = mapper.readValue(message, Response.class);
            if (response.getMessage().equals("Auction closed")) {
                response.getData().ifPresent(data -> {
                    Object itemObject = data.get("finalItem");
                    Item finalItem = mapper.convertValue(itemObject, Item.class);
                    Item currentItem = ServerAuctionApp.frame.getAppController().getItemController().findById(finalItem.getId());

                    String messageToSend = "";
                    if (finalItem.getCurrentBid() < finalItem.getData().getReservePrice()) {
                        messageToSend = "The bid did not reach the requested amount";
                        currentItem.setStatus(ItemStatus.CANCELLED);
                    } else {
                        messageToSend = "Congratulations you made the winning bid";
                        currentItem.setWinningBidder(finalItem.getWinningBidder());
                        currentItem.setStatus(ItemStatus.COMPLETED);
                        
                        boolean updated = ServerAuctionApp.frame.getAppController().getItemController().updateItem(currentItem);
                        if (updated) {
                            logger.info("Item updated successfully");
                        } else {
                            logger.warn("Failed to update item");
                        }

                    }

                    Response auctionEnded = new Response("AUCTION-ENDED", messageToSend);
                    auctionEnded.addData("bidderId", finalItem.getWinningBidder());
                    ServerAuctionApp.frame.getAppController().getMulticastController().send(auctionEnded);

                    ServerAuctionApp.frame.getAuction().setStatus(AuctionStatus.WAITING);
                    ServerAuctionApp.frame.getAuction().setCurrentAuctionItem(null);

                    Frame.pnProducts = new PnProducts();
                    ServerAuctionApp.frame.initNewPanel(Frame.pnProducts);

                });
            }
        } catch (JsonProcessingException ex) {
            logger.error("Error processing auction message.", ex);
        }
    }
}