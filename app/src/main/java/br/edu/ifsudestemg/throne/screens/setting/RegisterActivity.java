package br.edu.ifsudestemg.throne.screens.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
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
        if (bannerLayout != null && bannerText != null) {
            topBanner = new TopBanner(bannerLayout, bannerText, 250, 2500);
        }
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
        String username = usernameInput.getText().toString().trim().toLowerCase();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            topBanner.show(getString(R.string.empty_field));
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            topBanner.show("O e-mail digitado não é válido.");
            return;
        }

        if (password.length() < 6) {
            topBanner.show(getString(R.string.password_min_length));
            return;
        }

        lockRegisterButton();

        // PASSO 1: Verifica se o username já existe
        checkUsernameAndRegister(email, password, name, username);
    }

    private void checkUsernameAndRegister(String email, String password, String name, String username) {
        auth.findEmailByUsername(username, new AuthManager.EmailLookupCallback() {
            @Override
            public void onFound(String emailEncontrado) {
                unlockRegisterButton();
                topBanner.show("Este nome de usuário já está sendo usado.");
            }

            @Override
            public void onNotFound() {
                // PASSO 2: Se não achou o username, agora sim cria no Firebase Auth
                createUserInFirebase(email, password, name, username);
            }

            @Override
            public void onError(String error) {
                unlockRegisterButton();
                topBanner.show("Erro ao validar usuário. Verifique sua conexão.");
            }
        });
    }

    private void createUserInFirebase(String email, String password, String name, String username) {
        auth.registerUser(email, password, new AuthManager.AuthCallback() {
            @Override
            public void onSuccess() {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    // PASSO 3: Salva no Firestore
                    saveUserDataToFirestore(user, name, username, email);
                }
            }

            @Override
            public void onFail(String error) {
                unlockRegisterButton();
                // Tradução de mensagens do Firebase
                String lowerError = error.toLowerCase();
                if (lowerError.contains("email address is already in use")) {
                    topBanner.show("Este e-mail já está cadastrado.");
                } else if (lowerError.contains("badly formatted")) {
                    topBanner.show("O e-mail informado é inválido.");
                } else {
                    topBanner.show("Erro ao criar conta: " + error);
                }
            }
        });
    }

    private void saveUserDataToFirestore(FirebaseUser user, String name, String username, String email) {
        auth.saveUserData(user.getUid(), name, username, email, new AuthManager.AuthCallback() {
            @Override
            public void onSuccess() {
                // PASSO 4: Envia e-mail de verificação
                sendVerificationEmailAndShowModal(email);
            }

            @Override
            public void onFail(String error) {
                handleRegistrationError("Erro ao salvar perfil: " + error);
            }
        });
    }

    private void sendVerificationEmailAndShowModal(String email) {
        auth.sendEmailVerification(new AuthManager.AuthCallback() {
            @Override
            public void onSuccess() {
                unlockRegisterButton();
                showVerificationSentDialog(email);
            }

            @Override
            public void onFail(String error) {
                unlockRegisterButton();
                topBanner.show("Conta criada, mas houve erro ao enviar e-mail.");
                showVerificationSentDialog(email);
            }
        });
    }

    private void showVerificationSentDialog(String email) {
        try {
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

            txtMessage.setText("Enviamos um link de verificação para:\n\n" + email +
                    "\n\n💡 Verifique sua caixa de entrada e spam.");

            btnOk.setOnClickListener(v -> {
                dialog.dismiss();
                navigateToLogin();
            });

            btnResend.setOnClickListener(v -> resendVerificationEmail());
        } catch (Exception e) {
            navigateToLogin();
        }
    }

    private void resendVerificationEmail() {
        topBanner.show("Reenviando...");
        auth.sendEmailVerification(new AuthManager.AuthCallback() {
            @Override
            public void onSuccess() {
                topBanner.show("E-mail reenviado com sucesso!");
            }

            @Override
            public void onFail(String error) {
                topBanner.show("Falha ao reenviar.");
            }
        });
    }

    private void handleRegistrationError(String error) {
        unlockRegisterButton();
        if (topBanner != null) topBanner.show(error);
    }

    private void navigateToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void lockRegisterButton() {
        registerButton.setEnabled(false);
        registerButton.setText("Processando...");
        registerButton.setAlpha(0.6f);
    }

    private void unlockRegisterButton() {
        registerButton.setEnabled(true);
        registerButton.setText(getString(R.string.register_btn_enter));
        registerButton.setAlpha(1f);
    }
}