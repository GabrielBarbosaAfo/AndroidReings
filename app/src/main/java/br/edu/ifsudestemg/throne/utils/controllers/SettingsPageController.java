package br.edu.ifsudestemg.throne.utils.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.data.GameStorage;
import br.edu.ifsudestemg.throne.screens.setting.LoginActivity;
import br.edu.ifsudestemg.throne.screens.setting.SettingsActivity;
import br.edu.ifsudestemg.throne.utils.setting.AuthManager;

public class SettingsPageController {

    private final Activity activity;

    private final Button btnChangeApiKey;
    private final Button btnLogout;
    public SettingsPageController(Activity activity, View pageSettings) {
        this.activity = activity;

        btnChangeApiKey = pageSettings.findViewById(R.id.btn_change_api_key);
        btnLogout = pageSettings.findViewById(R.id.btn_logout);

        setupActions();
    }
    private void setupActions() {
        btnChangeApiKey.setOnClickListener(v -> {
            Intent i = new Intent(activity, SettingsActivity.class);
            activity.startActivity(i);
        });

        btnLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
    }

    private void showLogoutConfirmationDialog() {

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_confirm_logout, null);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(dialogView)
                .setCancelable(true)
                .create();

        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm_logout);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();

            new AuthManager().logout();
            new GameStorage(activity).clear();

            Intent i = new Intent(activity, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(i);
        });

        dialog.show();
    }
}
