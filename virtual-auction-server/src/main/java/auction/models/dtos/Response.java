package auction.models.dtos;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Response {

    private String status;
    private String message;
    private Map<String, Object> data;

    public Response() {
        this(null, null, null);
    }

    public Response(String status, String message) {
        this(status, message, null);
    }

    public Response(String status, String message, Map<String, Object> data) {
        this.status = status;
        this.message = message;
        this.data = (data == null || data.isEmpty()) ? null : data; // Evita objetos vazios
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // Retorna um Optional para indicar que "data" pode estar ausente
    public Optional<Map<String, Object>> getData() {
        return Optional.ofNullable(data);
    }

    public void setData(Map<String, Object> data) {
        this.data = (data == null || data.isEmpty()) ? null : data;
    }

    public void addData(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, value);
    }
}