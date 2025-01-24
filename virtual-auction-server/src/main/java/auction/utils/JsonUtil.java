package auction.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtil {
    
    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());     // Suporte a Optional
        objectMapper.registerModule(new JavaTimeModule()); // Suporte a Duration e outros tipos do java.time
        return objectMapper;
    }
    
}
