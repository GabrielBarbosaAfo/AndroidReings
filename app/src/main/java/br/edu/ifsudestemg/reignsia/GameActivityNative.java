package br.edu.ifsudestemg.reignsia;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.Arrays;
import java.util.List;

public class GameActivityNative extends AppCompatActivity {

    private CardStackView cardStackView;
    private List<String> mockCards = Arrays.asList(
            "O tesouro real foi roubado. Prender o suspeito?",
            "Um profeta prevê fome. Aumentar impostos?",
            "Um dragão aparece. Mandar o exército?",
            "Um viajante oferece tecnologia futura. Aceitar?",
            "O povo pede festa. Gastar ouro do reino?"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_native);

        cardStackView = findViewById(R.id.card_stack_view);

        CardStackLayoutManager layoutManager = new CardStackLayoutManager(this, new CardStackListener() {

                    @Override
                    public void onCardSwiped(Direction direction) {
                        if (direction == Direction.Right) {
                            Toast.makeText(GameActivityNative.this, "Sim", Toast.LENGTH_SHORT).show();
                        } else if (direction == Direction.Left) {
                            Toast.makeText(GameActivityNative.this, "Não", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCardDragging(Direction direction, float ratio) {}

                    @Override
                    public void onCardRewound() {}

                    @Override
                    public void onCardCanceled() {}

                    @Override
                    public void onCardAppeared(View view, int position) {}

                    @Override
                    public void onCardDisappeared(View view, int position) {}
                });

        cardStackView.setLayoutManager(layoutManager);
        cardStackView.setAdapter(new CardAdapter(mockCards));
    }
}
