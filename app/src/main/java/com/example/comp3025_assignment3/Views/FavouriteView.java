package com.example.comp3025_assignment3.Views;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.comp3025_assignment3.Models.Movie;
import com.example.comp3025_assignment3.Utils.FirestoreCallback;
import com.example.comp3025_assignment3.Utils.FirestoreUtil;
import com.example.comp3025_assignment3.Utils.ImageDownloader;
import com.example.comp3025_assignment3.ViewModels.MovieViewModel;
import com.example.comp3025_assignment3.databinding.FavoriteDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;

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


        FirestoreUtil fsUtil = new FirestoreUtil();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        MovieViewModel viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.GetMovie(movieId);

        viewModel.getMovieData().observe(this, result -> {
            movie = result;
            ImageDownloader.loadImageFromUrl(binding.imageView2, movie.getPoster());
            binding.favoriteDetailsTitle.setText(movie.getTitle());

            fsUtil.getDesc(mAuth.getCurrentUser().getUid(), movieId, new FirestoreCallback<String>() {
                @Override
                public void onCallback(String data) {
                    binding.favoriteDetailsDescription.setText(data);
                }
            });
        });

        binding.favoriteDetailsBack.setOnClickListener(view -> finish());

        binding.favoriteDetailsDelete.setOnClickListener(view -> {
            fsUtil.deleteFavourite(mAuth.getCurrentUser().getUid(), movieId);

            finish();
        });

        binding.favoriteDetailsUpdate.setOnClickListener(view -> {

            fsUtil.updateFavouriteDescription(mAuth.getCurrentUser().getUid(), movieId, binding.cronch.getText().toString());

            fsUtil.getDesc(mAuth.getCurrentUser().getUid(), movieId, new FirestoreCallback<String>() {
                @Override
                public void onCallback(String data) {
                    binding.favoriteDetailsDescription.setText(data);
                }
            });
        });
    }
}
