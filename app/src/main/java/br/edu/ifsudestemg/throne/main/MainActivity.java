package br.edu.ifsudestemg.throne.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.edu.ifsudestemg.throne.screens.game.ContextActivity;
import br.edu.ifsudestemg.throne.screens.setting.LoginActivity;
import br.edu.ifsudestemg.throne.screens.setting.SettingsActivity;
import br.edu.ifsudestemg.throne.data.SecurePrefs;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private SecurePrefs securePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        routeUser();
    }

    private void init() {

        mAuth = FirebaseAuth.getInstance();

        try {
            securePrefs = new SecurePrefs(this);
        } catch (Exception e) {
            securePrefs = null;
        }
    }

    private void routeUser() {

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            navigateTo(LoginActivity.class);
        } else if (!hasApiKey()) {
            navigateTo(SettingsActivity.class);
        } else {
            navigateTo(ContextActivity.class);
        }
    }

    private boolean hasApiKey() {
        return securePrefs != null && securePrefs.hasApiKey();
    }

    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(this, targetActivity));
        finish();
    }
}
