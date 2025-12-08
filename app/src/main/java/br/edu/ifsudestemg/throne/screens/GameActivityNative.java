package br.edu.ifsudestemg.throne.screens;

import android.graphics.Color;
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
import br.edu.ifsudestemg.throne.model.CardData;
import br.edu.ifsudestemg.throne.utils.CardAdapter;
import br.edu.ifsudestemg.throne.utils.FeedbackUtils;
import br.edu.ifsudestemg.throne.views.PercentageIconView;
import br.edu.ifsudestemg.throne.views.ShieldFillView;

public class GameActivityNative extends AppCompatActivity {

    private FrameLayout animationLayer;
    private CardStackView cardStackView;

    private CardAdapter cardAdapter;

    private final List<View> fakeCardsList = new ArrayList<>();

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

        animationLayer = findViewById(R.id.animation_layer);
        cardStackView = findViewById(R.id.card_stack_view);

        initAttribute();
        startIntroAnimation();
    }

    private void initAttribute() {

        PercentageIconView progressWealth = findViewById(R.id.progress_wealth);
        progressWealth.setIcons(R.drawable.ic_progress_empty, R.drawable.ic_progress_full);
        progressWealth.setPercentage(0.5f);

        PercentageIconView progressArmy = findViewById(R.id.progress_army);
        progressArmy.setIcons(R.drawable.ic_progress_empty, R.drawable.ic_progress_full);
        progressArmy.setPercentage(0.5f);

        PercentageIconView progressPeople = findViewById(R.id.progress_people);
        progressPeople.setIcons(R.drawable.ic_progress_empty, R.drawable.ic_progress_full);
        progressPeople.setPercentage(0.5f);

        PercentageIconView progressReligion = findViewById(R.id.progress_religion);
        progressReligion.setIcons(R.drawable.ic_progress_empty, R.drawable.ic_progress_full);
        progressReligion.setPercentage(0.5f);
    }


    private void startIntroAnimation() {
        animateFakeCard(0);
    }

    private void animateFakeCard(int step) {

        final int TOTAL_STEPS = 4;

        if (step >= TOTAL_STEPS) {

            for (View card : fakeCardsList) {
                if (card.getParent() != null) {
                    animationLayer.removeView(card);
                }
            }

            showRealGame();
            return;
        }

        View card = getLayoutInflater().inflate(R.layout.item_card_fake, animationLayer, false);

        animationLayer.addView(card);
        fakeCardsList.add(card);

        card.setTranslationX(-animationLayer.getWidth() * 0.5f);
        card.setTranslationY(-animationLayer.getHeight() * 0.5f);
        card.setAlpha(0f);
        card.setScaleX(0.7f);
        card.setScaleY(0.7f);
        card.setRotation(10f);

        card.animate()
                .translationX(0f)
                .translationY(0f)
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .rotation(0f)
                .setDuration(600)
                .setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator())
                .withStartAction(() -> FeedbackUtils.playCardAppear(GameActivityNative.this))
                .withEndAction(() -> new Handler().postDelayed(() -> animateFakeCard(step + 1), 100))
                .start();
    }

    private void showRealGame() {

        cardStackView.setVisibility(View.VISIBLE);

        List<CardData> cardList = new ArrayList<>();

        for (String text : realGameCards) {
            cardList.add(new CardData(
                    "REI HARRY",
                    text,
                    R.drawable.png_bg_back_card
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