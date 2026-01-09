package br.edu.ifsudestemg.throne.utils.controllers;

import android.view.View;

import com.yuyakaido.android.cardstackview.Direction;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.model.NarrativeCard;
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
        view.setIcons(
                R.drawable.ic_progress_empty,
                R.drawable.ic_progress_full
        );
    }

    public void show() {
        bar.setVisibility(View.VISIBLE);
    }

    public void hide() {
        bar.setVisibility(View.INVISIBLE);
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

    public void applyFocus(NarrativeCard card, Direction direction) {

        if (card == null)
            return;

        if (direction == com.yuyakaido.android.cardstackview.Direction.Right) {
            setFocus(R.id.layout_wealth, card.getYesWealth() == 0);
            setFocus(R.id.layout_people, card.getYesPeople() == 0);
            setFocus(R.id.layout_army, card.getYesArmy() == 0);
            setFocus(R.id.layout_religion, card.getYesFaith() == 0);
        }
        else if (direction == com.yuyakaido.android.cardstackview.Direction.Left) {
            setFocus(R.id.layout_wealth, card.getNoWealth() == 0);
            setFocus(R.id.layout_people, card.getNoPeople() == 0);
            setFocus(R.id.layout_army, card.getNoArmy() == 0);
            setFocus(R.id.layout_religion, card.getNoFaith() == 0);
        }
    }

    public void resetFocus() {
        setFocus(R.id.layout_wealth, true);
        setFocus(R.id.layout_people, true);
        setFocus(R.id.layout_army, true);
        setFocus(R.id.layout_religion, true);
    }

    private void setFocus(int viewId, boolean isVisible) {

        View view = bar.findViewById(viewId);
        if (view != null) {
            float targetAlpha = isVisible ? 1.0f : 0.20f;
            if (view.getAlpha() != targetAlpha) {
                view.animate()
                        .alpha(targetAlpha)
                        .setDuration(10)
                        .start();
            }
        }
    }
}
