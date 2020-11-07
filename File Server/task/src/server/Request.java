package server;

public class Request {
    String method;
    String name;
    String data;
    public Request (String method, String name, String data) {
        this.method = method;
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public String getMethod() {
        return method;
    }

    public String getData() {
        return data;
    }
}
