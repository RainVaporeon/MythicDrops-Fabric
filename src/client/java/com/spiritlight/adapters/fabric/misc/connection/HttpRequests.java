package com.spiritlight.adapters.fabric.misc.connection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.Objects;

public class HttpRequests {
    static final OkHttpClient client = new OkHttpClient();

    public static String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        } catch (NullPointerException | IOException ex) {
            LogManager.getLogger("MythicDrops/Connection")
                    .error("Failed to retrieve data from " + url + ": ", ex);
            return ex.toString();
        }
    }
}
