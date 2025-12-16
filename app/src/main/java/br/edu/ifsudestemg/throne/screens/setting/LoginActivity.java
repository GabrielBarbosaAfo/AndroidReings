package br.edu.ifsudestemg.throne.screens.setting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.utils.animations.TopBanner;
import br.edu.ifsudestemg.throne.utils.setting.AuthManager;
import br.edu.ifsudestemg.throne.utils.animations.FeedbackUtils;
import br.edu.ifsudestemg.throne.data.SecurePrefs;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;

    private Button loginButton;

    private TextView goRegisterText;

    private TopBanner topBanner;

    private AuthManager auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = new AuthManager();
        initViews();
        initListeners();
        initTopBanner();
    }

    private void initTopBanner() {
        LinearLayout bannerLayout = findViewById(R.id.top_banner);
        TextView bannerText = findViewById(R.id.top_banner_text);
        topBanner = new TopBanner(bannerLayout, bannerText, 250, 2500);
    }

    private void initViews() {
        usernameInput = findViewById(R.id.input_username_login);
        passwordInput = findViewById(R.id.input_password_login);
        loginButton = findViewById(R.id.btn_login_action);
        goRegisterText = findViewById(R.id.btn_go_register);
    }

    private void initListeners() {
        loginButton.setOnClickListener(v -> {
            FeedbackUtils.playClickFeedback(this);
            loginWithUsername();
        });

        goRegisterText.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void loginWithUsername() {

        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            topBanner.show(getString(R.string.empty_field));
            return;
        }

        lockLoginButton();

        auth.findEmailByUsername(username, new AuthManager.EmailLookupCallback() {

            @Override
            public void onFound(String email) {
                auth.loginWithEmail(email, password, new AuthManager.AuthCallback() {
                    @Override
                    public void onSuccess() {
                        saveLocalLogin();
                        navigateToSettings();
                    }

                    @Override
                    public void onFail(String error) {
                        unlockLoginButton();
                        topBanner.show(getString(R.string.wrong_password));
                    }
                });
            }

            @Override
            public void onNotFound() {
                unlockLoginButton();
                topBanner.show(getString(R.string.user_not_found));
            }

            @Override
            public void onError(String error) {
                unlockLoginButton();
                topBanner.show(error);
            }
        });
    }

    private void saveLocalLogin() {
        try {
            SecurePrefs prefs = new SecurePrefs(this);
            prefs.saveApiKey("");
        } catch (Exception ignored) {}
    }

    private void navigateToSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
        finish();
    }

    private void lockLoginButton() {
        loginButton.setEnabled(false);
        loginButton.setText(getString(R.string.login_btn_enter_process));
        loginButton.setAlpha(0.6f);
    }

    private void unlockLoginButton() {
        loginButton.setEnabled(true);
        loginButton.setText(getString(R.string.login_btn_enter));
        loginButton.setAlpha(1f);
    }
}