package com.lowlevelprog.lowlevelprogrammer.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lowlevelprog.lowlevelprogrammer.Interface.ItemClickListener;
import com.lowlevelprog.lowlevelprogrammer.R;

public class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView nameTextView, scoreTextView;
    private ItemClickListener itemClickListener;

    public RankingViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.ranking_lay_text_name);
        scoreTextView = itemView.findViewById(R.id.ranking_lay_text_score);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onItemClick(view, getAdapterPosition());
    }
}
