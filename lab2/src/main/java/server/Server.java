package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

public class Server implements Runnable {
    private static final Logger logger = LogManager.getLogger(Server.class);

    private final int port;
    private final int backlog;

    public Server(int port, int backlog){
        this.port = port;
        this.backlog = backlog;
    }


    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.port, this.backlog)){
            logger.info("Server start on port - " + this.port);
            while (true){
                Socket socket = serverSocket.accept();
                logger.info("Client connected - " + socket.getInetAddress() + ":" + socket.getPort());
                Thread clientHandler = new Thread(new ClientHandler(socket));
                clientHandler.start();
            }
        } catch (IOException e) {
            logger.error("Cant open server socket on port: " + this.port);
        }
    }
}
