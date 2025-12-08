package br.edu.ifsudestemg.throne.utils;

import android.animation.ValueAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;

import br.edu.ifsudestemg.throne.views.PercentageIconView;

public class AttributeBarAnimator {

    public static void animateTo(PercentageIconView bar, float targetValue, long durationMs) {

        float clampedTarget = Math.max(0f, Math.min(1f, targetValue));
        ValueAnimator animator = ValueAnimator.ofFloat(0f, clampedTarget);
        animator.setDuration(durationMs);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            bar.setPercentage(value);
        });

        animator.start();
    }
}
