package ru.nsu.gorin.networks.lab3.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import javafx.application.Platform;
import javafx.scene.control.Label;
import ru.nsu.gorin.networks.lab3.utils.Context;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class GetDescriptionService implements Consumer<List<String>> {
    private class Place {
        private final String name;

        private final String rate;

        private final String otm;


        private Place(String name, String rate, String otm) {
            this.name = name;
            this.rate = rate;
            this.otm = otm;
        }

        public String getName() {
            return name;
        }

        public String getRate() {
            return rate;
        }

        public String getOtm() {
            return otm;
        }
    }

    private final List<Place> places = new LinkedList<>();

    private static final String API_KEY = "5ae2e3f221c38a28845f05b64e2e26237a7e7d529809cf323df46cb5";

    private final Context context;

    public GetDescriptionService(Context context) {
        this.context = context;
    }

    public void addResultsOnScreen() {
        Platform.runLater(() -> {
            this.context.getDescriptionVBox().getChildren().clear();
            for (Place place : this.places) {
                this.context.getDescriptionVBox().getChildren().add(new Label("name: " + place.getName() + ", rate: " + place.getRate() + "\n otm: " + place.getOtm()));
            }
        });
    }

    public void getDescription(List<String> xids) {
        for (String xid : xids) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.opentripmap.com/0.1/en/places/xid/" + xid + "?apikey=" + API_KEY )
                    .get()
                    .build();
            try {
                Response response = client.newCall(request).execute();
                JsonObject jobj = new Gson().fromJson(response.body().string(), JsonObject.class);
                String name = String.valueOf(jobj.get("name")).replaceAll("\"","");
                String rate = String.valueOf(jobj.get("rate")).replaceAll("\"","");
                String otm = String.valueOf(jobj.get("otm")).replaceAll("\"","");
                places.add(0, new Place(name, rate, otm));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.addResultsOnScreen();
    }

    @Override
    public void accept(List<String> xids) {
        this.getDescription(xids);
    }

    @Override
    public Consumer<List<String>> andThen(Consumer<? super List<String>> after) {
        return Consumer.super.andThen(after);
    }
}
