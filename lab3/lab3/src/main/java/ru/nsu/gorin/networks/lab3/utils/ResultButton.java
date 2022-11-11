package ru.nsu.gorin.networks.lab3.utils;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ru.nsu.gorin.networks.lab3.services.GetDescriptionService;
import ru.nsu.gorin.networks.lab3.services.GetPlacesService;
import ru.nsu.gorin.networks.lab3.services.GetWeatherService;

import java.util.concurrent.CompletableFuture;

public class ResultButton extends Button {
    private final double lat;

    private final double lng;

    private final Context context;

    public ResultButton(String text, Context context, double lat, double lng) {
        super(text);

        this.context = context;
        this.lat = lat;
        this.lng = lng;

        this.setPrefSize(280,60);

        this.setOnAction(actionEvent -> {
            Platform.runLater(() -> {
                context.getWeatherLabel().setText("Searching....");
                context.getDescriptionVBox().getChildren().clear();
                context.getDescriptionVBox().getChildren().add(new Label("Searching...."));
            });
            CompletableFuture<Void> makeWeatherRequest = CompletableFuture.runAsync(new GetWeatherService(ResultButton.this.lat, ResultButton.this.lng, ResultButton.this.context));

            CompletableFuture<Void> makeDescriptionRequest = CompletableFuture
                    .supplyAsync(new GetPlacesService(ResultButton.this.lat, ResultButton.this.lng))
                    .thenAccept(new GetDescriptionService(this.context));
        });
    }
}
