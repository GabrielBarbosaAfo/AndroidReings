package br.edu.ifsudestemg.throne.screens.setting;

import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.utils.design.FeedbackUtils;
import br.edu.ifsudestemg.throne.utils.design.TopBanner;
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

        auth = new AuthManager(this);
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

        auth.registerUser(email, password, new AuthManager.AuthCallback() {
            @Override
            public void onSuccess() {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    auth.saveUserData(user.getUid(), name, username, email, new AuthManager.AuthCallback() {
                        @Override
                        public void onSuccess() {
                            topBanner.show(getString(R.string.complete_register));
                            finish();
                        }

                        @Override
                        public void onFail(String error) {
                            topBanner.show(error);
                        }
                    });
                }
            }

            @Override
            public void onFail(String error) {
                topBanner.show(error);
            }
        });
    }
}
