package br.edu.ifsudestemg.throne.utils;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.utils.FeedbackUtils;

public class CardAnimator {

    private final FrameLayout animationLayer;
    private final Context context;
    private final Handler handler;

    private final List<View> fakeCardsList = new ArrayList<>();
    private final int totalSteps;

    public interface AnimationEndListener {
        void onAnimationEnd();
    }

    public CardAnimator(FrameLayout animationLayer, Context context, int totalSteps) {
        this.animationLayer = animationLayer;
        this.context = context;
        this.totalSteps = totalSteps;
        this.handler = new Handler(context.getMainLooper());
    }

    public void startAnimation(AnimationEndListener listener) {
        animateStep(0, listener);
    }

    private void animateStep(int step, AnimationEndListener listener) {

        if (step >= totalSteps) {
            for (View card : fakeCardsList) {
                if (card.getParent() != null) {
                    ((ViewGroup) card.getParent()).removeView(card);
                }
            }

            if (listener != null) {
                listener.onAnimationEnd();
            }
            return;
        }

        View card = View.inflate(context, R.layout.layout_item_card_back, null);
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
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withStartAction(() -> FeedbackUtils.playCardAppear(context))
                .withEndAction(() ->
                        handler.postDelayed(() -> animateStep(step + 1, listener), 100)
                )
                .start();
    }
}
