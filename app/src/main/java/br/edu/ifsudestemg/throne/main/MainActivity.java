package br.edu.ifsudestemg.throne.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import br.edu.ifsudestemg.throne.screens.ContextActivity;
import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.utils.FeedbackUtils;

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
        startButton.setOnClickListener(v -> {
            FeedbackUtils.playClickFeedback(this);
            startActivity(new Intent(this, ContextActivity.class));
        });
    }
}