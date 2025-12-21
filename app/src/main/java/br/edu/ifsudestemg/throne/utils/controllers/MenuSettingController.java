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

    private final View pageCharacters;

    private final LinearLayout tabScore;
    private final LinearLayout tabSettings;

    private final LinearLayout tabCharacters;

    private final ImageView iconScore;
    private final ImageView iconSettings;
    private final ImageView iconCharacters;

    private final ImageView btnCloseMenu;

    private final LinearLayout scoreBar;

    public MenuSettingController(@NonNull View rootView) {

        Activity activity = (Activity) rootView.getContext();

        overlayMenu = rootView.findViewById(R.id.overlay_menu);

        pageScore = rootView.findViewById(R.id.page_score);
        pageSettings = rootView.findViewById(R.id.page_settings);
        pageCharacters = rootView.findViewById(R.id.page_characters);

        tabScore = rootView.findViewById(R.id.tab_score);
        tabSettings = rootView.findViewById(R.id.tab_settings);
        tabCharacters = rootView.findViewById(R.id.tab_characters);

        iconScore = rootView.findViewById(R.id.icon_score);
        iconSettings = rootView.findViewById(R.id.icon_settings);
        iconCharacters = rootView.findViewById(R.id.icon_characters);

        btnCloseMenu = rootView.findViewById(R.id.btn_close_menu);

        scoreBar = rootView.findViewById(R.id.score_bar);

        new SettingsPageController(activity, pageSettings);
        new ScorePageController(pageScore);

        setupListeners();

        setActiveTab("score");
    }

    private void setupListeners() {
        btnCloseMenu.setOnClickListener(v -> hide());
        tabScore.setOnClickListener(v -> showScorePage());
        tabSettings.setOnClickListener(v -> showSettingsPage());
        tabCharacters.setOnClickListener(v -> showCharactersPage());
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

    private void showPage(View pageToShow) {

        pageScore.setVisibility(View.GONE);
        pageSettings.setVisibility(View.GONE);
        pageCharacters.setVisibility(View.GONE);

        pageToShow.setVisibility(View.VISIBLE);
    }


    private void showScorePage() {
        showPage(pageScore);
        setActiveTab("score");
    }

    private void showSettingsPage() {
        showPage(pageSettings);
        setActiveTab("settings");
    }

    private void showCharactersPage() {
        showPage(pageCharacters);
        setActiveTab("characters");
    }

    private void setActiveTab(String activeTab) {

        int activeColor = 0xFFE0C060;
        int inactiveColor = 0xFF808080;

        iconScore.setColorFilter(activeTab.equals("score") ? activeColor : inactiveColor);
        iconSettings.setColorFilter(activeTab.equals("settings") ? activeColor : inactiveColor);
        iconCharacters.setColorFilter(activeTab.equals("characters") ? activeColor : inactiveColor);
    }

    public void showScore() {
        scoreBar.setVisibility(View.VISIBLE);
    }

    public void hideScore() {
        scoreBar.setVisibility(View.INVISIBLE);
    }
}
