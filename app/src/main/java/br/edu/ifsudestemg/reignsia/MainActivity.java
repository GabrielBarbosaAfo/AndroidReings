package br.edu.ifsudestemg.reignsia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    public void init() {
        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(v -> startActivity(new Intent(this, ContextActivity.class)));
    }
}