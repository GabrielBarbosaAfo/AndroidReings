package br.edu.ifsudestemg.throne.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import br.edu.ifsudestemg.throne.screens.ContextActivity;
import br.edu.ifsudestemg.throne.screens.LoginActivity;
import br.edu.ifsudestemg.throne.screens.SettingsActivity;
import br.edu.ifsudestemg.throne.utils.SecurePrefs;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        routeUserForTest();

    }

    private void routeUserForTest() {
        startActivity(new Intent(this, LoginActivity.class));

        finish();
    }

    private void routeUserOriginal() {
        try {
            SecurePrefs prefs = new SecurePrefs(this);

            boolean userLogged = isGoogleLogged(); // Isso precisa ser atualizado para checar o Firebase Auth
            boolean hasKey = prefs.hasApiKey();

            if (!userLogged) {
                startActivity(new Intent(this, LoginActivity.class));
            } else if (!hasKey) {
                startActivity(new Intent(this, SettingsActivity.class));
            } else {
                startActivity(new Intent(this, ContextActivity.class));
            }

        } catch (Exception e) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        finish();
    }

    private boolean isGoogleLogged() {
        return true;
    }
}