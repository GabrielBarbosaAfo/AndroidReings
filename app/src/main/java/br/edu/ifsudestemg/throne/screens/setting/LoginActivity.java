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
                        }

                        @Override
                        public void onFail(String error) {
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
                topBanner.show(getString(R.string.user_not_found));
            }

            @Override
            public void onError(String error) {
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
}
