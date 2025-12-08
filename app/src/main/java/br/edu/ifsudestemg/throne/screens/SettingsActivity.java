package br.edu.ifsudestemg.throne.screens;

import android.os.Bundle;
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
import br.edu.ifsudestemg.throne.utils.TopBanner;

public class SettingsActivity extends AppCompatActivity {

    private Button btnKey;
    private EditText inputKey;

    private TopBanner topBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }

    private void init() {
        btnKey = findViewById(R.id.btn_key);
        inputKey = findViewById(R.id.input_key);

        LinearLayout bannerLayout = findViewById(R.id.top_banner);
        TextView bannerText = findViewById(R.id.top_banner_text);

        topBanner = new TopBanner(bannerLayout, bannerText, 250, 2500);

        btnKey.setOnClickListener(v -> {

            FeedbackUtils.playClickFeedback(this);

            String key = inputKey.getText().toString().trim();

            if (key.isEmpty()) {
                topBanner.show(getString(R.string.msg_empty_key));
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

                    topBanner.show(getString(R.string.msg_valid_key));

                    startActivity(new Intent(this, ContextActivity.class));
                    finish();

                } catch (Exception e) {
                    topBanner.show(getString(R.string.msg_save_error));
                }

            } else {
                topBanner.show(getString(R.string.msg_invalid_key));
            }
        });
    }
}
