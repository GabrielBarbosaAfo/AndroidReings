package br.edu.ifsudestemg.throne.ai;

import android.content.Context;

import androidx.annotation.NonNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import br.edu.ifsudestemg.throne.model.CardEvent;
import br.edu.ifsudestemg.throne.model.GameContext;
import br.edu.ifsudestemg.throne.model.GameState;
import br.edu.ifsudestemg.throne.utils.Constants;
import br.edu.ifsudestemg.throne.utils.SecurePrefs;

public class AICardGenerator {

    private final Context context;

    public AICardGenerator(Context context) {
        this.context = context;
    }

    public CardEvent generateCard(@NonNull GameContext gameContext,
                                  @NonNull GameState gameState,
                                  @NonNull String lastChoice) throws IOException, JSONException {

        String apiKey = getApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException(Constants.ERROR_API_KEY);
        }

        String prompt = PromptBuilder.buildCardPrompt(gameContext, gameState, lastChoice);

        GeminiAIClient client = new GeminiAIClient(apiKey);
        String rawResponse = client.generateText(prompt);

        if (rawResponse == null || rawResponse.isBlank()) {
            throw new IOException(Constants.ANWER_AI_EMPTY);
        }

        String clean = rawResponse.replace("```json", "")
                .replace("```", "")
                .trim();

        JSONObject obj = new JSONObject(clean);

        String title = obj.getString("title");
        String description = obj.getString("description");
        JSONObject yes = obj.getJSONObject("effects").getJSONObject("yes");
        JSONObject no = obj.getJSONObject("effects").getJSONObject("no");

        return new CardEvent(
                title,
                description,
                yes.getInt("people"),
                yes.getInt("army"),
                yes.getInt("wealth"),
                yes.getInt("faith"),
                no.getInt("people"),
                no.getInt("army"),
                no.getInt("wealth"),
                no.getInt("faith")
        );
    }

    private String getApiKey() {
        try {
            return new SecurePrefs(context).getApiKey();
        } catch (Exception e) {
            return null;
        }
    }

}
