package br.edu.ifsudestemg.throne.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import br.edu.ifsudestemg.throne.model.GameProgress;
import br.edu.ifsudestemg.throne.model.NarrativeTree;
import br.edu.ifsudestemg.throne.narrative.KingdomState;

public class GameStorage {

    private static final String PREF = "throne_game_v2";

    private static final String KEY_USER_CONTEXT = "user_context";

    private static final String KEY_KINGDOM_STATE = "kingdom_state";

    private static final String KEY_GAME_PROGRESS = "game_progress";

    private static final String KEY_CURRENT_TREE = "current_tree";

    private static final String KEY_NEXT_TREE = "next_tree";

    private static final String KEY_PREVIOUS_TWIST = "previous_twist";

    private static final String KEY_REIGN_YEARS = "reign_years";

    private final SharedPreferences prefs;

    private final Gson gson;

    public GameStorage(Context ctx) {
        this.prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void saveUserContext(String context) {
        prefs.edit().putString(KEY_USER_CONTEXT, context).apply();
    }

    public String loadUserContext() {
        return prefs.getString(KEY_USER_CONTEXT, null);
    }

    public void saveKingdomState(KingdomState state) {
        prefs.edit().putString(KEY_KINGDOM_STATE, gson.toJson(state)).apply();
    }

    public KingdomState loadKingdomState() {
        String json = prefs.getString(KEY_KINGDOM_STATE, null);
        if (json == null) {
            return new KingdomState(50, 50, 50, 50);
        }
        try {
            KingdomStateProxy p = gson.fromJson(json, KingdomStateProxy.class);
            return new KingdomState(p.wealth, p.people, p.army, p.faith);
        } catch (Exception e) {
            return new KingdomState(50, 50, 50, 50);
        }
    }

    public void saveProgress(GameProgress progress) {
        prefs.edit().putString(KEY_GAME_PROGRESS, gson.toJson(progress)).apply();
    }

    public GameProgress loadProgress() {
        String json = prefs.getString(KEY_GAME_PROGRESS, null);
        if (json == null) return new GameProgress();
        try {
            return gson.fromJson(json, GameProgress.class);
        } catch (Exception e) {
            return new GameProgress();
        }
    }

    public void saveTree(NarrativeTree tree) {
        prefs.edit().putString(KEY_CURRENT_TREE, gson.toJson(tree)).apply();
    }

    public NarrativeTree loadTree() {
        String json = prefs.getString(KEY_CURRENT_TREE, null);
        if (json == null) return null;
        try {
            return gson.fromJson(json, NarrativeTree.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void saveNextTree(NarrativeTree tree) {
        if (tree != null) {
            prefs.edit().putString(KEY_NEXT_TREE, gson.toJson(tree)).apply();
        } else {
            prefs.edit().remove(KEY_NEXT_TREE).apply();
        }
    }

    public NarrativeTree loadNextTree() {
        String json = prefs.getString(KEY_NEXT_TREE, null);
        if (json == null) return null;
        try {
            return gson.fromJson(json, NarrativeTree.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void savePreviousTwist(String twist) {
        prefs.edit().putString(KEY_PREVIOUS_TWIST, twist).apply();
    }

    public String loadPreviousTwist() {
        return prefs.getString(KEY_PREVIOUS_TWIST, null);
    }

    public boolean hasActiveGame() {
        return loadUserContext() != null && loadTree() != null;
    }

    public void saveReignYears(int years) {
        prefs.edit().putInt(KEY_REIGN_YEARS, years).apply();
    }

    public int loadReignYears() {
        return prefs.getInt(KEY_REIGN_YEARS, 0);
    }

    public void clear() {
        prefs.edit().clear().apply();
    }

    private static class KingdomStateProxy {
        int wealth, people, army, faith;
    }
}