package br.edu.ifsudestemg.throne.screens.game;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.data.GameStorage;
import br.edu.ifsudestemg.throne.data.SecurePrefs;
import br.edu.ifsudestemg.throne.narrative.KingdomState;
import br.edu.ifsudestemg.throne.narrative.NarrativeEngine;
import br.edu.ifsudestemg.throne.narrative.NarrativeGenerationContext;
import br.edu.ifsudestemg.throne.utils.animations.FeedbackUtils;
import br.edu.ifsudestemg.throne.utils.animations.TopBanner;
import br.edu.ifsudestemg.throne.utils.controllers.GameMenuController;

public class ContextActivity extends AppCompatActivity {

    private static final String TAG = "ContextActivity";

    private EditText editContext;
    private TopBanner topBanner;
    private GameStorage storage;
    private SecurePrefs securePrefs;

    private ProgressBar loadingSpinner;
    private View loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);

        storage = new GameStorage(this);

        try {
            securePrefs = new SecurePrefs(this);
        } catch (GeneralSecurityException | IOException e) {
            Log.e(TAG, "Erro ao inicializar SecurePrefs", e);
            return;
        }

        View root = findViewById(android.R.id.content);
        new GameMenuController(root);

        initViews();

        if (storage.hasActiveGame())
            askContinueOrNewGame();
    }

    private void initViews() {

        Button btnContext = findViewById(R.id.btn_context);
        editContext = findViewById(R.id.edit_context);

        LinearLayout bannerLayout = findViewById(R.id.top_banner);
        TextView bannerText = findViewById(R.id.top_banner_text);
        topBanner = new TopBanner(bannerLayout, bannerText, 250, 2500);

        loadingOverlay = findViewById(R.id.loading_overlay);
        loadingSpinner = findViewById(R.id.loading_spinner);

        btnContext.setOnClickListener(v -> {
            FeedbackUtils.playClickFeedback(this);
            String context = editContext.getText().toString().trim();

            if (context.isEmpty()) {
                topBanner.show(getString(R.string.msg_empty_context));
                return;
            }

            startGameWithNewContext(context);
        });
    }

    private void startGameWithNewContext(String context) {

        if (!securePrefs.hasApiKey()) {
            Toast.makeText(this, "Chave da IA não configurada!", Toast.LENGTH_LONG).show();
            return;
        }

        showLoading(true);

        new Thread(() -> {

            KingdomState initialState = new KingdomState(50, 50, 50, 50);
            storage.saveKingdomState(initialState);
            storage.saveUserContext(context);

            NarrativeGenerationContext genContext = new NarrativeGenerationContext(
                    context,
                    initialState,
                    ""
            );

            NarrativeEngine engine = new NarrativeEngine(storage, securePrefs.getApiKey());
            boolean success = engine.generateTree(genContext);

            new Handler(Looper.getMainLooper()).post(() -> {
                showLoading(false);

                if (success) {

                    Intent intent = new Intent(this, GameActivityNative.class);
                    intent.putExtra("USER_CONTEXT", context);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Falha ao gerar o reino. Tente novamente.", Toast.LENGTH_LONG).show();
                    storage.clear();
                }
            });
        }).start();
    }

    private void showLoading(boolean show) {

        if (loadingOverlay != null)
            loadingOverlay.setVisibility(show ? View.VISIBLE : View.GONE);

        if (loadingSpinner != null)
            loadingSpinner.setVisibility(show ? View.VISIBLE : View.GONE);

        Button btn = findViewById(R.id.btn_context);

        if (btn != null)
            btn.setEnabled(!show);
    }

    private void askContinueOrNewGame() {

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_continue_game);
        dialog.setCancelable(false);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        final View overlay = findViewById(R.id.blur_overlay);
        final View rootContent = findViewById(android.R.id.content);

        Button btnContinue = dialog.findViewById(R.id.btn_continue);
        Button btnNew = dialog.findViewById(R.id.btn_new);

        ViewCompat.setBackgroundTintList(btnContinue, null);
        ViewCompat.setBackgroundTintList(btnNew, null);

        Runnable showBackdrop = () -> {

            if (overlay != null) {
                overlay.setAlpha(0f);
                overlay.setVisibility(View.VISIBLE);
                overlay.animate().alpha(1f).setDuration(220).start();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                try {
                    float radius = 18f;
                    RenderEffect blur = RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.CLAMP);
                    rootContent.setRenderEffect(blur);
                } catch (Throwable t) {
                    Log.d(TAG, "Erro ao aplicar blur: " + t.getMessage());
                }
            }
        };

        Runnable hideBackdrop = () -> {

            if (overlay != null) {
                overlay.animate().alpha(0f).setDuration(200)
                        .withEndAction(() -> overlay.setVisibility(View.GONE))
                        .start();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                try {
                    rootContent.setRenderEffect(null);
                } catch (Throwable t) {
                    Log.d(TAG, "Erro ao remover blur: " + t.getMessage());
                }
            }
        };

        btnContinue.setOnClickListener(v -> {
            String userContext = storage.loadUserContext();
            Intent intent = new Intent(this, GameActivityNative.class);
            intent.putExtra("USER_CONTEXT", userContext);
            startActivity(intent);
            finish();
            dialog.dismiss();
        });

        btnNew.setOnClickListener(v -> {
            storage.clear();
            dialog.dismiss();
        });

        dialog.setOnShowListener(d -> showBackdrop.run());
        dialog.setOnDismissListener(d -> hideBackdrop.run());
        dialog.show();
    }
}