package client;

import common.Response;
import exceptions.FileNotFoundException;
import exceptions.UnknownResponseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

public class Client implements Runnable {
    private static final Logger logger = LogManager.getLogger(Client.class);

    private static final int BUFFER_SIZE = 1024;

    private final Path path;
    private final InetAddress serverAddress;
    private final int serverPort;

    public Client(String path, InetAddress serverAddress, int serverPort) throws FileNotFoundException {
        this.path = getFilePath(path);
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    private Path getFilePath(String pathStr) throws FileNotFoundException {
        Path tmpPath = Paths.get(pathStr);
        if (!Files.exists(tmpPath)){
            logger.error(pathStr + " not found");
            throw new FileNotFoundException(pathStr + " not found");
        }
        return tmpPath;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(this.serverAddress, this.serverPort);
             InputStream fileReader = Files.newInputStream(this.path);
             DataOutputStream socketWriter = new DataOutputStream(socket.getOutputStream());
             DataInputStream socketReader = new DataInputStream(socket.getInputStream())){

            long fileSize = Files.size(this.path);
            String fileName = this.path.getFileName().toString();
            socketWriter.writeInt(fileName.length());
            socketWriter.writeUTF(fileName);
            Response filenameTransferResponse = Response.getResponseByCode(socketReader.readInt());

            if (filenameTransferResponse == Response.FAILURE_FILENAME_TRANSFER){
                logger.error("Error with file occurred");
                return;
            }

            socketWriter.writeLong(fileSize);
            socketWriter.flush();
            byte[] buffer = new byte[BUFFER_SIZE];
            int lineSize;
            MessageDigest hashSum = MessageDigest.getInstance("MD5");

            while ((lineSize = fileReader.read(buffer, 0, BUFFER_SIZE)) > 0){
                socketWriter.write(buffer, 0, lineSize);
                socketWriter.flush();
                hashSum.update(buffer, 0, lineSize);
            }
            socketWriter.writeUTF(hashSumToString(hashSum));
            socketWriter.flush();
            Response fileTransferResponse = Response.getResponseByCode(socketReader.readInt());
            if (fileTransferResponse == Response.FAILURE_FILE_TRANSFER){
                logger.info(Level.SEVERE +  " Failure file transfer");
            }

        } catch (IOException | NoSuchAlgorithmException e) {
            logger.error(e);
        } catch (UnknownResponseException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    private String hashSumToString(MessageDigest md){
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
