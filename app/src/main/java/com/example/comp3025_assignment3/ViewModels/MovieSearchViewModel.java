package com.example.comp3025_assignment3.ViewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.comp3025_assignment3.Models.MovieSearch;
import com.example.comp3025_assignment3.Utils.APIUtil;
import com.example.comp3025_assignment3.Utils.MovieSearchViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieSearchViewModel extends ViewModel {

    private final MutableLiveData<List<MovieSearch>> movieSearchResults = new MutableLiveData<>();

    public LiveData<List<MovieSearch>> getMovieSearchResults() {
        return movieSearchResults;
    }

    public void searchMovies(String query) {

        APIUtil.getMovies(query, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;

                String responseData = response.body().string();

                try {
                    JSONObject json = new JSONObject(responseData);
                    if(json.has("Search")) {
                        JSONArray resultsArray = json.getJSONArray("Search");
                        List<MovieSearch> searchResults = new ArrayList<>();
                        for (int i = 0; i < resultsArray.length(); i++) {
                            JSONObject item = resultsArray.getJSONObject(i);
                            MovieSearch movie = new MovieSearch();
                            movie.setTitle(item.getString("Title"));
                            movie.setYear(item.getString("Year"));
                            movie.setImdbID(item.getString("imdbID"));
                            movie.setType(item.getString("Type"));
                            movie.setPoster(item.getString("Poster"));

                            searchResults.add(movie);
                        }

                        movieSearchResults.postValue(searchResults);
                    } else {
                        // Handle case where no search results are found
                        movieSearchResults.postValue(new ArrayList<>());
                    }
                } catch (JSONException e) {
                    Log.e("MovieSearchViewModel", "JSON Parsing Error");
                }
            }
        });
    }

    public MovieSearchViewModel() {}
}
