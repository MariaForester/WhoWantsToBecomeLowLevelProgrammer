package com.lowlevelprog.lowlevelprogrammer.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lowlevelprog.lowlevelprogrammer.R;

public class RankingDetailViewHolder extends RecyclerView.ViewHolder {

    public TextView nameTextView, scoreTextView;

    public RankingDetailViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.ranking_lay_text_name_detailed);
        scoreTextView = itemView.findViewById(R.id.ranking_lay_text_score_detailed);
    }
}
