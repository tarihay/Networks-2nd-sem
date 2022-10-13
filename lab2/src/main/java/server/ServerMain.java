package server;


public class ServerMain {
    private static final int port = 8888;

    public static void main(String[] args) {
        Server server = new Server(port, 10);
        server.run();
    }
}
