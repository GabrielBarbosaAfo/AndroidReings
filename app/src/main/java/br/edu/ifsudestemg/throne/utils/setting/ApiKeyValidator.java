package br.edu.ifsudestemg.throne.utils.setting;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiKeyValidator {

    public interface Callback {
        void onResult(boolean valid, String message);
    }

    private final OkHttpClient client;
    private final Executor executor;
    private final Handler mainHandler;

    public ApiKeyValidator() {
        client = new OkHttpClient();
        executor = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void validateKey(final String apiKey, final Callback callback) {

        executor.execute(() -> {
            Response response = null;

            try {
                String testUrl = "https://generativelanguage.googleapis.com/v1/models/gemini-2.0-flash?key=" + apiKey;

                Request request = new Request.Builder()
                        .url(testUrl)
                        .get()
                        .build();

                response = client.newCall(request).execute();

                boolean ok = response.isSuccessful();

                String msg = "HTTP " + response.code() + " - " + response.message();
                if (response.body() != null) {
                    String body = response.body().string();
                    if (body.length() > 300) body = body.substring(0, 300) + "...";
                    msg += " | " + body;
                }

                String finalMsg = msg;

                mainHandler.post(() -> callback.onResult(ok, finalMsg));

            } catch (Exception e) {
                mainHandler.post(() -> callback.onResult(false, e.getMessage()));
            } finally {
                if (response != null) response.close();
            }

        });
    }
}
