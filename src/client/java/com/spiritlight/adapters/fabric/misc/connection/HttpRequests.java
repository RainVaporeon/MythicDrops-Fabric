package com.spiritlight.adapters.fabric.misc.connection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class HttpRequests {
    static final OkHttpClient client = new OkHttpClient();

    public static String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (NullPointerException | IOException ex) {
            ex.printStackTrace();
            return ex.toString();
        }
    }
}
