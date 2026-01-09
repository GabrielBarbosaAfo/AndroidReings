package br.edu.ifsudestemg.throne.utils.controllers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import br.edu.ifsudestemg.throne.R;

public class ScorePageController {

    private final View pageScore;
    private final LinearLayout scoreListContainer;
    private final FirebaseFirestore db;

    public ScorePageController(View pageScore) {
        this.pageScore = pageScore;
        this.scoreListContainer = pageScore.findViewById(R.id.score_list);
        this.db = FirebaseFirestore.getInstance();

        startRealtimeRanking();
    }

    private void startRealtimeRanking() {

        db.collection("users")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(20)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e("RANKING", "Erro ao ouvir ranking", e);
                        return;
                    }

                    if (snapshots != null) {
                        updateList(snapshots.getDocuments());
                    }
                });
    }

    private void updateList(List<DocumentSnapshot> users) {
        scoreListContainer.removeAllViews();

        int position = 1;
        LayoutInflater inflater = LayoutInflater.from(scoreListContainer.getContext());

        for (DocumentSnapshot doc : users) {

            View itemView = inflater.inflate(R.layout.item_ranking, scoreListContainer, false);

            TextView tvPos = itemView.findViewById(R.id.rank_pos);
            TextView tvName = itemView.findViewById(R.id.rank_name);
            TextView tvScore = itemView.findViewById(R.id.rank_score);

            String name = doc.getString("username");
            if (name == null || name.isEmpty()) name = "Desconhecido";

            Long score = doc.getLong("score");
            if (score == null) score = 0L;

            tvPos.setText(position + "º");
            tvName.setText(name);
            tvScore.setText(score + " pts");

            if (position == 1) tvPos.setTextColor(0xFFFFD700); // Dourado
            else if (position == 2) tvPos.setTextColor(0xFFC0C0C0); // Prata
            else if (position == 3) tvPos.setTextColor(0xFFCD7F32); // Bronze
            else tvPos.setTextColor(0xFFFFFFFF); // Branco

            scoreListContainer.addView(itemView);
            position++;
        }
    }
}