package br.edu.ifsudestemg.throne.screens.game;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.data.GameStorage;
import br.edu.ifsudestemg.throne.model.CardData;
import br.edu.ifsudestemg.throne.model.NarrativeCard;
import br.edu.ifsudestemg.throne.model.NarrativeTree;
import br.edu.ifsudestemg.throne.utils.controllers.AttributeBarController;
import br.edu.ifsudestemg.throne.utils.controllers.GameMenuController;
import br.edu.ifsudestemg.throne.utils.design.CardAdapter;
import br.edu.ifsudestemg.throne.utils.design.CardAnimator;
import br.edu.ifsudestemg.throne.utils.design.FeedbackUtils;

public class GameActivityNative extends AppCompatActivity {

    private FrameLayout animationLayer;

    private CardStackView cardStackView;

    private CardAdapter cardAdapter;

    private CardStackLayoutManager layoutManager;

    private AttributeBarController attributeController;

    private GameMenuController menuConfigController;

    private Direction lastDirection = null;

    private GameStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_native);

        storage = new GameStorage(this);

        if (!validateUserContext()) return;

        initComponents();
        startIntroAnimation();
    }

    private void initComponents() {
        animationLayer = findViewById(R.id.animation_layer);
        cardStackView = findViewById(R.id.card_stack_view);

        View root = findViewById(android.R.id.content);
        menuConfigController = new GameMenuController(root);
        attributeController = new AttributeBarController(root);
        attributeController.setupIcons();
    }

    private boolean validateUserContext() {
        String userContext = getIntent().getStringExtra("USER_CONTEXT");
        if (userContext == null || userContext.trim().isEmpty()) {
            finish();
            return false;
        }

        if (storage.loadUserContext() == null) {
            storage.saveUserContext(userContext);
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

        NarrativeTree tree = storage.loadTree();

        if (tree == null || tree.getRoot() == null) {
            finish();
            return;
        }

        List<CardData> cardList = new ArrayList<>();
        cardList.add(convertToCardData(tree.getRoot(), R.drawable.bg_back_card));

        cardAdapter = new CardAdapter(cardList);

        CardStackListener listener = new CardStackListener() {

            @Override
            public void onCardDragging(Direction direction, float ratio) {
                int topPos = layoutManager.getTopPosition();
                RecyclerView.ViewHolder vh = cardStackView.findViewHolderForAdapterPosition(topPos);
                if (vh == null || cardAdapter == null) return;

                TextView titleView = vh.itemView.findViewById(R.id.card_name);
                CardData currentCard = cardAdapter.getCardData(topPos);
                if (currentCard == null) return;

                if (Math.abs(ratio) < 0.05f) {
                    titleView.setText(currentCard.getTitle());
                    lastDirection = null;
                    return;
                }

                if (direction == Direction.Right && lastDirection != Direction.Right) {
                    titleView.setText(currentCard.getYesResponse());
                    lastDirection = Direction.Right;
                } else if (direction == Direction.Left && lastDirection != Direction.Left) {
                    titleView.setText(currentCard.getNoResponse());
                    lastDirection = Direction.Left;
                }
            }

            private void restoreTitleForTopCard() {
                int topPos = layoutManager.getTopPosition();
                RecyclerView.ViewHolder vh = cardStackView.findViewHolderForAdapterPosition(topPos);
                if (vh != null && cardAdapter != null) {
                    TextView titleView = vh.itemView.findViewById(R.id.card_name);
                    CardData card = cardAdapter.getCardData(topPos);
                    if (card != null) {
                        titleView.setText(card.getTitle());
                    }
                }
                lastDirection = null;
            }

            @Override
            public void onCardSwiped(Direction direction) {
                FeedbackUtils.playClickFeedback(GameActivityNative.this);
                lastDirection = null;
            }

            @Override
            public void onCardCanceled() {
                restoreTitleForTopCard();
            }

            @Override
            public void onCardRewound() {
                restoreTitleForTopCard();
            }

            @Override
            public void onCardAppeared(View view, int position) {
                new Handler(getMainLooper()).post(() -> {
                    if (position == 0) {
                        FeedbackUtils.playCardAppear(GameActivityNative.this);
                    }
                    cardAdapter.revealCard(position);
                });
            }

            @Override
            public void onCardDisappeared(View view, int position) {
            }
        };

        layoutManager = new CardStackLayoutManager(this, listener);
        layoutManager.setStackFrom(StackFrom.Top);
        layoutManager.setVisibleCount(3);

        cardStackView.setLayoutManager(layoutManager);
        cardStackView.setAdapter(cardAdapter);
    }

    private CardData convertToCardData(NarrativeCard card, int imageRes) {
        return new CardData(
                card.getTitle(),
                card.getDescription(),
                imageRes,
                card.getYesResponse(),
                card.getNoResponse()
        );
    }
}