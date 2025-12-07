package br.edu.ifsudestemg.throne.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.utils.FeedbackUtils;

public class LoginActivity extends AppCompatActivity {

    private Button btnGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
    public void init() {
        btnGoogle = findViewById(R.id.btn_google);
        btnGoogle.setOnClickListener(v -> {
            FeedbackUtils.playClickFeedback(this);
            startActivity(new Intent(this, SettingsActivity.class));
        });
    }
}