package br.edu.ifsudestemg.throne.screens;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.util.ArrayList;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.controlllers.MenuOverlayController;
import br.edu.ifsudestemg.throne.data.GameStorage;
import br.edu.ifsudestemg.throne.logic.GameOrchestrator;
import br.edu.ifsudestemg.throne.model.CardData;
import br.edu.ifsudestemg.throne.model.CardEvent;
import br.edu.ifsudestemg.throne.model.GameContext;
import br.edu.ifsudestemg.throne.utils.AttributeBarAnimator;
import br.edu.ifsudestemg.throne.utils.CardAdapter;
import br.edu.ifsudestemg.throne.utils.CardAnimator;
import br.edu.ifsudestemg.throne.utils.FeedbackUtils;
import br.edu.ifsudestemg.throne.views.PercentageIconView;

public class GameActivityNative extends AppCompatActivity {

    private FrameLayout animationLayer;
    private CardStackView cardStackView;
    private CardAdapter cardAdapter;
    private GameOrchestrator orchestrator;
    private MenuOverlayController menuController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_native);

        if (!validateUserContext()) return;

        orchestrator = new GameOrchestrator(this);

        animationLayer = findViewById(R.id.animation_layer);
        cardStackView = findViewById(R.id.card_stack_view);

        preloadFirstCards();
        startIntroAnimation();

        menuController = new MenuOverlayController(findViewById(R.id.overlay_menu));
        setupMenuButton();
    }

    /** Valida se o contexto do usuário foi passado corretamente */
    private boolean validateUserContext() {
        String userContext = getIntent().getStringExtra("USER_CONTEXT");
        if (userContext == null || userContext.trim().isEmpty()) {
            finish();
            return false;
        }
        GameStorage storage = new GameStorage(this);
        if (storage.loadContext() == null) {
            storage.saveContext(new GameContext(userContext));
        }
        return true;
    }

    /** Configura o botão de abrir menu dentro do LinearLayout de score_bar */
    private void setupMenuButton() {
        ImageButton btnMenu = findViewById(R.id.btn_open_menu);
        btnMenu.setOnClickListener(v -> toggleMenu());
    }

    /** Alterna o menu overlay aberto/fechado */
    private void toggleMenu() {
        if (menuController != null) {
            menuController.toggle();
        }
    }

    /** Inicia as primeiras cartas */
    private void preloadFirstCards() {
        orchestrator.start();
    }

    /** Inicia a animação de introdução */
    private void startIntroAnimation() {
        CardAnimator animator = new CardAnimator(animationLayer, this, 4);
        animator.startAnimation(this::showRealGame);
    }

    /** Mostra o jogo real após animação */
    private void showRealGame() {
        findViewById(R.id.attributes_bar).setVisibility(View.VISIBLE);
        setupAttributeIcons();
        setAttributeBarsDirectly();

        cardStackView.setVisibility(View.VISIBLE);

        cardAdapter = new CardAdapter(new ArrayList<>());
        CardStackLayoutManager layoutManager = getCardStackLayoutManager();
        layoutManager.setStackFrom(StackFrom.Top);
        layoutManager.setVisibleCount(3);

        cardStackView.setLayoutManager(layoutManager);
        cardStackView.setAdapter(cardAdapter);

        loadNextCardIntoDeck();
        loadNextCardIntoDeck();
    }

    private void loadNextCardIntoDeck() {
        CardEvent ev = orchestrator.getNextCard();
        CardData newCard = new CardData(ev.getTitle(), ev.getDescription(), R.drawable.png_bg_back_card);
        runOnUiThread(() -> cardAdapter.addCard(newCard));
    }

    @NonNull
    private CardStackLayoutManager getCardStackLayoutManager() {
        CardStackListener listener = new CardStackListener() {
            @Override
            public void onCardSwiped(Direction direction) {
                FeedbackUtils.playClickFeedback(GameActivityNative.this);
                orchestrator.recordUserChoice(direction == Direction.Right);
                animateAttributeBars();
                loadNextCardIntoDeck();
            }

            @Override public void onCardDragging(Direction direction, float ratio) {}
            @Override public void onCardRewound() {}
            @Override public void onCardCanceled() {}

            @Override
            public void onCardAppeared(View view, int position) {
                new Handler(getMainLooper()).post(() -> {
                    if (position == 0) FeedbackUtils.playCardAppear(GameActivityNative.this);
                    cardAdapter.revealCard(position);
                });
            }

            @Override public void onCardDisappeared(View view, int position) {}
        };

        return new CardStackLayoutManager(this, listener);
    }

    private void setAttributeBarsDirectly() {
        setBarPercentage(R.id.progress_people, orchestrator.getPeopleRatio());
        setBarPercentage(R.id.progress_army, orchestrator.getArmyRatio());
        setBarPercentage(R.id.progress_wealth, orchestrator.getWealthRatio());
        setBarPercentage(R.id.progress_religion, orchestrator.getFaithRatio());
    }

    private void setBarPercentage(int viewId, float ratio) {
        PercentageIconView view = findViewById(viewId);
        if (view != null) view.setPercentage(ratio);
    }

    private void animateAttributeBars() {
        long duration = 1200;
        animateBar(R.id.progress_people, orchestrator.getPeopleRatio(), duration);
        animateBar(R.id.progress_army, orchestrator.getArmyRatio(), duration);
        animateBar(R.id.progress_wealth, orchestrator.getWealthRatio(), duration);
        animateBar(R.id.progress_religion, orchestrator.getFaithRatio(), duration);
    }

    private void animateBar(int viewId, float targetRatio, long duration) {
        PercentageIconView view = findViewById(viewId);
        if (view != null) AttributeBarAnimator.animateTo(view, targetRatio, duration);
    }

    private void setupAttributeIcons() {
        setupIconView(R.id.progress_wealth, R.drawable.ic_progress_empty, R.drawable.ic_progress_full);
        setupIconView(R.id.progress_army, R.drawable.ic_progress_empty, R.drawable.ic_progress_full);
        setupIconView(R.id.progress_people, R.drawable.ic_progress_empty, R.drawable.ic_progress_full);
        setupIconView(R.id.progress_religion, R.drawable.ic_progress_empty, R.drawable.ic_progress_full);
    }

    private void setupIconView(int viewId, int emptyIcon, int fullIcon) {
        PercentageIconView view = findViewById(viewId);
        if (view != null) view.setIcons(emptyIcon, fullIcon);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orchestrator != null) orchestrator.shutdown();
    }
}
