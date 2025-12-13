package br.edu.ifsudestemg.throne.narrative;

import android.util.Log;
import androidx.annotation.NonNull;
import br.edu.ifsudestemg.throne.data.GameStorage;
import br.edu.ifsudestemg.throne.model.GameProgress;
import br.edu.ifsudestemg.throne.model.NarrativeTree;
import br.edu.ifsudestemg.throne.ia.GeminiHelper;
import com.google.gson.Gson;

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
            String response = GeminiHelper.askGemini(prompt, apiKey);
            NarrativeTree tree = new Gson().fromJson(response, NarrativeTree.class);

            if (tree == null || tree.getRoot() == null)
                return false;

            storage.saveTree(tree);
            storage.saveUserContext(ctx.getUserContext());
            storage.saveKingdomState(ctx.getKingdomState());
            storage.saveProgress(new GameProgress());

            return true;
        } catch (IOException e) {
            Log.e(TAG, "Erro de rede", e);
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Erro ao gerar árvore", e);
            return false;
        }
    }
}