package br.edu.ifsudestemg.throne.utils.controllers;

import android.view.View;
import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.narrative.KingdomState;
import br.edu.ifsudestemg.throne.utils.animations.AttributeBarAnimator;
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
        PercentageIconView view = bar.findViewById(id);
        view.setIcons(R.drawable.ic_progress_empty, R.drawable.ic_progress_full);
    }

    public void show() {
        bar.setVisibility(View.VISIBLE);
    }

    public void updateValues(KingdomState state) {
        updateValue(R.id.progress_wealth, state.getWealth());
        updateValue(R.id.progress_people, state.getPeople());
        updateValue(R.id.progress_army, state.getArmy());
        updateValue(R.id.progress_religion, state.getFaith());
    }

    private void updateValue(int viewId, int targetValue) {
        PercentageIconView view = bar.findViewById(viewId);
        AttributeBarAnimator.animateTo(view, targetValue, 600);
    }

    public void initializeValues(KingdomState state) {
        setInstantValue(R.id.progress_wealth, state.getWealth());
        setInstantValue(R.id.progress_people, state.getPeople());
        setInstantValue(R.id.progress_army, state.getArmy());
        setInstantValue(R.id.progress_religion, state.getFaith());
    }

    private void setInstantValue(int viewId, int value) {
        PercentageIconView view = bar.findViewById(viewId);
        view.setPercentage(value);
    }
}