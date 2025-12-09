package br.edu.ifsudestemg.throne.logic;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import br.edu.ifsudestemg.throne.ai.AICardGenerator;
import br.edu.ifsudestemg.throne.data.GameStorage;
import br.edu.ifsudestemg.throne.model.CardEvent;
import br.edu.ifsudestemg.throne.model.GameContext;
import br.edu.ifsudestemg.throne.model.GameState;

public class GameOrchestrator {

    private static final String TAG = "ORCH";

    private static final int MAX_BUFFER = 6;
    private static final int MIN_BUFFER = 3;

    private final GameStorage storage;
    private final AICardGenerator aiGenerator;

    private GameContext gameContext;
    private GameState gameState;

    private final ExecutorService executor;
    private final AtomicBoolean producing = new AtomicBoolean(false);

    private List<CardEvent> buffer = new ArrayList<>();
    private int currentIndex = 0;
    private Boolean lastUserChoice = null;

    public GameOrchestrator(Context context) {
        Log.d(TAG, "INIT: Carregando dados do jogo...");

        this.storage = new GameStorage(context);
        this.aiGenerator = new AICardGenerator(context);

        loadGameState();
        loadBuffer();

        executor = Executors.newSingleThreadExecutor();

        Log.d(TAG, "INIT: Concluído");
    }

    private void loadGameState() {
        Log.d(TAG, "loadGameState()");

        gameContext = storage.loadContext();
        if (gameContext == null) {
            Log.d(TAG, "Criando novo contexto");
            gameContext = new GameContext("Jogador");
            storage.saveContext(gameContext);
        }

        gameState = storage.loadState();
        if (gameState == null) {
            Log.d(TAG, "Criando novo estado");
            gameState = new GameState();
            storage.saveState(gameState);
        }
    }

    private void loadBuffer() {

        Log.d(TAG, "loadBuffer()");

        List<CardEvent> saved = storage.loadBuffer();
        int savedIdx = storage.loadCurrentIndex();

        if (saved == null) saved = new ArrayList<>();

        if (savedIdx > 0 && savedIdx <= saved.size()) {
            Log.d(TAG, "Limpando cartas consumidas — removendo " + savedIdx);
            saved = new ArrayList<>(saved.subList(savedIdx, saved.size()));
        }

        buffer = new ArrayList<>(saved);
        currentIndex = 0;

        Log.d(TAG, "Buffer carregado: size=" + buffer.size());
    }

    public void start() {
        Log.d(TAG, "start()");
        ensureBufferStock();
    }

    public synchronized CardEvent getNextCard() {
        Log.d(TAG, "getNextCard() | idx=" + currentIndex + " size=" + buffer.size());

        if (currentIndex < buffer.size()) {
            CardEvent ev = buffer.get(currentIndex);
            currentIndex++;

            storage.saveCurrentIndex(currentIndex);
            ensureBufferStock();

            return ev;
        }

        ensureBufferStock();
        return placeholderCard();
    }

    public void recordUserChoice(boolean choseYes) {
        Log.d(TAG, "recordUserChoice(" + choseYes + ")");

        if (currentIndex == 0 || currentIndex - 1 >= buffer.size()) {
            Log.w(TAG, "Sem carta para aplicar escolha");
            return;
        }

        CardEvent last = buffer.get(currentIndex - 1);

        gameState.applyChoice(last, choseYes);
        storage.saveState(gameState);

        lastUserChoice = choseYes;

        ensureBufferStock();
    }

    private void ensureBufferStock() {
        Log.d(TAG, "ensureBufferStock() → available=" + getAvailableCards());

        if (producing.get()) {
            Log.d(TAG, "Produção já está ativa, ignorando");
            return;
        }

        if (getAvailableCards() >= MIN_BUFFER) {
            Log.d(TAG, "Buffer OK, não produzir agora");
            return;
        }

        startProduction();
    }

    private int getAvailableCards() {
        return buffer.size() - currentIndex;
    }

    private void startProduction() {
        Log.d(TAG, "startProduction() called — available=" + getAvailableCards() + " buffer=" + buffer.size());

        if (!producing.compareAndSet(false, true)) {
            Log.d(TAG, "startProduction aborted: already producing");
            return;
        }

        try {
            executor.submit(() -> {
                try {
                    Log.d(TAG, "*** PRODUÇÃO INICIADA ***");
                    while (getAvailableCards() < MAX_BUFFER) {
                        Log.d(TAG, "Loop produção: available=" + getAvailableCards() + " buffer=" + buffer.size());
                        produceOneCard();
                    }
                    Log.d(TAG, "*** PRODUÇÃO FINALIZADA ***");
                } catch (Throwable t) {
                    Log.e(TAG, "Erro inesperado no produtor", t);
                } finally {
                    producing.set(false);
                    Log.d(TAG, "Produção finalizada, producing=false");
                }
            });
        } catch (RejectedExecutionException rex) {
            Log.e(TAG, "Executor rejeitou submit()", rex);
            producing.set(false);
        } catch (Throwable t) {
            Log.e(TAG, "Erro ao submeter tarefa ao executor", t);
            producing.set(false);
        }
    }


    private void produceOneCard() {
        Log.d(TAG, "produceOneCard()");

        String choiceStr = lastUserChoice == null ? "nenhuma"
                : lastUserChoice ? "YES" : "NO";

        try {
            Log.d(TAG, "Chamando IA...");
            CardEvent ev = aiGenerator.generateCard(gameContext, gameState, choiceStr);
            Log.d(TAG, "IA retornou com sucesso");

            synchronized (this) {
                buffer.add(ev);
                saveCleanBuffer();
            }

        } catch (Exception e) {
            Log.e(TAG, "Erro ao gerar carta IA", e);

            CardEvent fallback = new CardEvent(
                    "Conselho Real",
                    "Os conselheiros estão indecisos no momento...",
                    0,0,0,0,0,0,0,0
            );

            synchronized (this) {
                buffer.add(fallback);
                saveCleanBuffer();
            }

            try { Thread.sleep(600); } catch (InterruptedException ignored) {}
        }
    }

    private void saveCleanBuffer() {

        // Remove cartas já consumidas
        if (currentIndex > 0) {
            List<CardEvent> remaining =
                    new ArrayList<>(buffer.subList(currentIndex, buffer.size()));

            buffer.clear();
            buffer.addAll(remaining);

            currentIndex = 0;
        }

        storage.saveBuffer(buffer);
        storage.saveCurrentIndex(currentIndex);

        Log.d(TAG, "saveCleanBuffer() → buffer=" + buffer.size() + " idx=" + currentIndex);
    }

    private CardEvent placeholderCard() {
        return new CardEvent("Gerando...", "O oráculo está pensando…",
                0,0,0,0, 0,0,0,0);
    }

    public float getPeopleRatio() { return gameState.getPeople() / 100f; }
    public float getArmyRatio()   { return gameState.getArmy() / 100f; }
    public float getWealthRatio() { return gameState.getWealth() / 100f; }
    public float getFaithRatio()  { return gameState.getFaith() / 100f; }

    public void shutdown() {
        Log.d(TAG, "shutdown()");
        executor.shutdownNow();
    }
}
