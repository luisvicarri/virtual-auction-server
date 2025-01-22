package auction.models.dtos;

public class Request {
    
    private String action;
    private String name;
    private String password;

    public Request() {
    }

    public Request(String action, String name, String password) {
        this.action = action;
        this.name = name;
        this.password = password;
    }
    
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}