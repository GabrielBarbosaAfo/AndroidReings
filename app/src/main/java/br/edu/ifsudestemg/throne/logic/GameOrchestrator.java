// GameOrchestrator.java
package br.edu.ifsudestemg.throne.logic;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.data.GameStorage;
import br.edu.ifsudestemg.throne.model.CardData;
import br.edu.ifsudestemg.throne.model.GameContext;
import br.edu.ifsudestemg.throne.model.GameState;

public class GameOrchestrator {

    private final String userContext;
    private final GameStorage storage;
    private GameState gameState;

    public GameOrchestrator(Context context, String userContext) {
        this.userContext = userContext;
        this.storage = new GameStorage(context);
        this.gameState = loadOrCreateGameState();
    }

    private GameState loadOrCreateGameState() {

        GameState saved = storage.loadState();
        if (saved != null) {
            return saved;
        }

        GameState newState = new GameState();
        GameContext ctx = new GameContext(userContext);
        storage.saveState(newState);
        storage.saveContext(ctx);
        return newState;
    }

    public float getPeopleRatio() {
        return gameState.getPeople() / 100f;
    }

    public float getArmyRatio() {
        return gameState.getArmy() / 100f;
    }

    public float getWealthRatio() {
        return gameState.getWealth() / 100f;
    }

    public float getFaithRatio() {
        return gameState.getFaith() / 100f;
    }

    public List<CardData> provideInitialCards() {
        List<CardData> cards = new ArrayList<>();
        cards.add(new CardData("REI HARRY", "O tesouro real foi roubado. Prender o suspeito?", R.drawable.png_bg_back_card));
        cards.add(new CardData("REI HARRY", "Um profeta prevê fome. Aumentar impostos?", R.drawable.png_bg_back_card));
        cards.add(new CardData("REI HARRY", "Um dragão aparece. Mandar o exército?", R.drawable.png_bg_back_card));
        cards.add(new CardData("REI HARRY", "Um viajante oferece tecnologia futura. Aceitar?", R.drawable.png_bg_back_card));
        cards.add(new CardData("REI HARRY", "O povo pede festa. Gastar ouro do reino?", R.drawable.png_bg_back_card));
        return cards;
    }
}