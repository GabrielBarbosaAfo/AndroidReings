package br.edu.ifsudestemg.throne.screens;

import android.content.Intent;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import java.util.Objects;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.data.GameStorage;
import br.edu.ifsudestemg.throne.model.GameContext;
import br.edu.ifsudestemg.throne.utils.FeedbackUtils;
import br.edu.ifsudestemg.throne.utils.TopBanner;

public class ContextActivity extends AppCompatActivity {

    private EditText editContext;
    private TopBanner topBanner;
    private GameStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);

        storage = new GameStorage(this);

        initViews();

        if (storage.loadContext() != null) {
            askContinueOrNewGame();
        }
    }

    private void initViews() {

        Button btnContext = findViewById(R.id.btn_context);
        editContext = findViewById(R.id.edit_context);

        LinearLayout bannerLayout = findViewById(R.id.top_banner);
        TextView bannerText = findViewById(R.id.top_banner_text);

        topBanner = new TopBanner(bannerLayout, bannerText, 250, 2500);

        btnContext.setOnClickListener(v -> {
            FeedbackUtils.playClickFeedback(this);

            String context = editContext.getText().toString().trim();

            if (context.isEmpty()) {
                topBanner.show(getString(R.string.msg_empty_context));
                return;
            }

            goToGameWithNewContext(context);
        });
    }

    private void askContinueOrNewGame() {

        final android.app.Dialog dialog = new android.app.Dialog(this);
        dialog.setContentView(R.layout.layout_dialog_continue_game);
        dialog.setCancelable(false);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        final View overlay = findViewById(R.id.blur_overlay);
        final View rootContent = findViewById(android.R.id.content);

        Button btnContinue = dialog.findViewById(R.id.btn_continue);
        Button btnNew      = dialog.findViewById(R.id.btn_new);

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
                    Log.d("ContextActivity", "Erro ao aplicar blur: " + t.getMessage());
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
                    Log.d("ContextActivity", "Erro ao remover blur: " + t.getMessage());
                }
            }
        };

        btnContinue.setOnClickListener(v -> {
            GameContext ctx = storage.loadContext();

            Intent intent = new Intent(this, GameActivityNative.class);
            intent.putExtra("USER_CONTEXT", ctx.getUserContext());
            startActivity(intent);

            dialog.dismiss();
        });

        btnNew.setOnClickListener(v -> {
            storage.reset();
            dialog.dismiss();
        });

        dialog.setOnShowListener(d -> showBackdrop.run());
        dialog.setOnDismissListener(d -> hideBackdrop.run());

        dialog.show();
    }

    private void goToGameWithNewContext(String context) {

        storage.reset();
        storage.saveContext(new GameContext(context));

        Intent intent = new Intent(this, GameActivityNative.class);
        intent.putExtra("USER_CONTEXT", context);
        startActivity(intent);
    }
}