package br.edu.ifsudestemg.throne.utils;

import android.view.View;
import android.widget.TextView;

public class TopBanner {

    private final View bannerView;
    private final TextView bannerText;
    private final int animDuration;
    private final int showDuration;

    public TopBanner(View bannerView, TextView bannerText, int animDuration, int showDuration) {
        this.bannerView = bannerView;
        this.bannerText = bannerText;
        this.animDuration = animDuration;
        this.showDuration = showDuration;
    }

    public void show(String message) {

        bannerText.setText(message);
        bannerView.setVisibility(View.VISIBLE);

        bannerView.setTranslationY(-bannerView.getHeight());
        bannerView.animate()
                .translationY(0)
                .setDuration(animDuration)
                .start();

        bannerView.postDelayed(() ->
                        bannerView.animate()
                                .translationY(-bannerView.getHeight())
                                .setDuration(animDuration)
                                .withEndAction(() -> bannerView.setVisibility(View.GONE))
                                .start(),
                showDuration
        );
    }
}

