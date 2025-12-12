package br.edu.ifsudestemg.throne.utils.controllers;

import android.view.View;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.utils.design.AttributeBarAnimator;
import br.edu.ifsudestemg.throne.views.PercentageIconView;

public class AttributeBarController {

    private final View bar;

    public AttributeBarController(View root) {
        bar = root.findViewById(R.id.attributes_bar);
    }

    public void setupIcons() {
        setupIcon(R.id.progress_people);
        setupIcon(R.id.progress_army);
        setupIcon(R.id.progress_wealth);
        setupIcon(R.id.progress_religion);
    }

    private void setupIcon(int id) {
        PercentageIconView view = bar.getRootView().findViewById(id);
        view.setIcons(R.drawable.ic_progress_empty, R.drawable.ic_progress_full);
    }

    public void animateAll(long duration) {
        animate(R.id.progress_people, 50, duration);
        animate(R.id.progress_army, 50, duration);
        animate(R.id.progress_wealth, 50, duration);
        animate(R.id.progress_religion, 50, duration);
    }

    private void animate(int id, int target, long duration) {
        PercentageIconView view = bar.getRootView().findViewById(id);
        AttributeBarAnimator.animateTo(view, target, duration);
    }

    public void show() {
        bar.setVisibility(View.VISIBLE);
    }
}
