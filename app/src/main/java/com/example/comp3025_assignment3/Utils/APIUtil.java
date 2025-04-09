package com.example.comp3025_assignment3.Utils;


import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class APIUtil {
    // OkHttpClient instance
    private static final OkHttpClient client = new OkHttpClient();

    // Base URL for the OMDB API
    private static final String baseUrl = "https://omdbapi.com/?apikey=a3dbd1e7&type=movie";

    // getMovies - used durring search to get list of movies
    public static void getMovies(String search, Callback callback) {
        Request request = new Request.Builder()
                .url(baseUrl + "&s=" + search)
                .build();
        client.newCall(request).enqueue(callback);
    }

    // getMovie - used to get a singular movie
    public static void getMovie(String movieId, Callback callback){
        Request request = new Request.Builder()
                .url(baseUrl + "&i=" + movieId)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
