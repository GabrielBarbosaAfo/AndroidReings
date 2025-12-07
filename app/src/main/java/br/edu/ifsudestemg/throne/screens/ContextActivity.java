package br.edu.ifsudestemg.throne.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.utils.FeedbackUtils;

public class ContextActivity extends AppCompatActivity {

    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);
        initViews();
    }

    private void initViews() {
        startButton = findViewById(R.id.btn_start);
        startButton.setOnClickListener(v -> {
            FeedbackUtils.playClickFeedback(this);
            goToGame();
        });
    }

    private void goToGame() {
        startActivity(new Intent(this, GameActivityNative.class));
    }
}