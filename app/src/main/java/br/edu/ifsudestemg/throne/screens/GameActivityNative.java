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

import java.util.List;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.logic.GameOrchestrator;
import br.edu.ifsudestemg.throne.model.CardData;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_native);

        String userContext = getIntent().getStringExtra("USER_CONTEXT");
        if (userContext == null) {
            finish();
            return;
        }

        orchestrator = new GameOrchestrator(this, userContext);

        animationLayer = findViewById(R.id.animation_layer);
        cardStackView = findViewById(R.id.card_stack_view);

        startIntroAnimation();
    }

    private void startIntroAnimation() {
        CardAnimator animator = new CardAnimator(animationLayer, this, 4);
        animator.startAnimation(this::showRealGame);
    }

    private void showRealGame() {

        findViewById(R.id.attributes_bar).setVisibility(View.VISIBLE);

        setupAttributeIcons();

        animateAttributeBars();

        cardStackView.setVisibility(View.VISIBLE);

        List<CardData> cards = orchestrator.provideInitialCards();

        cardAdapter = new CardAdapter(cards);

        CardStackListener listener = new CardStackListener() {
            @Override
            public void onCardSwiped(Direction direction) {
                FeedbackUtils.playClickFeedback(GameActivityNative.this);
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

    private void animateAttributeBars() {

        long duration = 1200;

        AttributeBarAnimator.animateTo(
                findViewById(R.id.progress_people),
                orchestrator.getPeopleRatio(),
                duration
        );

        AttributeBarAnimator.animateTo(
                findViewById(R.id.progress_army),
                orchestrator.getArmyRatio(),
                duration
        );

        AttributeBarAnimator.animateTo(
                findViewById(R.id.progress_wealth),
                orchestrator.getWealthRatio(),
                duration
        );

        AttributeBarAnimator.animateTo(
                findViewById(R.id.progress_religion),
                orchestrator.getFaithRatio(),
                duration
        );
    }

    private void setupAttributeIcons() {
        setupIconView(R.id.progress_wealth, R.drawable.ic_progress_empty, R.drawable.ic_progress_full);
        setupIconView(R.id.progress_army, R.drawable.ic_progress_empty, R.drawable.ic_progress_full);
        setupIconView(R.id.progress_people, R.drawable.ic_progress_empty, R.drawable.ic_progress_full);
        setupIconView(R.id.progress_religion, R.drawable.ic_progress_empty, R.drawable.ic_progress_full);
    }

    private void setupIconView(int viewId, int emptyIcon, int fullIcon) {
        PercentageIconView view = findViewById(viewId);
        view.setIcons(emptyIcon, fullIcon);
    }
}