package ru.nsu.gorin.networks.lab3.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import javafx.application.Platform;
import ru.nsu.gorin.networks.lab3.utils.Context;

import java.io.IOException;

public class GetWeatherService implements Runnable {
    private static final String API_KEY = "bfb6a5a7d1b837b924521f636adffdc2";

    private final double lat;

    private final double lng;

    private final Context context;

    public GetWeatherService(double lat, double lng, Context context) {
        this.lat = lat;
        this.lng = lng;
        this.context = context;
    }

    private void addResultsOnScreen(JsonObject jobj) {
        JsonObject weather = (JsonObject) jobj.getAsJsonArray("weather").get(0);
        JsonObject main = (JsonObject) jobj.get("main");

        String description = String.valueOf(weather.get("description")).replaceAll("\"","");
        String temp = String.valueOf(main.get("temp")).replaceAll("\"","");
        String feelsLike = String.valueOf(main.get("feels_like")).replaceAll("\"","");

        Platform.runLater(() -> {
            this.context.getWeatherLabel().setText("Description: " + description + "\nTemp: " + temp + "\nFeels like: " + feelsLike);
        });
    }

    public void getWeather(double lat, double lng) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lng + "&appid=" + API_KEY + "&units=metric")
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            JsonObject jobj = new Gson().fromJson(response.body().string(), JsonObject.class);
            this.addResultsOnScreen(jobj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run() {
        getWeather(this.lat, this.lng);
    }
}
