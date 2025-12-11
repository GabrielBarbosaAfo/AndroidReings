package br.edu.ifsudestemg.throne.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.utils.ApiKeyValidator;
import br.edu.ifsudestemg.throne.utils.FeedbackUtils;
import br.edu.ifsudestemg.throne.utils.SecurePrefs;

public class SettingsActivity extends AppCompatActivity {

    private static final int BANNER_DURATION = 2500;
    private static final int BANNER_ANIM = 250;

    private Button btnKey;
    private EditText inputKey;
    private LinearLayout topBanner;
    private TextView topBannerText;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // 1. Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        init();

        // 2. Tentar buscar a chave assim que a tela abre
        carregarChaveDoFirebase();
    }

    private void init() {
        btnKey = findViewById(R.id.btn_key);
        inputKey = findViewById(R.id.input_key);
        topBanner = findViewById(R.id.top_banner);
        topBannerText = findViewById(R.id.top_banner_text);

        btnKey.setOnClickListener(v -> {
            FeedbackUtils.playClickFeedback(this);

            String key = inputKey.getText().toString().trim();

            if (key.isEmpty()) {
                showTopBanner(getString(R.string.msg_empty_key));
                return;
            }

            validateKey(key);
        });
    }

    // --- NOVA FUNÇÃO: Busca a chave no banco e preenche o campo ---
    private void carregarChaveDoFirebase() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // Desativa o campo enquanto carrega (opcional, mas elegante)
            inputKey.setEnabled(false);
            inputKey.setHint("Buscando chave no reino...");

            db.collection("users").document(user.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        inputKey.setEnabled(true);
                        inputKey.setHint(getString(R.string.key_gemini)); // Restaura o hint original

                        if (documentSnapshot.exists()) {
                            String apiKeySalva = documentSnapshot.getString("apiKey");
                            // Se existir uma chave e ela não for vazia, preenche o campo
                            if (apiKeySalva != null && !apiKeySalva.isEmpty()) {
                                inputKey.setText(apiKeySalva);
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        inputKey.setEnabled(true);
                        // Se falhar (sem internet), apenas deixa o campo vazio para ele digitar
                    });
        }
    }

    private void validateKey(String apiKey) {

        btnKey.setEnabled(false);
        btnKey.setText(getString(R.string.msg_validating));

        ApiKeyValidator validator = new ApiKeyValidator();

        validator.validateKey(apiKey, (valid, message) -> {

            btnKey.setEnabled(true);
            btnKey.setText(getString(R.string.btn_key_save));

            if (valid) {
                try {
                    // 1. Salva localmente (SecurePrefs)
                    SecurePrefs prefs = new SecurePrefs(this);
                    prefs.saveApiKey(apiKey);

                    // 2. ATUALIZA NO FIREBASE TAMBÉM (Sincronia)
                    atualizarChaveNoFirebase(apiKey);

                    showTopBanner(getString(R.string.msg_valid_key));

                    // Aguarda um pouquinho para o usuário ler a mensagem antes de trocar de tela
                    new android.os.Handler().postDelayed(() -> {
                        startActivity(new Intent(this, ContextActivity.class));
                        finish();
                    }, 1000);

                } catch (Exception e) {
                    showTopBanner(getString(R.string.msg_save_error));
                }

            } else {
                showTopBanner(getString(R.string.msg_invalid_key));
            }
        });
    }

    // --- NOVA FUNÇÃO: Atualiza o banco quando o usuário salva uma nova chave ---
    private void atualizarChaveNoFirebase(String novaApiKey) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Map<String, Object> updates = new HashMap<>();
            updates.put("apiKey", novaApiKey);

            db.collection("users").document(user.getUid())
                    .update(updates);
            // O .update() é silencioso aqui, não precisamos travar a tela esperando ele
        }
    }

    private void showTopBanner(String message) {
        topBannerText.setText(message);
        topBanner.setVisibility(View.VISIBLE);

        topBanner.setTranslationY(-topBanner.getHeight());
        topBanner.animate()
                .translationY(0)
                .setDuration(BANNER_ANIM)
                .start();

        topBanner.postDelayed(() ->
                        topBanner.animate()
                                .translationY(-topBanner.getHeight())
                                .setDuration(BANNER_ANIM)
                                .withEndAction(() -> topBanner.setVisibility(View.GONE))
                                .start(),
                BANNER_DURATION
        );
    }
}