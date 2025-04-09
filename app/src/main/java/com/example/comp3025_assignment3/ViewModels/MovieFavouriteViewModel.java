package com.example.comp3025_assignment3.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.comp3025_assignment3.Models.Movie;
import com.example.comp3025_assignment3.Utils.FirestoreCallback;
import com.example.comp3025_assignment3.Utils.FirestoreUtil;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MovieFavouriteViewModel extends ViewModel {
    private final MutableLiveData<List<Movie>> movieFavouritesResults = new MutableLiveData<>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirestoreUtil fsUtil = new FirestoreUtil();

    public LiveData<List<Movie>> getMovieFavouriteResults() {
        return movieFavouritesResults;
    }

    // Constructor to initialize the ViewModel
    public MovieFavouriteViewModel() {
        String UID = mAuth.getCurrentUser().getUid();

        fsUtil.getFavourites(UID, new FirestoreCallback<List<Movie>>() {
            @Override
            public void onCallback(List<Movie> data) {
                movieFavouritesResults.postValue(data);
            }
        });
    }

    // Method to refresh the data
    public void refreshData() {
        String UID = mAuth.getCurrentUser().getUid();

        fsUtil.getFavourites(UID, new FirestoreCallback<List<Movie>>() {
            @Override
            public void onCallback(List<Movie> data) {
                movieFavouritesResults.postValue(data);
            }
        });
    }
}
