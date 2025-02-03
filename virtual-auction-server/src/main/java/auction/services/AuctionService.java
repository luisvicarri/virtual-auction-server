package auction.services;

import auction.controllers.BiddingController;
import auction.enums.AuctionStatus;
import auction.main.ServerAuctionApp;
import auction.models.Bid;
import auction.models.Item;
import auction.models.dtos.Response;
import auction.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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

            JsonUtil.printFormattedJson(message);

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
            
            Item currentItem = ServerAuctionApp.frame.getAuction().getCurrentAuctionItem();
            Response response = new Response("AUCTION-INFO", "Auction in progress");
            response.addData("item", currentItem);

            BiddingController biddingController = ServerAuctionApp.frame.getAppController().getBiddingController();
            List<Bid> bids = biddingController.getBidsForItem(currentItem.getId());
            response.addData("bids", bids);

            ServerAuctionApp.frame.getAppController().getMulticastController().send(response);
        }

        try {
            JsonNode jsonNode = mapper.readTree(message);
            logger.info("New client connected: {}", jsonNode);
        } catch (JsonProcessingException e) {
            logger.error("Error processing CLIENT-CONNECTED message: {}", message, e);
        }
    }
}
