package br.edu.ifsudestemg.throne.utils.design;

import android.animation.ValueAnimator;

import br.edu.ifsudestemg.throne.views.PercentageIconView;

public class AttributeBarAnimator {

    public static void animateTo(PercentageIconView view, float targetRatio, long duration) {

        if (view == null)
            return;

        float start = view.getPercentage();
        float end = Math.max(0f, Math.min(100f, targetRatio)) / 100f;

        if (Math.abs(end - start) < 0.001f)
            return;

        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            view.setPercentage(value);
        });

        animator.start();
    }
}