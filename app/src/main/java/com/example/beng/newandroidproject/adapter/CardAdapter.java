package com.example.beng.newandroidproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.beng.newandroidproject.entity.Card;
import com.example.beng.newandroidproject.interfaces.CardAdapterInterface;
import com.example.beng.newandroidproject.R;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<Card> cards;
    private Context context;
    private CardAdapterInterface cardAdapterInterface;

    public CardAdapter(List<Card> cards, Context context, CardAdapterInterface cardAdapterInterface) {
        this.cards = cards;
        this.context = context;
        this.cardAdapterInterface = cardAdapterInterface;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card_item, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, final int position) {
        switch (cards.get(position).getType()){
            case 1:
                holder.textCard.setBackgroundResource(R.drawable.bg_clubs);
                break;
            case 2:
                holder.textCard.setBackgroundResource(R.drawable.bg_diamonds);
                break;
            case 3:
                holder.textCard.setBackgroundResource(R.drawable.bg_heart);
                break;
            case 4:
                holder.textCard.setBackgroundResource(R.drawable.bg_spade);
                break;
                default:
                    break;
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardAdapterInterface.cardSelected(position);
            }
        });
        holder.textCard.setText(String.valueOf(cards.get(position).getValue()));
        if(cards.get(position).isClicked()){
            holder.linearLayout.setBackgroundResource(R.drawable.border);
        }else {
            holder.linearLayout.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{
        private TextView textCard;
        private LinearLayout linearLayout;

        public CardViewHolder(View view){
            super(view);
            textCard = view.findViewById(R.id.card_text_view);
            linearLayout = view.findViewById(R.id.card_frame_linear);
        }

    }
}
