package br.edu.ifsudestemg.throne.ia;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeminiAIClient {

    private final String apiKey;
    private final OkHttpClient client;

    public GeminiAIClient(@NonNull String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
    }

    public String generateText(@NonNull String prompt) throws IOException, JSONException {

        String modelId = "gemini-2.0-flash";
        String apiUrl = "https://generativelanguage.googleapis.com/v1/models/"
                + modelId + ":generateContent?key=" + apiKey;

        JSONObject requestBodyJson = buildRequestJson(prompt);

        RequestBody body = RequestBody.create(
                requestBodyJson.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException("Error na API Gemini: " + response.code() + " " + response.message());
            }

            String responseStr = response.body() != null ? response.body().string() : "";
            JSONObject json = new JSONObject(responseStr);

            JSONArray candidates = json.getJSONArray("candidates");
            JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");

            return parts.getJSONObject(0).getString("text");
        }
    }

    @NonNull
    private JSONObject buildRequestJson(String prompt) throws JSONException {
        JSONObject textPart = new JSONObject();
        textPart.put("text", prompt);

        JSONArray partsArray = new JSONArray();
        partsArray.put(textPart);

        JSONObject contentObj = new JSONObject();
        contentObj.put("parts", partsArray);

        JSONArray contentsArray = new JSONArray();
        contentsArray.put(contentObj);

        JSONObject generationConfig = new JSONObject();
        generationConfig.put("temperature", 0.5);
        generationConfig.put("maxOutputTokens", 600);

        JSONObject requestBodyJson = new JSONObject();
        requestBodyJson.put("contents", contentsArray);
        requestBodyJson.put("generationConfig", generationConfig);

        return requestBodyJson;
    }
}
