package com.example.comp3025_assignment3.Views;

import android.content.Intent;
import android.widget.Toast;

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
        //setting binding
        binding = FavoriteDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //getting id through intents
        Intent intObj = getIntent();
        movieId = intObj.getStringExtra("MOVIE_ID");

        //firestore connection
        FirestoreUtil fsUtil = new FirestoreUtil();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //getting view model fro info
        MovieViewModel viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        viewModel.GetMovie(movieId);

        //setting xml with info from both firesotre and omdb
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

        //back button
        binding.favoriteDetailsBack.setOnClickListener(view -> finish());

        //delete from favourite button
        binding.favoriteDetailsDelete.setOnClickListener(view -> {
            fsUtil.deleteFavourite(mAuth.getCurrentUser().getUid(), movieId);

            finish();
        });

        //update description button
        binding.favoriteDetailsUpdate.setOnClickListener(view -> {
            //toast to let the user know the description was updated
            Toast.makeText(this, "Updated Description!",Toast.LENGTH_SHORT).show();
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
