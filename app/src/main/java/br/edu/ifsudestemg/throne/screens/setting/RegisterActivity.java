package br.edu.ifsudestemg.throne.screens.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.utils.animations.FeedbackUtils;
import br.edu.ifsudestemg.throne.utils.animations.TopBanner;
import br.edu.ifsudestemg.throne.utils.setting.AuthManager;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameInput, usernameInput, emailInput, passwordInput;
    private Button registerButton;

    private AuthManager auth;
    private TopBanner topBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
        nameInput = findViewById(R.id.et_nome);
        usernameInput = findViewById(R.id.et_username);
        emailInput = findViewById(R.id.et_email);
        passwordInput = findViewById(R.id.et_senha);
        registerButton = findViewById(R.id.btn_registrar);
    }

    private void initListeners() {
        registerButton.setOnClickListener(v -> {
            FeedbackUtils.playClickFeedback(this);
            registerUser();
        });
    }

    private void registerUser() {

        String name = nameInput.getText().toString().trim();
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            topBanner.show(getString(R.string.empty_field));
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            topBanner.show(getString(R.string.invalid_email));
            return;
        }

        if (password.length() < 6) {
            topBanner.show(getString(R.string.password_min_length));
            return;
        }

        lockRegisterButton();

        auth.registerUser(email, password, new AuthManager.AuthCallback() {
            @Override
            public void onSuccess() {
                FirebaseUser user = auth.getCurrentUser();
                if (user == null) {
                    unlockRegisterButton();
                    topBanner.show(getString(R.string.generic_error));
                    return;
                }

                auth.saveUserData(
                        user.getUid(),
                        name,
                        username,
                        email,
                        new AuthManager.AuthCallback() {

                            @Override
                            public void onSuccess() {
                                topBanner.show(getString(R.string.complete_register));

                                registerButton.postDelayed(() -> {
                                    startActivity(new Intent(
                                            RegisterActivity.this,
                                            LoginActivity.class
                                    ));
                                    finish();
                                }, 1200);
                            }

                            @Override
                            public void onFail(String error) {
                                unlockRegisterButton();
                                topBanner.show(error);
                            }
                        }
                );
            }

            @Override
            public void onFail(String error) {
                unlockRegisterButton();
                topBanner.show(error);
            }
        });
    }

    private void lockRegisterButton() {
        registerButton.setEnabled(false);
        registerButton.setText(getString(R.string.register_processing));
        registerButton.setAlpha(0.6f);
    }

    private void unlockRegisterButton() {
        registerButton.setEnabled(true);
        registerButton.setText(getString(R.string.register_btn_enter));
        registerButton.setAlpha(1f);
    }
}
