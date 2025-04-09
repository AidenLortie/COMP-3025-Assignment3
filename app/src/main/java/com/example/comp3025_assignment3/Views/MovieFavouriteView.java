package com.example.comp3025_assignment3.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp3025_assignment3.Models.Movie;
import com.example.comp3025_assignment3.Utils.ItemClickListener;
import com.example.comp3025_assignment3.Utils.MovieFavouriteAdapter;
import com.example.comp3025_assignment3.ViewModels.MovieFavouriteViewModel;
import com.example.comp3025_assignment3.databinding.MovieFavouriteListBinding;

import java.util.List;

public class MovieFavouriteView extends AppCompatActivity implements ItemClickListener {
    List<Movie> movies;
    MovieFavouriteListBinding binding;
    MovieFavouriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MovieFavouriteListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LinearLayoutManager layoutManger = new LinearLayoutManager(this);
        binding.recycler.setLayoutManager(layoutManger);

        MovieFavouriteViewModel viewModel = new ViewModelProvider(this).get(MovieFavouriteViewModel.class);

        viewModel.getMovieFavouriteResults().observe(this, results -> {
            movies = results;
            adapter = new MovieFavouriteAdapter(this, movies);
            adapter.setClickListener(this);
            binding.recycler.setAdapter(adapter);
        });

        binding.toSrchBtn.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        // Refresh the data when the activity is resumed (e.g., after returning from the FavouriteView activity)
        MovieFavouriteViewModel viewModel = new ViewModelProvider(this).get(MovieFavouriteViewModel.class);
        viewModel.refreshData();
    }


    @Override
    public void onClick(View view, int pos) {
        Movie movie = movies.get(pos);
        String movieId = movie.getImdbID();

        // Create an intent to start the MovieView activity
        Intent intent = new Intent(this, FavouriteView.class);
        intent.putExtra("MOVIE_ID", movieId);
        startActivity(intent);
    }
}
