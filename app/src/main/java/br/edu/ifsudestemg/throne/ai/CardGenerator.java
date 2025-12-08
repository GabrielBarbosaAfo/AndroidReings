package br.edu.ifsudestemg.throne.ai;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsudestemg.throne.model.CardEvent;
import br.edu.ifsudestemg.throne.model.GameContext;
import br.edu.ifsudestemg.throne.model.GameState;

public class CardGenerator {

    private final GameContext context;
    private final GameState state;

    private final List<CardEvent> buffer = new ArrayList<>();
    private boolean isFetching = false;

    public CardGenerator(GameContext context, GameState state) {
        this.context = context;
        this.state = state;
    }

    public CardEvent popNext() {

        if (buffer.size() < 3 && !isFetching)
            requestMoreCards();

        if (buffer.isEmpty())
            return null;

        return buffer.remove(0);
    }

    public void requestMoreCards() {
        if (isFetching) return;
        isFetching = true;

        new Thread(() -> {
            try {
                String prompt = br.edu.ifsudestemg.throne.ai.AiPromptBuilder.build(context, state);


                buffer.add(fakeEvent());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isFetching = false;
            }
        }).start();
    }

    private CardEvent fakeEvent() {
        return new CardEvent(
                "Convocação Real",
                "O reino está inquieto. Enviar o exército?",
                +5, -2, -3, 0,
                -5, +2, 0, 0
        );
    }
}
