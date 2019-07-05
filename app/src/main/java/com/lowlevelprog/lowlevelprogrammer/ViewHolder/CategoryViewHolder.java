package com.lowlevelprog.lowlevelprogrammer.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lowlevelprog.lowlevelprogrammer.Interface.ItemClickListener;
import com.lowlevelprog.lowlevelprogrammer.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView categoryName;
    public ImageView categoryImage;

    private ItemClickListener icl;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        categoryName = itemView.findViewById(R.id.text_categories);
        categoryImage = itemView.findViewById(R.id.image_categories);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        icl.onClick(view, getAdapterPosition(), false);
    }

    public void setIcl(ItemClickListener icl){
        this.icl = icl;
    }
}
