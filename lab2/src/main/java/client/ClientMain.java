package client;

import exceptions.FileNotFoundException;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientMain {
    private static final String fileName = "src/main/resources/data.txt";
    private static final int port = 8888;

    public static void main(String[] args) throws UnknownHostException, FileNotFoundException {
        Client client = new Client(fileName, InetAddress.getLocalHost(), port);
        client.run();
    }
}
