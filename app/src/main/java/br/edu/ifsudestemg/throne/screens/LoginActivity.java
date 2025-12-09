package br.edu.ifsudestemg.throne.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.utils.FeedbackUtils;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
    public void init() {
        Button btnGoogle = findViewById(R.id.btn_google);
        btnGoogle.setOnClickListener(v -> {
            FeedbackUtils.playClickFeedback(this);
            startActivity(new Intent(this, SettingsActivity.class));
        });
    }
}