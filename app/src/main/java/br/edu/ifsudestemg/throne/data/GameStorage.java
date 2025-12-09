package br.edu.ifsudestemg.throne.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifsudestemg.throne.model.CardEvent;
import br.edu.ifsudestemg.throne.model.GameContext;
import br.edu.ifsudestemg.throne.model.GameState;

public class GameStorage {

    private static final String PREF_NAME = "game_storage";

    private static final String KEY_CONTEXT = "game_context";
    private static final String KEY_STATE = "game_state";
    private static final String KEY_BUFFER = "game_buffer";
    private static final String KEY_CURRENT_INDEX = "current_index";

    private final SharedPreferences prefs;
    private final Gson gson = new Gson();

    public GameStorage(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveContext(GameContext ctx) {
        prefs.edit().putString(KEY_CONTEXT, gson.toJson(ctx)).apply();
    }

    public GameContext loadContext() {
        String json = prefs.getString(KEY_CONTEXT, null);
        if (json == null) return null;

        try {
            return gson.fromJson(json, GameContext.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public void saveState(GameState state) {
        prefs.edit().putString(KEY_STATE, gson.toJson(state)).apply();
    }

    public GameState loadState() {
        String json = prefs.getString(KEY_STATE, null);
        if (json == null) return null;

        try {
            GameState state = gson.fromJson(json, GameState.class);

            if (state == null) return new GameState();
            return state;

        } catch (JsonSyntaxException e) {
            return new GameState();
        }
    }

    public void saveBuffer(List<CardEvent> buffer) {
        prefs.edit().putString(KEY_BUFFER, gson.toJson(buffer)).apply();
    }

    public List<CardEvent> loadBuffer() {
        String json = prefs.getString(KEY_BUFFER, null);
        if (json == null) return new ArrayList<>();

        try {
            Type type = new TypeToken<List<CardEvent>>(){}.getType();
            List<CardEvent> list = gson.fromJson(json, type);

            return (list != null) ? list : new ArrayList<>();

        } catch (JsonSyntaxException e) {
            return new ArrayList<>();
        }
    }

    public void saveCurrentIndex(int index) {
        prefs.edit().putInt(KEY_CURRENT_INDEX, index).apply();
    }

    public int loadCurrentIndex() {
        return prefs.getInt(KEY_CURRENT_INDEX, 0);
    }

    public void reset() {
        prefs.edit().clear().apply();
    }
}
