package br.edu.ifsudestemg.throne.ai;

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

        String modelId = "gemini-2.5-flash";
        String apiUrl = "https://generativelanguage.googleapis.com/v1/models/"
                + modelId + ":generateContent?key=" + apiKey;

        JSONObject json = new JSONObject();
        JSONArray contents = new JSONArray();
        JSONObject part = new JSONObject();
        part.put("text", prompt);

        JSONObject content = new JSONObject();
        content.put("parts", new JSONArray().put(part));
        contents.put(content);

        json.put("contents", contents);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Erro na API Gemini: " + response.code());
        }

        assert response.body() != null;
        String responseBody = response.body().string();

        JSONObject responseJson = new JSONObject(responseBody);
        JSONArray candidates = responseJson.getJSONArray("candidates");
        JSONObject firstCandidate = candidates.getJSONObject(0);
        JSONObject contentResponse = firstCandidate.getJSONObject("content");
        JSONArray parts = contentResponse.getJSONArray("parts");

        return parts.getJSONObject(0).getString("text");
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
