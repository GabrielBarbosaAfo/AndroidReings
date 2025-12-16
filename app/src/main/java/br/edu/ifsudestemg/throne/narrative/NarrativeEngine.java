package br.edu.ifsudestemg.throne.narrative;

import android.util.Log;
import androidx.annotation.NonNull;
import br.edu.ifsudestemg.throne.data.GameStorage;
import br.edu.ifsudestemg.throne.model.GameProgress;
import br.edu.ifsudestemg.throne.model.NarrativeTree;
import br.edu.ifsudestemg.throne.ia.GeminiHelper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

public class NarrativeEngine {

    private static final String TAG = "NarrativeEngine";
    private final GameStorage storage;
    private final String apiKey;

    public NarrativeEngine(@NonNull GameStorage storage, @NonNull String apiKey) {
        this.storage = storage;
        this.apiKey = apiKey;
    }

    public boolean generateTree(@NonNull NarrativeGenerationContext ctx) {
        try {
            String prompt = PromptBuilder.buildPrompt(ctx);
            String rawResponse = GeminiHelper.askGemini(prompt, apiKey);

            String json = extractJsonFromResponse(rawResponse);
            if (json == null) {
                Log.e(TAG, "Nenhum JSON válido encontrado na resposta da IA");
                return false;
            }

            Log.d(TAG, "JSON extraído:\n" + json);

            NarrativeTree tree;
            try {
                tree = new Gson().fromJson(json, NarrativeTree.class);
            } catch (JsonSyntaxException e) {
                Log.e(TAG, "JSON inválido mesmo após extração", e);
                return false;
            }

            if (tree == null || tree.getRoot() == null) {
                Log.e(TAG, "Árvore nula ou raiz ausente");
                return false;
            }

            storage.saveTree(tree);
            storage.saveUserContext(ctx.getUserContext());
            storage.saveKingdomState(ctx.getKingdomState());
            storage.saveProgress(new GameProgress());

            Log.i(TAG, "Árvore narrativa gerada com sucesso!");
            return true;

        } catch (IOException e) {
            Log.e(TAG, "Erro de rede ao chamar IA", e);
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Erro inesperado em generateTree", e);
            return false;
        }
    }

    private String extractJsonFromResponse(String input) {
        if (input == null) return null;

        String trimmed = input.trim();

        if (trimmed.startsWith("{")) {
            int lastBrace = trimmed.lastIndexOf('}');
            if (lastBrace > 0) {
                return trimmed.substring(0, lastBrace + 1);
            }
        }

        int firstBrace = trimmed.indexOf('{');
        int lastBrace = trimmed.lastIndexOf('}');

        if (firstBrace != -1 && lastBrace != -1 && lastBrace > firstBrace) {
            return trimmed.substring(firstBrace, lastBrace + 1);
        }

        return null;
    }
}