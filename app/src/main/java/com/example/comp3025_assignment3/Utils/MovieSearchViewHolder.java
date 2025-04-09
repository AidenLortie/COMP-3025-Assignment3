package com.example.comp3025_assignment3.Utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp3025_assignment3.R;

public class MovieSearchViewHolder extends RecyclerView.ViewHolder {

    public ItemClickListener clickListener;

    ImageView imageView;

    TextView title, description;

    public MovieSearchViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
        super(itemView);

        // Initialize the views
        imageView = itemView.findViewById(R.id.MS_imageview);
        title = itemView.findViewById(R.id.MS_title_txt);
        description = itemView.findViewById(R.id.MS_description_text);

        this.clickListener = itemClickListener;

        itemView.setOnClickListener(view ->
                clickListener.onClick(view, getAdapterPosition())
        );
    }

}
