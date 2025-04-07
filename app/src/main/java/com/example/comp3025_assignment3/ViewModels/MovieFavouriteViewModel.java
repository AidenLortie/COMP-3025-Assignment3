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

    public LiveData<List<Movie>> getMovieFavouriteResults() {
        return movieFavouritesResults;
    }

    public MovieFavouriteViewModel() {
        FirestoreUtil fsUtil = new FirestoreUtil();
        String UID = mAuth.getCurrentUser().getUid();

        fsUtil.getFavourites(UID, new FirestoreCallback<List<Movie>>() {
            @Override
            public void onCallback(List<Movie> data) {
                movieFavouritesResults.postValue(data);
            }
        });
    }
}
