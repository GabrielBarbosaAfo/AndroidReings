package br.edu.ifsudestemg.throne.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import br.edu.ifsudestemg.throne.model.GameProgress;
import br.edu.ifsudestemg.throne.model.NarrativeTree;
import br.edu.ifsudestemg.throne.narrative.KingdomState;

public class GameStorage {

    private static final String PREF = "throne_game_v1";

    private final SharedPreferences prefs;

    private final Gson gson;

    public GameStorage(Context ctx) {
        this.prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void saveUserContext(String ctx) {
        prefs.edit().putString("ctx", ctx).apply();
    }

    public String loadUserContext() { return prefs.getString("ctx", null); }

    public void saveKingdomState(KingdomState state) {
        prefs.edit().putString("state", gson.toJson(state)).apply();
    }

    public KingdomState loadKingdomState() {

        String json = prefs.getString("state", null);

        if (json == null)
            return new KingdomState(50, 50, 50, 50);

        try {
            KingdomStateProxy p = gson.fromJson(json, KingdomStateProxy.class);
            return new KingdomState(p.wealth, p.people, p.army, p.faith);
        } catch (Exception e) {
            return new KingdomState(50, 50, 50, 50);
        }
    }

    public void saveProgress(GameProgress p) {
        prefs.edit().putString("prog", gson.toJson(p)).apply();
    }

    public GameProgress loadProgress() {
        String json = prefs.getString("prog", null);

        if (json == null)
            return new GameProgress();

        try {
            return gson.fromJson(json, GameProgress.class);
        } catch (Exception e) {
            return new GameProgress();
        }
    }

    public void saveTree(NarrativeTree tree) {
        prefs.edit().putString("tree", gson.toJson(tree)).apply();
    }

    public NarrativeTree loadTree() {
        String json = prefs.getString("tree", null);
        if (json == null) return null;
        try {
            return gson.fromJson(json, NarrativeTree.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void savePreviousTwist(String twist) {
        prefs.edit().putString("twist", twist).apply();
    }

    public String loadPreviousTwist() {
        return prefs.getString("twist", null);
    }

    public boolean hasActiveGame() {
        return loadUserContext() != null && loadTree() != null;
    }

    public void clear() {
        prefs.edit().clear().apply();
    }

    private static class KingdomStateProxy {
        int wealth, people, army, faith;
    }
}