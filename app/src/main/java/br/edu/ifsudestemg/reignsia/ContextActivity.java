package br.edu.ifsudestemg.reignsia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ContextActivity extends AppCompatActivity {

    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);
        initViews();
        setupClick();
    }

    private void initViews() {
        startButton = findViewById(R.id.btn_start);
    }

    private void setupClick() {
        startButton.setOnClickListener(v -> goToGame());
    }

    private void goToGame() {
        startActivity(new Intent(this, GameActivityNative.class));
    }
}