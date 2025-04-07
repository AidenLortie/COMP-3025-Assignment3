package com.example.comp3025_assignment3.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp3025_assignment3.Models.Movie;
import com.example.comp3025_assignment3.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MovieFavouriteAdapter extends RecyclerView.Adapter<MovieFavouriteViewHolder> {

    List<Movie> movies;
    Context context;
    ItemClickListener clickListener;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public MovieFavouriteAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieFavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_search_layout, parent, false);
        return new MovieFavouriteViewHolder(itemView, this.clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieFavouriteViewHolder holder, int position) {
        FirestoreUtil fsUtil = new FirestoreUtil();

        Movie movie = movies.get(position);
        FirebaseAuth user = mAuth;
        String UID = user.getCurrentUser().getUid();
        String description = fsUtil.getDesc(UID, movie.getImdbID());



        holder.title.setText(movie.getTitle());
        holder.description.setText(description);
        ImageDownloader.loadImageFromUrl(holder.imageView, movie.getPoster());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
