package ru.nsu.gorin.networks.lab3;

import javafx.fxml.FXML;
import java.net.URL;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import ru.nsu.gorin.networks.lab3.services.GetLocationService;
import ru.nsu.gorin.networks.lab3.utils.Context;

import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class MainController {
    private Context context;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox descriptionVBox;

    @FXML
    private VBox resultsVBox;

    @FXML
    private TextField userInput;

    @FXML
    private Label weatherLabel;

    @FXML
    void searchRequest(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            this.resultsVBox.getChildren().clear();
            this.weatherLabel.setText("");
            this.descriptionVBox.getChildren().clear();
            this.resultsVBox.getChildren().add(new Label("Searching......"));
            CompletableFuture<Void> makeRequest =
                    CompletableFuture.runAsync(new GetLocationService(this.context, this.userInput.getText()));
        }
    }

    @FXML
    void initialize() {
        assert this.descriptionVBox != null : "fx:id=\"descriptionVBox\" was not injected: check your FXML file 'main-view.fxml'.";
        assert this.resultsVBox != null : "fx:id=\"resultsVBox\" was not injected: check your FXML file 'main-view.fxml'.";
        assert this.userInput != null : "fx:id=\"userInput\" was not injected: check your FXML file 'main-view.fxml'.";
        assert this.weatherLabel != null : "fx:id=\"weatherLabel\" was not injected: check your FXML file 'main-view.fxml'.";

        this.context = new Context(this.descriptionVBox, this.resultsVBox, this.weatherLabel);

    }
}
