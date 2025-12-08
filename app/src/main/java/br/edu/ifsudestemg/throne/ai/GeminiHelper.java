package br.edu.ifsudestemg.throne.ai;

import androidx.annotation.NonNull;

import org.json.JSONException;

import java.io.IOException;

public class GeminiHelper {

    public static String askGemini(@NonNull String prompt, @NonNull String apiKey) throws IOException, JSONException {
        GeminiAIClient client = new GeminiAIClient(apiKey);
        return client.generateText(prompt);
    }
}
