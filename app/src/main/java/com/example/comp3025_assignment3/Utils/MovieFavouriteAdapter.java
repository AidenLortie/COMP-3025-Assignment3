package com.example.comp3025_assignment3.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp3025_assignment3.Models.Movie;
import com.example.comp3025_assignment3.R;
import com.example.comp3025_assignment3.Views.MovieFavouriteView;
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

        // Get the movie at the current position
        Movie movie = movies.get(position);

        FirebaseAuth user = mAuth;
        String UID = user.getCurrentUser().getUid();

        // Get the description of the movie from Firestore
        fsUtil.getDesc(UID, movie.getImdbID(), new FirestoreCallback<String>() {
            @Override
            public void onCallback(String data) {
                if(data == null){
                    holder.description.setText("No Description");
                }
                else{
                    holder.description.setText(data);
                }
            }
        });

        // Set the title and image of the movie
        holder.title.setText(movie.getTitle());
        ImageDownloader.loadImageFromUrl(holder.imageView, movie.getPoster());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }
}
