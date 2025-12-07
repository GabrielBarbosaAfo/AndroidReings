package br.edu.ifsudestemg.throne.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.edu.ifsudestemg.throne.R;
import br.edu.ifsudestemg.throne.model.CardData;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private final List<CardData> cards;
    private final Set<Integer> revealedIndices = new HashSet<>();

    public CardAdapter(List<CardData> cards) {
        this.cards = cards;
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
                .inflate(R.layout.item_card, parent, false);

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
            holder.molduraTitulo.setVisibility(View.VISIBLE);
            holder.molduraImagem.setVisibility(View.VISIBLE);
            holder.molduraHistoria.setVisibility(View.VISIBLE);

        } else {
            holder.backgroundFull.setVisibility(View.VISIBLE);
            holder.frameMiddle.setVisibility(View.INVISIBLE);
            holder.molduraTitulo.setVisibility(View.INVISIBLE);
            holder.molduraImagem.setVisibility(View.INVISIBLE);
            holder.molduraHistoria.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView backgroundFull, frameMiddle, cardImage;
        TextView cardName, cardText;
        View molduraTitulo, molduraImagem, molduraHistoria;

        ViewHolder(View itemView) {
            super(itemView);

            backgroundFull = itemView.findViewById(R.id.background_full);
            frameMiddle = itemView.findViewById(R.id.frame_middle);

            cardImage = itemView.findViewById(R.id.card_image);
            cardName = itemView.findViewById(R.id.card_name);
            cardText = itemView.findViewById(R.id.card_text);

            molduraTitulo = itemView.findViewById(R.id.moldura_titulo);
            molduraImagem = itemView.findViewById(R.id.moldura_imagem);
            molduraHistoria = itemView.findViewById(R.id.moldura_historia);
        }
    }
}
