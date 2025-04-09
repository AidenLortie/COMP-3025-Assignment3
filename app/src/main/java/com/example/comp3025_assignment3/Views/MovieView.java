package com.example.comp3025_assignment3.Views;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.comp3025_assignment3.Models.Movie;
import com.example.comp3025_assignment3.Utils.FirestoreCallback;
import com.example.comp3025_assignment3.Utils.FirestoreUtil;
import com.example.comp3025_assignment3.Utils.ImageDownloader;
import com.example.comp3025_assignment3.ViewModels.MovieViewModel;
import com.example.comp3025_assignment3.databinding.SearchDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MovieView extends AppCompatActivity {
    Movie movie;
    String movieId;
    SearchDetailsBinding binding;

    FirestoreUtil fsUtil = new FirestoreUtil();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

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

            //adding to firestore
            fsUtil.addFavourite(mAuth.getCurrentUser().getUid(), movieId);

            //disabling the button
            binding.add2FavBtn.setEnabled(false);
            binding.add2FavBtn.setText("Already in Favourites");
        });

        // Check if the movie is already in the favourites

        fsUtil.isFavourited(mAuth.getCurrentUser().getUid(), movieId, new FirestoreCallback<Boolean>() {
            @Override
            public void onCallback(Boolean data) {
                if(data){
                    binding.add2FavBtn.setEnabled(false);
                    binding.add2FavBtn.setText("Already in Favourites");
                }else{
                    binding.add2FavBtn.setEnabled(true);
                    binding.add2FavBtn.setText("Add to Favourites");
                }
            }
        });


    }
}
