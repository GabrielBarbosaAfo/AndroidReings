package br.edu.ifsudestemg.throne.screens.setting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.screens.game.ContextActivity;
import br.edu.ifsudestemg.throne.utils.animations.TopBanner;
import br.edu.ifsudestemg.throne.utils.setting.ApiKeyValidator;
import br.edu.ifsudestemg.throne.utils.animations.FeedbackUtils;
import br.edu.ifsudestemg.throne.data.SecurePrefs;

public class SettingsActivity extends AppCompatActivity {

    private Button btnSaveKey;
    private EditText inputApiKey;

    private TopBanner topBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initializeViews();
        loadLocalApiKey();
        initTopBanner();
    }

    private void initTopBanner() {
        LinearLayout bannerLayout = findViewById(R.id.top_banner);
        TextView bannerText = findViewById(R.id.top_banner_text);

        topBanner = new TopBanner(bannerLayout, bannerText, 250, 2500);
    }

    private void initializeViews() {
        btnSaveKey = findViewById(R.id.btn_key);
        inputApiKey = findViewById(R.id.input_key);

        btnSaveKey.setOnClickListener(v -> saveApiKey());
    }

    private void loadLocalApiKey() {
        try {
            SecurePrefs prefs = new SecurePrefs(this);
            String apiKey = prefs.getApiKey();

            if (apiKey != null) {
                inputApiKey.setText(apiKey);
            }

        } catch (Exception ignored) {}
    }

    private void saveApiKey() {

        FeedbackUtils.playClickFeedback(this);

        String apiKey = inputApiKey.getText().toString().trim();
        if (apiKey.isEmpty()) {
            topBanner.show(getString(R.string.msg_empty_key));
            return;
        }

        lockKeyButton();

        new ApiKeyValidator().validateKey(apiKey, (isValid, message) -> {

            unlockKeyButton();

            if (isValid) {
                saveApiKeyLocally(apiKey);
                topBanner.show(getString(R.string.msg_valid_key));

                startActivity(new Intent(this, ContextActivity.class));
                finish();

            } else {
                topBanner.show(getString(R.string.msg_invalid_key));
            }
        });
    }

    private void saveApiKeyLocally(String apiKey) {
        try {
            SecurePrefs prefs = new SecurePrefs(this);
            prefs.saveApiKey(apiKey);
        } catch (Exception ignored) {}
    }

    private void lockKeyButton() {
        btnSaveKey.setEnabled(false);
        btnSaveKey.setText(getString(R.string.msg_validating));
        btnSaveKey.setAlpha(0.6f);
    }

    private void unlockKeyButton() {
        btnSaveKey.setEnabled(true);
        btnSaveKey.setText(getString(R.string.btn_key_save));
        btnSaveKey.setAlpha(1f);
    }
}
