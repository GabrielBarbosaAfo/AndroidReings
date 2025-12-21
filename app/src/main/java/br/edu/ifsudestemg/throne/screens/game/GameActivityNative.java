package br.edu.ifsudestemg.throne.screens.game;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.data.GameStorage;
import br.edu.ifsudestemg.throne.data.SecurePrefs;
import br.edu.ifsudestemg.throne.model.CardData;
import br.edu.ifsudestemg.throne.model.GameProgress;
import br.edu.ifsudestemg.throne.model.NarrativeCard;
import br.edu.ifsudestemg.throne.model.NarrativeTree;
import br.edu.ifsudestemg.throne.narrative.KingdomState;
import br.edu.ifsudestemg.throne.narrative.NarrativeEngine;
import br.edu.ifsudestemg.throne.narrative.NarrativeGenerationContext;
import br.edu.ifsudestemg.throne.utils.animations.CardAnimator;
import br.edu.ifsudestemg.throne.utils.animations.FeedbackUtils;
import br.edu.ifsudestemg.throne.utils.animations.MusicManager;
import br.edu.ifsudestemg.throne.utils.controllers.AttributeBarController;
import br.edu.ifsudestemg.throne.utils.controllers.CardAdapter;
import br.edu.ifsudestemg.throne.utils.controllers.GameMenuController;
import br.edu.ifsudestemg.throne.utils.setting.TwistState;
import br.edu.ifsudestemg.throne.views.TwistDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class GameActivityNative extends AppCompatActivity {

    private static final String TAG = "GameActivityNative";
    private static final int MAX_DECISIONS_PER_CYCLE = 3;

    private FrameLayout animationLayer;

    private CardStackView cardStackView;

    private CardAdapter cardAdapter;

    private CardStackLayoutManager layoutManager;

    private AttributeBarController attributeController;

    private GameMenuController menuConfigController;

    private Direction lastDirection = null;

    private TextView txtReignYears;

    private GameStorage storage;

    private final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();

    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private volatile boolean isNextTreeGenerationRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_native);

        storage = new GameStorage(this);

        if (!validateUserContext())
            return;

        initComponents();
        decideGameEntry();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backgroundExecutor.shutdown();
    }

    private void decideGameEntry() {

        if (!hasAnyTree()) {
            generateInitialTreeAndStart();
            return;
        }

        startIntroAnimation();
    }

    private boolean hasAnyTree() {
        return storage.loadTree() != null || storage.loadNextTree() != null;
    }

    private boolean validateUserContext() {
        String userContext = getIntent().getStringExtra("USER_CONTEXT");
        if (userContext == null || userContext.trim().isEmpty()) {
            finish();
            return false;
        }
        return true;
    }

    private void initComponents() {
        animationLayer = findViewById(R.id.animation_layer);
        cardStackView = findViewById(R.id.card_stack_view);
        txtReignYears = findViewById(R.id.txt_reign_years);

        TextView txtPlayerName = findViewById(R.id.txt_player_name);

        String playerName = storage.getUserName();

        if (playerName == null || playerName.trim().isEmpty())
            playerName = getString(R.string.default_player_name);

        txtPlayerName.setText(playerName);

        View root = findViewById(android.R.id.content);
        menuConfigController = new GameMenuController(root);
        attributeController = new AttributeBarController(root);
        attributeController.setupIcons();

        int reignYears = storage.loadReignYears();
        updateReignDisplay(reignYears);
    }

    private void startIntroAnimation() {
        new CardAnimator(animationLayer, this, 4)
                .startAnimation(() -> {
                    attributeController.show();
                    menuConfigController.showMenu();
                    showRealGame();
                });
    }

    private void showRealGame() {

        cardStackView.setVisibility(View.VISIBLE);

        NarrativeTree tree = storage.loadTree();
        GameProgress progress = storage.loadProgress();
        KingdomState state = storage.loadKingdomState();

        if (tree == null || tree.getRoot() == null || progress == null) {
            Toast.makeText(this, "Erro: dados do jogo corrompidos.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        attributeController.updateValues(state);

        NarrativeCard card = getCardById(tree, progress.getCurrentCardId());

        if (card == null)
            card = tree.getRoot();

        List<CardData> list = new ArrayList<>();

        cardAdapter = new CardAdapter(list);
        setupCardStackListener();
        cardStackView.setAdapter(cardAdapter);

        if (storage.isInTwist()) {
            showTwistAndSwitchTree(tree);
            return;
        }

        MusicManager.play(this, R.raw.bg_game, true);
        list.add(convertToCardData(card, card.getCharacter()));
    }

    private void updateReignDisplay(int years) {
        txtReignYears.setText(
                years == 0
                        ? getString(R.string.reign_start)
                        : getString(R.string.reign_years, years)
        );
    }

    private void setupCardStackListener() {
        layoutManager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                handleCardDragging(direction, ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                handleCardSwiped(direction);
            }

            @Override
            public void onCardCanceled() {
                restoreCardTitle();
            }

            @Override
            public void onCardRewound() {
                restoreCardTitle();
            }

            @Override
            public void onCardDisappeared(View view, int position) {}

            @Override
            public void onCardAppeared(View view, int position) {
                mainHandler.post(() -> {
                    if (position == 0)
                        FeedbackUtils.playCardAppear(GameActivityNative.this);
                    cardAdapter.revealCard(position);
                });
            }
        });

        layoutManager.setStackFrom(StackFrom.Top);
        layoutManager.setVisibleCount(3);
        cardStackView.setLayoutManager(layoutManager);
    }

    private void handleCardDragging(Direction direction, float ratio) {

        int pos = layoutManager.getTopPosition();
        RecyclerView.ViewHolder vh = cardStackView.findViewHolderForAdapterPosition(pos);

        if (vh == null || cardAdapter == null)
            return;

        TextView title = vh.itemView.findViewById(R.id.card_name);
        CardData card = cardAdapter.getCardData(pos);

        if (card == null)
            return;

        if (Math.abs(ratio) < 0.05f) {
            title.setText(card.getTitle());
            lastDirection = null;
            return;
        }

        if (direction == Direction.Right && lastDirection != Direction.Right) {
            title.setText(card.getYesResponse());
            lastDirection = Direction.Right;
        } else if (direction == Direction.Left && lastDirection != Direction.Left) {
            title.setText(card.getNoResponse());
            lastDirection = Direction.Left;
        }
    }

    private void restoreCardTitle() {
        int pos = layoutManager.getTopPosition();
        RecyclerView.ViewHolder vh = cardStackView.findViewHolderForAdapterPosition(pos);
        if (vh != null && cardAdapter != null) {
            CardData card = cardAdapter.getCardData(pos);
            if (card != null) {
                ((TextView) vh.itemView.findViewById(R.id.card_name)).setText(card.getTitle());
            }
        }

        lastDirection = null;
    }

    private void handleCardSwiped(Direction direction) {

        FeedbackUtils.playClickFeedback(this);

        GameProgress progress = storage.loadProgress();
        NarrativeTree tree = storage.loadTree();
        KingdomState state = storage.loadKingdomState();

        if (tree == null || progress == null) return;

        NarrativeCard card = getCardById(tree, progress.getCurrentCardId());
        if (card == null) return;

        KingdomState newState = applyCardEffects(state, card);
        storage.saveKingdomState(newState);
        attributeController.updateValues(newState);

        if (isGameOver(newState)) {
            handleGameOver(newState);
            return;
        }

        int count = progress.getDecisionCount() + 1;
        progress.setDecisionCount(count);

        String nextId = getNextCardId(progress.getCurrentCardId(), direction);
        if (nextId != null) progress.setCurrentCardId(nextId);

        storage.saveProgress(progress);

        incrementScore();

        int reignYears = storage.loadReignYears() + 1;
        storage.saveReignYears(reignYears);
        updateReignDisplay(reignYears);

        if (count == MAX_DECISIONS_PER_CYCLE && !storage.isInTwist()) {
            storage.saveTwistState(TwistState.PART_1);
            prepareNextTree();
            showTwistAndSwitchTree(tree);

        } else {
            loadNextCard(nextId);
        }
    }

    private boolean isGameOver(KingdomState state) {
        return state.getWealth() <= 0 || state.getWealth() >= 100 ||
                state.getPeople() <= 0 || state.getPeople() >= 100 ||
                state.getArmy() <= 0 || state.getArmy() >= 100 ||
                state.getFaith() <= 0 || state.getFaith() >= 100;
    }

    private void handleGameOver(KingdomState finalState) {

        StringBuilder message = new StringBuilder();

        if (finalState.getWealth() <= 0)
            message.append("Seu tesouro está vazio. A fome toma o reino.\n");
        else if (finalState.getWealth() >= 100)
            message.append("Sua ganância construiu um império... e destruiu sua humanidade.\n");

        if (finalState.getPeople() <= 0)
            message.append("Não resta ninguém para governar. Você reina sobre ruínas.\n");
        else if (finalState.getPeople() >= 100)
            message.append("O povo o idolatra... mas onde está o rei por trás do mito?\n");

        if (finalState.getArmy() <= 0)
            message.append("Seus soldados desertaram. Inimigos cercam o castelo.\n");
        else if (finalState.getArmy() >= 100)
            message.append("Seu exército é invencível... e já não reconhece seu comando.\n");

        if (finalState.getFaith() <= 0)
            message.append("Os templos estão vazios. Até os deuses viraram as costas.\n");
        else if (finalState.getFaith() >= 100)
            message.append("Você foi elevado aos céus... mas abandonou seu reino mortal.\n");

        message.append("\nReinado: ").append(storage.loadReignYears()).append(" ano(s).");

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_game_over);
        dialog.setCancelable(false);

        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView txtMessage = dialog.findViewById(R.id.txt_game_over_message);
        txtMessage.setText(message.toString());

        Button btnExit = dialog.findViewById(R.id.btn_exit);
        Button btnRestart = dialog.findViewById(R.id.btn_restart);

        btnExit.setOnClickListener(v -> {
            dialog.dismiss();
            storage.clear();
            finish();
        });

        btnRestart.setOnClickListener(v -> {
            dialog.dismiss();
            storage.clear();
            Intent intent = new Intent(GameActivityNative.this, ContextActivity.class);
            startActivity(intent);
            finish();
        });

        dialog.show();
    }

    private void prepareNextTree() {
        synchronized (this) {
            if (isNextTreeGenerationRequested) return;

            isNextTreeGenerationRequested = true;

            backgroundExecutor.execute(() -> {
                boolean success = generateTreeInternal();
                if (!success) {
                    isNextTreeGenerationRequested = false;
                }
            });
        }
    }


    private void showTwistAndSwitchTree(NarrativeTree tree) {
        attributeController.hide();
        menuConfigController.hideMenu();
        MusicManager.switchTrack(this, R.raw.bg_pilot_twist, true);
        showTwistPart1(tree);
    }

    private void showTwistPart1(NarrativeTree tree) {

        storage.saveTwistState(TwistState.PART_1);

        TwistDialog.newInstance(
                tree.getTwist().getPart1(),
                () -> showTwistPart2(tree)

        ).show(getSupportFragmentManager(), "twist_part_1");
    }

    private void showTwistPart2(NarrativeTree tree) {

        storage.saveTwistState(TwistState.PART_2);

        TwistDialog.newInstance(
                tree.getTwist().getPart2(),
                () -> showTwistPart3(tree)
        ).show(getSupportFragmentManager(), "twist_part_2");
    }

    private void showTwistPart3(NarrativeTree tree) {
        TwistDialog.newInstance(
                tree.getTwist().getPart3(),
                this::switchToNextTree
        ).show(getSupportFragmentManager(), "twist_part_3");
    }

    private void switchToNextTree() {

        MusicManager.switchTrack(this, R.raw.bg_game, true);

        attributeController.show();
        menuConfigController.showMenu();

        GameProgress currentProgress = storage.loadProgress();
        int currentDecisionCount = (currentProgress != null) ? currentProgress.getDecisionCount() : 0;

        GameProgress newProgress = new GameProgress();
        newProgress.setDecisionCount(currentDecisionCount);
        newProgress.setCurrentCardId("root");
        storage.saveProgress(newProgress);

        loadNextCard("root");

        isNextTreeGenerationRequested = false;
        storage.saveTwistState(TwistState.NONE);
    }

    private void generateInitialTreeAndStart() {
        new Thread(() -> {
            boolean ok = generateTreeInternal();
            mainHandler.post(() -> {
                if (ok) startIntroAnimation();
                else {
                    Toast.makeText(this, "Erro ao gerar história", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }).start();
    }

    private boolean generateTreeInternal() {

        try {
            String apiKey = new SecurePrefs(this).getApiKey();

            if (apiKey == null)
                return false;

            NarrativeEngine engine = new NarrativeEngine(storage, apiKey);
            return engine.generateTree(buildNextContext());
        } catch (Exception e) {
            Log.e(TAG, "Erro ao gerar árvore", e);
            return false;
        }
    }

    private NarrativeGenerationContext buildNextContext() {
        return new NarrativeGenerationContext(
                storage.loadUserContext(),
                storage.loadKingdomState(),
                storage.loadPreviousTwist()
        );
    }

    private void loadNextCard(String id) {
        NarrativeCard card = getCardById(storage.loadTree(), id);
        if (card == null) return;

        List<CardData> list = new ArrayList<>();
        list.add(convertToCardData(card, card.getCharacter()));
        cardAdapter.updateCards(list);
    }

    private NarrativeCard getCardById(NarrativeTree tree, String id) {
        switch (id) {
            case "root": return tree.getRoot();
            case "yesBranch": return tree.getYesBranch();
            case "noBranch": return tree.getNoBranch();
            case "yesYesLeaf": return tree.getYesYesLeaf();
            case "yesNoLeaf": return tree.getYesNoLeaf();
            case "noYesLeaf": return tree.getNoYesLeaf();
            case "noNoLeaf": return tree.getNoNoLeaf();
            default: return null;
        }
    }

    private String getNextCardId(String currentId, Direction dir) {
        boolean yes = dir == Direction.Right;
        switch (currentId) {
            case "root": return yes ? "yesBranch" : "noBranch";
            case "yesBranch": return yes ? "yesYesLeaf" : "yesNoLeaf";
            case "noBranch": return yes ? "noYesLeaf" : "noNoLeaf";
            default: return null;
        }
    }

    private KingdomState applyCardEffects(KingdomState s, NarrativeCard c) {
        return new KingdomState(
                clamp(s.getWealth() + c.getWealth()),
                clamp(s.getPeople() + c.getPeople()),
                clamp(s.getArmy() + c.getArmy()),
                clamp(s.getFaith() + c.getFaith())
        );
    }

    private int clamp(int v) {
        return Math.max(0, Math.min(100, v));
    }

    private int getCardBackgroundForCharacter(String character) {
        if (character == null) return R.drawable.bg_back_card;

        switch (character.toUpperCase()) {
            case "ISOLDE":
                return R.drawable.bg_card_isolde;
            case "MALACHI":
                return R.drawable.bg_card_malachi;
            case "ELOWEN":
                return R.drawable.bg_card_elowen;
            case "BORIN":
                return R.drawable.bg_card_borin;
            case "NYRA":
                return R.drawable.bg_card_nyra;
            default:
                return R.drawable.bg_back_card;
        }
    }

    private CardData convertToCardData(NarrativeCard c, String character) {

        int background = getCardBackgroundForCharacter(character);

        return new CardData(
                c.getTitle(),
                c.getDescription(),
                background,
                c.getYesResponse(),
                c.getNoResponse()
        );
    }

    private void incrementScore() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("users").document(uid)
                    .update("score", FieldValue.increment(1))
                    .addOnFailureListener(e -> Log.e(TAG, "Erro ao computar ponto", e));
        }
    }
}