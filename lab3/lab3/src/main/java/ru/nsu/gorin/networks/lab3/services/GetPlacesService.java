package ru.nsu.gorin.networks.lab3.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class GetPlacesService implements Supplier<List<String>> {
    private static final String API_KEY = "5ae2e3f221c38a28845f05b64e2e26237a7e7d529809cf323df46cb5";
    private static final int RADIUS = 1000;
    public static final String FEATURES = "features";

    private final double lat;

    private final double lng;

    private final List<String> interestingPlaceIds = new LinkedList<>();

    public GetPlacesService(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public void getInterestingPlaces(double lat, double lng) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.opentripmap.com/0.1/en/places/radius?radius=" + RADIUS + "&lon=" + lng + "&lat=" + lat + "&apikey=" + API_KEY)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            JsonObject jobj = new Gson().fromJson(response.body().string(), JsonObject.class);
            JsonArray features = jobj.getAsJsonArray(FEATURES);

            for (JsonElement element : features) {
                JsonObject object = (JsonObject)element;
                JsonObject properties = (JsonObject) object.get("properties");
                String xid = String.valueOf(properties.get("xid")).replaceAll("\"","");
                interestingPlaceIds.add(0,xid);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> get() {
        getInterestingPlaces(this.lat, this.lng);
        return interestingPlaceIds;
    }
}
