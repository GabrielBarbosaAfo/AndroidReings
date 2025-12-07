package br.edu.ifsudestemg.throne.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
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

    private void validateKey(String apiKey) {

        btnKey.setEnabled(false);
        btnKey.setText(getString(R.string.msg_validating));

        ApiKeyValidator validator = new ApiKeyValidator();

        validator.validateKey(apiKey, (valid, message) -> {

            btnKey.setEnabled(true);
            btnKey.setText(getString(R.string.btn_key_save));

            if (valid) {
                try {
                    SecurePrefs prefs = new SecurePrefs(this);
                    prefs.saveApiKey(apiKey);

                    showTopBanner(getString(R.string.msg_valid_key));

                    startActivity(new Intent(this, ContextActivity.class));
                    finish();

                } catch (Exception e) {
                    showTopBanner(getString(R.string.msg_save_error));
                }

            } else {
                showTopBanner(getString(R.string.msg_invalid_key));
            }
        });
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
