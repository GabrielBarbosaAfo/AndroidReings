package br.edu.ifsudestemg.throne.utils.animations;

import android.animation.ValueAnimator;

import br.edu.ifsudestemg.throne.views.PercentageIconView;

public class AttributeBarAnimator {

    public static void animateTo(PercentageIconView view, int targetValue, long duration) {

        int clampedValue = Math.max(0, Math.min(100, targetValue));

        float targetFloat = clampedValue / 100f;
        float currentFloat = view.getPercentage();

        view.clearAnimation();

        ValueAnimator animator = ValueAnimator.ofFloat(currentFloat, targetFloat);

        animator.setDuration(duration);

        animator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            view.setPercentage(animatedValue);
        });

        animator.start();
    }
}