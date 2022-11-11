package ru.nsu.gorin.networks.lab3.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import javafx.application.Platform;
import ru.nsu.gorin.networks.lab3.utils.Context;
import ru.nsu.gorin.networks.lab3.utils.ResultButton;

import java.io.IOException;

public class GetLocationService implements Runnable {
    public static final String HITS = "hits";
    private final Context context;

    private static final String API_KEY = "373223bc-1d18-4473-95f2-959326698116";

    private final String location;

    public GetLocationService(Context context, String location) {
        this.context = context;
        this.location = location;
    }

    public void getLocation(String location) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graphhopper.com/api/1/geocode?q=" + location + "&key=" + API_KEY )
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            JsonObject jobj = new Gson().fromJson(response.body().string(), JsonObject.class);
            JsonArray hits = jobj.getAsJsonArray(HITS);
            this.addResultsOnScreen(hits);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addResultsOnScreen(JsonArray hits) {
        Platform.runLater(() -> {
            this.context.getResultsVBox().getChildren().clear();
            for (JsonElement element : hits) {
                JsonObject object = (JsonObject)element;

                double lat = Double.parseDouble(String.valueOf(object.getAsJsonObject("point").get("lat")).replaceAll("\"",""));
                double lng = Double.parseDouble(String.valueOf(object.getAsJsonObject("point").get("lng")).replaceAll("\"",""));

                String name = String.valueOf(object.get("name")).replaceAll("\"","");
                String osm_value = String.valueOf(object.get("osm_value")).replaceAll("\"","");
                String country = String.valueOf(object.get("country")).replaceAll("\"","");
                String city = String.valueOf(object.get("city")).replaceAll("\"","");
                String buttonName = "name :" + name + ", osm value: " + osm_value + "\n country: " + country + ", city: " + city;

                this.context.getResultsVBox().getChildren().add(new ResultButton(buttonName, this.context, lat, lng));
            }
        });
    }

    @Override
    public void run() {
        this.getLocation(this.location);
    }
}
