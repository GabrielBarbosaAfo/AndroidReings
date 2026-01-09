package br.edu.ifsudestemg.throne.screens.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.data.GameStorage;
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
    private static final String TAG = "LoginActivity";

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
        if (bannerLayout != null && bannerText != null) {
            topBanner = new TopBanner(bannerLayout, bannerText, 250, 2500);
        }
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
        String username = usernameInput.getText().toString().trim().toLowerCase();
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
                        handlePostLogin();
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
                topBanner.show("Erro de conexão com o banco.");
            }
        });
    }

    private void handlePostLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            unlockLoginButton();
            topBanner.show(getString(R.string.generic_error));
            return;
        }

        if (!user.isEmailVerified()) {
            unlockLoginButton();
            showVerificationSentDialog(user, user.getEmail());
            FirebaseAuth.getInstance().signOut();
            return;
        }

        saveFirebaseLogin(user);
        saveLocalLogin();
        navigateToSettings();
    }

    private void showVerificationSentDialog(FirebaseUser user, String email) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_email_verification, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView).setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();

        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Button btnOk = dialogView.findViewById(R.id.btn_ok);
        Button btnResend = dialogView.findViewById(R.id.btn_resend);
        TextView txtMessage = dialogView.findViewById(R.id.txt_message);

        txtMessage.setText("Sua conta ainda não foi verificada.\n\n" +
                "Enviamos um link para:\n" + email +
                "\n\n💡 Dica: se não aparecer na caixa de entrada,\n" +
                "verifique a pasta de Spam ou Promoções.");

        btnOk.setOnClickListener(v -> dialog.dismiss());

        btnResend.setOnClickListener(v -> {
            dialog.dismiss();
            resendVerificationEmail(user, email);
        });
    }

    private void resendVerificationEmail(FirebaseUser user, String email) {
        topBanner.show("Enviando...");
        auth.sendEmailVerification(new AuthManager.AuthCallback() {
            @Override
            public void onSuccess() {
                topBanner.show("E-mail reenviado!");
                showVerificationSentDialog(user, email);
            }

            @Override
            public void onFail(String error) {
                topBanner.show("Falha: " + error);
            }
        });
    }

    private void saveFirebaseLogin(FirebaseUser user) {
        String username = usernameInput.getText().toString().trim();
        String uid = user.getUid();
        String name = user.getDisplayName();

        GameStorage storage = new GameStorage(LoginActivity.this);
        storage.initUser(uid, (name != null && !name.isEmpty()) ? name : username);
    }

    private void saveLocalLogin() {
        try {
            SecurePrefs prefs = new SecurePrefs(this);
            prefs.saveApiKey("");
        } catch (Exception e) {
            Log.e(TAG, "Erro SecurePrefs: " + e.getMessage());
        }
    }

    private void navigateToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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