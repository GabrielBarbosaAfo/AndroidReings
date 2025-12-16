package br.edu.ifsudestemg.throne.utils.controllers;

import android.view.View;
import android.widget.ImageButton;

import br.edu.ifsudestemg.throne.R;

public class GameMenuController {

    private final MenuSettingController menuController;

    public GameMenuController(View root) {
        menuController = new MenuSettingController(root);

        ImageButton btnMenu = root.findViewById(R.id.btn_open_menu);
        btnMenu.setOnClickListener(v -> toggleMenu());
    }

    public void toggleMenu() {
        menuController.toggle();
    }

    public void showMenu() {
        menuController.showScore();
    }
}

