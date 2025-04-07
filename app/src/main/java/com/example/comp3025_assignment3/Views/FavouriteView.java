package com.example.comp3025_assignment3.Views;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.comp3025_assignment3.Models.Movie;
import com.example.comp3025_assignment3.Utils.ImageDownloader;
import com.example.comp3025_assignment3.ViewModels.MovieViewModel;
import com.example.comp3025_assignment3.databinding.FavoriteDetailsBinding;

public class FavouriteView extends AppCompatActivity {
    Movie movie;
    String movieId;
    FavoriteDetailsBinding binding;

    @Override
    protected void onStart(){
        super.onStart();
        binding = FavoriteDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intObj = getIntent();
        movieId = intObj.getStringExtra("MOVIE_ID");

        MovieViewModel viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.GetMovie(movieId);

        viewModel.getMovieData().observe(this, result -> {
            movie = result;
            ImageDownloader.loadImageFromUrl(binding.imageView2, movie.getPoster());
            binding.favoriteDetailsTitle.setText(movie.getTitle());

            //details here i gues
        });

        binding.favoriteDetailsBack.setOnClickListener(view -> finish());

        binding.favoriteDetailsDelete.setOnClickListener(view -> finish());

        binding.favoriteDetailsDelete.setOnClickListener(view -> finish());
    }
}
