package br.edu.ifsudestemg.throne.utils.controllers;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import br.edu.ifsudestemg.throne.R;

public class MenuSettingController {

    private final ConstraintLayout overlayMenu;

    private final View pageScore;
    private final View pageSettings;

    private final LinearLayout tabScore;
    private final LinearLayout tabSettings;

    private final ImageView iconScore;
    private final ImageView iconSettings;

    private final ImageView btnCloseMenu;

    private final LinearLayout scoreBar;

    public MenuSettingController(@NonNull View rootView) {

        Activity activity = (Activity) rootView.getContext();

        overlayMenu = rootView.findViewById(R.id.overlay_menu);

        pageScore = rootView.findViewById(R.id.page_score);
        pageSettings = rootView.findViewById(R.id.page_settings);

        tabScore = rootView.findViewById(R.id.tab_score);
        tabSettings = rootView.findViewById(R.id.tab_settings);

        iconScore = rootView.findViewById(R.id.icon_score);
        iconSettings = rootView.findViewById(R.id.icon_settings);

        btnCloseMenu = rootView.findViewById(R.id.btn_close_menu);

        scoreBar = rootView.findViewById(R.id.score_bar);

        new SettingsPageController(activity, pageSettings);
        new ScorePageController(pageScore);

        setupListeners();

        setActiveTab(true);
    }
    private void setupListeners() {
        btnCloseMenu.setOnClickListener(v -> hide());
        tabScore.setOnClickListener(v -> showScorePage());
        tabSettings.setOnClickListener(v -> showSettingsPage());
    }

    public void show() {
        overlayMenu.setVisibility(View.VISIBLE);
        showScorePage();
    }

    public void hide() {
        overlayMenu.setVisibility(View.GONE);
    }

    public void toggle() {
        if (overlayMenu.getVisibility() == View.VISIBLE)
            hide();
        else
            show();
    }

    private void showScorePage() {
        pageScore.setVisibility(View.VISIBLE);
        pageSettings.setVisibility(View.GONE);
        setActiveTab(true);
    }

    private void showSettingsPage() {
        pageScore.setVisibility(View.GONE);
        pageSettings.setVisibility(View.VISIBLE);
        setActiveTab(false);
    }

    private void setActiveTab(boolean scoreActive) {
        if (scoreActive) {
            iconScore.setColorFilter(0xFFE0C060);
            iconSettings.setColorFilter(0xFF808080);
        } else {
            iconScore.setColorFilter(0xFF808080);
            iconSettings.setColorFilter(0xFFE0C060);
        }
    }

    public void showScore() {
        scoreBar.setVisibility(View.VISIBLE);
    }
}
