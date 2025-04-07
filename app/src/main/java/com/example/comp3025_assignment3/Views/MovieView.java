package com.example.comp3025_assignment3.Views;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.comp3025_assignment3.Models.Movie;
import com.example.comp3025_assignment3.Utils.ImageDownloader;
import com.example.comp3025_assignment3.ViewModels.MovieViewModel;
import com.example.comp3025_assignment3.databinding.SearchDetailsBinding;

public class MovieView extends AppCompatActivity {
    Movie movie;
    String movieId;
    SearchDetailsBinding binding;

    @Override
    protected void onStart(){
        super.onStart();
        binding = SearchDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intObj = getIntent();
        movieId = intObj.getStringExtra("MOVIE_ID");

        MovieViewModel viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.GetMovie(movieId);

        viewModel.getMovieData().observe(this, result -> {
            movie = result;
            ImageDownloader.loadImageFromUrl(binding.imageView, movie.getPoster());
            binding.searchDetailsTitle.setText(movie.getTitle());
            binding.searchDetailsYear.setText(movie.getYear());
            binding.searchDetailsGenre.setText(movie.getGenre());
            binding.searchDetailsRating.setText(movie.getRated());
            binding.searchDetailsPlot.setText(movie.getPlot());
        });

        binding.searchDetailsBack.setOnClickListener(view -> finish());

        //Change this to add to favs once set up
        binding.add2FavBtn.setOnClickListener(view -> finish());
    }
}
