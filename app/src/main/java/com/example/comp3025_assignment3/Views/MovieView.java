package com.example.comp3025_assignment3.Views;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.comp3025_assignment3.Models.Movie;
import com.example.comp3025_assignment3.Utils.FirestoreUtil;
import com.example.comp3025_assignment3.Utils.ImageDownloader;
import com.example.comp3025_assignment3.ViewModels.MovieViewModel;
import com.example.comp3025_assignment3.databinding.SearchDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MovieView extends AppCompatActivity {
    Movie movie;
    String movieId;
    SearchDetailsBinding binding;

    @Override
    protected void onStart(){
        //setting binding
        super.onStart();
        binding = SearchDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //getting intent and keys
        Intent intObj = getIntent();
        movieId = intObj.getStringExtra("MOVIE_ID");

        //setting up connection to view model
        MovieViewModel viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.GetMovie(movieId);

        //filling xml with info
        viewModel.getMovieData().observe(this, result -> {
            movie = result;
            ImageDownloader.loadImageFromUrl(binding.imageView, movie.getPoster());
            binding.searchDetailsTitle.setText(movie.getTitle());
            binding.searchDetailsYear.setText(movie.getYear());
            binding.searchDetailsGenre.setText(movie.getGenre());
            binding.searchDetailsRating.setText(movie.getRated());
            binding.searchDetailsPlot.setText(movie.getPlot());
        });

        //back button
        binding.searchDetailsBack.setOnClickListener(view -> finish());

        //add to favourite button
        binding.add2FavBtn.setOnClickListener(view -> {
            //toast to let them know it has been added
            Toast.makeText(this, "Added to Favourites!",Toast.LENGTH_SHORT).show();

            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirestoreUtil fsUtil = new FirestoreUtil();
            fsUtil.addFavourite(mAuth.getCurrentUser().getUid(), movieId);
        });
    }
}
