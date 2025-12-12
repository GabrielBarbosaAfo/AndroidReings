package br.edu.ifsudestemg.throne.screens;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.utils.controllers.AttributeBarController;
import br.edu.ifsudestemg.throne.utils.controllers.GameMenuController;
import br.edu.ifsudestemg.throne.data.GameStorage;
import br.edu.ifsudestemg.throne.model.CardData;
import br.edu.ifsudestemg.throne.model.GameContext;
import br.edu.ifsudestemg.throne.utils.CardAdapter;
import br.edu.ifsudestemg.throne.utils.design.CardAnimator;
import br.edu.ifsudestemg.throne.utils.design.FeedbackUtils;

public class GameActivityNative extends AppCompatActivity {

    private FrameLayout animationLayer;

    private CardStackView cardStackView;

    private CardAdapter cardAdapter;

    private AttributeBarController attributeController;

    private GameMenuController menuConfigController;

    private final List<String> realGameCards = Arrays.asList(
            "O tesouro real foi roubado. Prender o suspeito?",
            "Um profeta prevê fome. Aumentar impostos?",
            "Um dragão aparece. Mandar o exército?",
            "Um viajante oferece tecnologia futura. Aceitar?",
            "O povo pede festa. Gastar ouro do reino?"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_native);

        if (!validateUserContext())
            return;

        initComponents();

        startIntroAnimation();
    }

    private void initComponents() {
        animationLayer = findViewById(R.id.animation_layer);
        cardStackView = findViewById(R.id.card_stack_view);

        menuConfigController = new GameMenuController(findViewById(android.R.id.content));
        attributeController = new AttributeBarController(findViewById(android.R.id.content));
        attributeController.setupIcons();
    }

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

    private void startIntroAnimation() {
        CardAnimator animator = new CardAnimator(animationLayer, this, 4);

        animator.startAnimation(() -> {
            showAttributeBar();
            menuConfigController.showMenu();
            showRealGame();
        });
    }

    private void showAttributeBar() {
        attributeController.show();
        attributeController.animateAll(1200);
    }

    private void showRealGame() {

        cardStackView.setVisibility(View.VISIBLE);

        List<CardData> cardList = new ArrayList<>();
        for (String text : realGameCards) {
            cardList.add(new CardData(
                    "REI HARRY",
                    text,
                    R.drawable.bg_back_card
            ));
        }

        cardAdapter = new CardAdapter(cardList);

        CardStackListener listener = new CardStackListener() {
            @Override
            public void onCardSwiped(Direction direction) {
                FeedbackUtils.playClickFeedback(GameActivityNative.this);
                String answer = (direction == Direction.Right) ? "Sim" : "Não";
            }

            @Override public void onCardDragging(Direction direction, float ratio) {}
            @Override public void onCardRewound() {}
            @Override public void onCardCanceled() {}

            @Override
            public void onCardAppeared(View view, int position) {
                new Handler(getMainLooper()).post(() -> {
                    if (position == 0)
                        FeedbackUtils.playCardAppear(GameActivityNative.this);

                    cardAdapter.revealCard(position);
                });
            }

            @Override public void onCardDisappeared(View view, int position) {}
        };

        CardStackLayoutManager layoutManager = new CardStackLayoutManager(this, listener);
        layoutManager.setStackFrom(StackFrom.Top);
        layoutManager.setVisibleCount(3);

        cardStackView.setLayoutManager(layoutManager);
        cardStackView.setAdapter(cardAdapter);
    }
}
