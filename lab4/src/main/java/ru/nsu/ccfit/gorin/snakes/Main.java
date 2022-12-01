package ru.nsu.ccfit.gorin.snakes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nsu.ccfit.gorin.snakes.config.Config;
import ru.nsu.ccfit.gorin.snakes.config.ConfigReader;
import ru.nsu.ccfit.gorin.snakes.presenter.MoveHandler;
import ru.nsu.ccfit.gorin.snakes.gui.controllers.GameViewController;
import ru.nsu.ccfit.gorin.snakes.net.MainNodeHandler;
import ru.nsu.ccfit.gorin.snakes.net.node.Role;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String GAME_VIEW_FXML_PATH = "GameView.fxml";
    private static final String MULTICAST_HOST = "239.192.0.4";
    private static final int MULTICAST_PORT = 9291;
    private static int port = 0;
    private static String configPath;

    public static void main(String[] args) {
        try {
            port = Integer.parseInt(args[0]);
            configPath = args[1];
        } catch (NumberFormatException e) {
            logger.error("Cant parse port={}", args[0], e);
            return;
        }
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Config config = ConfigReader.readConfig(configPath);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getClassLoader().getResource(GAME_VIEW_FXML_PATH));
            SplitPane root = loader.load();
            GameViewController controller = loader.getController();
            MoveHandler moveHandler = new MoveHandler(
                    config,
                    new MainNodeHandler(Role.MASTER, config, port, InetAddress.getByName(MULTICAST_HOST), MULTICAST_PORT),
                    controller
            );
            controller.setStage(stage);
            controller.setGamePresenter(moveHandler);
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.show();
        } catch (UnknownHostException e) {
            logger.error("Cant get multicast address by host={}", MULTICAST_HOST);
        } catch (IOException e) {
            logger.error("Problem with load FXML at path={}", GAME_VIEW_FXML_PATH, e);
        }
    }
}
