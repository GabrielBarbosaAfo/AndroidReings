package br.edu.ifsudestemg.throne.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.utils.FeedbackUtils;
import br.edu.ifsudestemg.throne.utils.TopBanner;

public class ContextActivity extends AppCompatActivity {

    private EditText editContext;
    private TopBanner topBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);
        initViews();
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

            goToGame();
        });
    }

    private void goToGame() {
        String context = editContext.getText().toString().trim();
        Intent intent = new Intent(this, GameActivityNative.class);
        intent.putExtra("USER_CONTEXT", context);
        startActivity(intent);
    }
}