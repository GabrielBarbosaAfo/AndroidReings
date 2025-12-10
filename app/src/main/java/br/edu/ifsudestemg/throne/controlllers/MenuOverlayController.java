package br.edu.ifsudestemg.throne.controlllers;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import br.edu.ifsudestemg.throne.R;

public class MenuOverlayController {

    private final ConstraintLayout overlayMenu;
    private final LinearLayout pageScore;
    private final LinearLayout pageSettings;
    private final LinearLayout tabScore;
    private final LinearLayout tabSettings;
    private final ImageView btnCloseMenu;

    public MenuOverlayController(@NonNull View rootView) {

        overlayMenu = rootView.findViewById(R.id.overlay_menu);
        pageScore = rootView.findViewById(R.id.page_score);
        pageSettings = rootView.findViewById(R.id.page_settings);
        tabScore = rootView.findViewById(R.id.tab_score);
        tabSettings = rootView.findViewById(R.id.tab_settings);
        btnCloseMenu = rootView.findViewById(R.id.btn_close_menu);

        setupListeners();
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
        if (overlayMenu.getVisibility() == View.VISIBLE) {
            hide();
        } else {
            show();
        }
    }

    private void showScorePage() {
        pageScore.setVisibility(View.VISIBLE);
        pageSettings.setVisibility(View.GONE);

        tabScore.setBackgroundResource(R.drawable.tab_bg_selected);
        tabSettings.setBackgroundResource(R.drawable.tab_bg_normal);
    }

    private void showSettingsPage() {
        pageScore.setVisibility(View.GONE);
        pageSettings.setVisibility(View.VISIBLE);

        tabSettings.setBackgroundResource(R.drawable.tab_bg_selected);
        tabScore.setBackgroundResource(R.drawable.tab_bg_normal);
    }
}