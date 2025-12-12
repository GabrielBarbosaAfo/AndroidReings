package br.edu.ifsudestemg.throne.screens.setting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.utils.design.TopBanner;
import br.edu.ifsudestemg.throne.utils.setting.AuthManager;
import br.edu.ifsudestemg.throne.utils.design.FeedbackUtils;
import br.edu.ifsudestemg.throne.data.SecurePrefs;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private Button loginButton;
    private TextView goRegisterText;
    private TextView googleButton;

    private TopBanner topBanner;

    private AuthManager auth;

    private final String WEB_CLIENT_ID = "SEU_WEB_CLIENT_ID";

    private final ActivityResultLauncher<Intent> googleLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    auth.loginWithGoogle(task, new AuthManager.AuthCallback() {
                        @Override
                        public void onSuccess() {
                            saveLocalLogin();
                            navigateToSettings();
                            unlockGoogleButton();
                        }

                        @Override
                        public void onFail(String error) {
                            unlockGoogleButton();
                            topBanner.show(error);
                        }
                    });
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        usernameInput = findViewById(R.id.input_username_login);
        passwordInput = findViewById(R.id.input_password_login);
        loginButton = findViewById(R.id.btn_login_action);
        googleButton = findViewById(R.id.btn_google);
        goRegisterText = findViewById(R.id.btn_go_register);
    }

    private void initListeners() {
        loginButton.setOnClickListener(v -> {
            FeedbackUtils.playClickFeedback(this);
            loginWithUsername();
        });

        googleButton.setOnClickListener(v -> {
            FeedbackUtils.playClickFeedback(this);
            lockGoogleButton();
            googleLauncher.launch(auth.getGoogleSignInIntent(WEB_CLIENT_ID));
        });

        goRegisterText.setOnClickListener(v ->  startActivity(new Intent(this, RegisterActivity.class)));
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

    private void lockGoogleButton() {
        googleButton.setEnabled(false);
        googleButton.setText(getString(R.string.login_btn_google_process));
        googleButton.setAlpha(0.6f);
    }

    private void unlockGoogleButton() {
        googleButton.setEnabled(true);
        googleButton.setText(getString(R.string.login_btn_google_sign));
        googleButton.setAlpha(1f);
    }


}
