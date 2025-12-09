package br.edu.ifsudestemg.throne.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.model.CardData;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private final List<CardData> cards;
    private final Set<Integer> revealedIndices = new HashSet<>();

    public CardAdapter(List<CardData> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public void addCard(CardData card) {
        cards.add(card);
        notifyItemInserted(cards.size() - 1);
    }

    public void revealCard(int position) {
        if (!revealedIndices.contains(position)) {
            revealedIndices.add(position);
            notifyItemChanged(position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_item_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CardData card = cards.get(position);

        holder.cardName.setText(card.getTitle());
        holder.cardText.setText(card.getText());
        holder.cardImage.setImageResource(card.getImageRes());

        if (revealedIndices.contains(position)) {

            holder.backgroundFull.setVisibility(View.GONE);

            holder.frameMiddle.setVisibility(View.VISIBLE);
            holder.frameTitle.setVisibility(View.VISIBLE);
            holder.frameImage.setVisibility(View.VISIBLE);
            holder.frameHistory.setVisibility(View.VISIBLE);

        } else {
            holder.backgroundFull.setVisibility(View.VISIBLE);
            holder.frameMiddle.setVisibility(View.INVISIBLE);
            holder.frameTitle.setVisibility(View.INVISIBLE);
            holder.frameImage.setVisibility(View.INVISIBLE);
            holder.frameHistory.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView backgroundFull, frameMiddle, cardImage;
        TextView cardName, cardText;
        View frameTitle, frameImage, frameHistory;

        ViewHolder(View itemView) {
            super(itemView);

            backgroundFull = itemView.findViewById(R.id.background_full);
            frameMiddle = itemView.findViewById(R.id.frame_middle);

            cardImage = itemView.findViewById(R.id.card_image);
            cardName = itemView.findViewById(R.id.card_name);
            cardText = itemView.findViewById(R.id.card_text);

            frameTitle = itemView.findViewById(R.id.frame_title);
            frameImage = itemView.findViewById(R.id.frame_image);
            frameHistory = itemView.findViewById(R.id.frame_history);
        }
    }
}
