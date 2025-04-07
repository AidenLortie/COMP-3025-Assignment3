package com.example.comp3025_assignment3.ViewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.comp3025_assignment3.Models.Movie;
import com.example.comp3025_assignment3.Utils.APIUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieViewModel extends ViewModel {
    private final MutableLiveData<Movie> movieData = new MutableLiveData<Movie>();
    Movie movie = new Movie();

    public LiveData<Movie> getMovieData() {
        return movieData;
    }

    public void GetMovie(String movieId){
        // Define the callback for the API request
        APIUtil.getMovie(movieId, new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // ensure response body is not null
                assert response.body() != null;
                String responseData = response.body().string();

                JSONObject json = null;
                try{
                    // parse JSON response
                    json = new JSONObject(responseData);
                    movie.setTitle(json.getString("Title"));
                    movie.setYear(json.getString("Year"));
                    movie.setRated(json.getString("Rated"));
                    movie.setReleased(json.getString("Released"));
                    movie.setGenre(json.getString("Genre"));
                    movie.setPlot(json.getString("Plot"));
                    movie.setPoster(json.getString("Poster"));
                    // Set movie data
                    movieData.postValue(movie);
                } catch(JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {}
        });
    }
}
