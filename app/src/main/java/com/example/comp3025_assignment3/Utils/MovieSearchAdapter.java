package com.example.comp3025_assignment3.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp3025_assignment3.Models.MovieSearch;
import com.example.comp3025_assignment3.R;
import com.example.comp3025_assignment3.ViewModels.MovieSearchViewModel;

import java.util.List;

public class MovieSearchAdapter extends RecyclerView.Adapter<MovieSearchViewHolder>{

    List<MovieSearch> movies;
    Context context;
    ItemClickListener clickListener;

    public MovieSearchAdapter(Context context, List<MovieSearch> movies){
        this.context = context;
        this.movies = movies;
    }

    public void setClickListener(ItemClickListener listener){
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public MovieSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_search_layout, parent, false);
        return new MovieSearchViewHolder(itemView, this.clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieSearchViewHolder holder, int position) {
        MovieSearch movie = movies.get(position);

        holder.title.setText(movie.getTitle());
        holder.description.setText(movie.getYear());
        ImageDownloader.loadImageFromUrl(holder.imageView, movie.getPoster());

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
