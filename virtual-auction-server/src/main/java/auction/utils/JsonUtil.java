package auction.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    
    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());     // Suporte a Optional
        objectMapper.registerModule(new JavaTimeModule()); // Suporte a Duration e outros tipos do java.time
        return objectMapper;
    }
    
    public static void printFormattedJson(String message) {
        try {
            Object jsonObject = JsonUtil.getObjectMapper().readValue(message, Object.class);
            String prettyJson = JsonUtil.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
            System.out.println("");
            logger.debug(prettyJson);
            System.out.println("");
        } catch (JsonProcessingException ex) {
            logger.error("Error converting json to object", ex);
        }
    }
    
}
