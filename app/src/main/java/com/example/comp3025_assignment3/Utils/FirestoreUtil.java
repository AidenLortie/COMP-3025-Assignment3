package com.example.comp3025_assignment3.Utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.comp3025_assignment3.Models.Movie;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FirestoreUtil {

    // Firestore instance
    FirebaseFirestore db;

    // Constructor
    public FirestoreUtil() {
        // I would have made everything static but Firebase complains of potential memory leaks sooo not doing that.
        db = FirebaseFirestore.getInstance();
    }

    // Add a favourite movie to Firestore
    public void addFavourite(String userId, String movieId) {
        String docId = userId + "_" + movieId;

        //Create a map to hold the data
        Map<String, Object> favData = new HashMap<>();
        favData.put("userId", userId);
        favData.put("movieId", movieId);
        favData.put("timestamp", FieldValue.serverTimestamp());

        // Add the data to Firestore
        db.collection("favourites")
                .document(docId)
                .set(favData)
                .addOnSuccessListener(aVoid -> Log.d("FirestoreUtil", "Favourite added successfully"))
                .addOnFailureListener(e -> Log.w("FirestoreUtil", "Error adding favourite", e));
    }

    // Update the favourite movie in Firestore
    public void updateFavouriteDescription(String userId, String movieId, String description){
        String docId = userId + "_" + movieId;

        // Save the description in a map
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("description", description);


        // Update the data in Firestore
        db.collection("favourites")
                .document(docId)
                .update(updateData)
                .addOnSuccessListener(aVoid -> Log.d("FirestoreUtil", "Favourite updated successfully"))
                .addOnFailureListener(e -> Log.w("FirestoreUtil", "Error updating favourite", e));
    }

    // Delete a favourite movie from Firestore
    public void deleteFavourite(String userId, String movieId) {
        String docId = userId + "_" + movieId;

        db.collection("favourites")
                .document(docId)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("FirestoreUtil", "Favourite deleted successfully"))
                .addOnFailureListener(e -> Log.w("FirestoreUtil", "Error deleting favourite", e));
    }

    // Get all favourite movies for a user
    public void getFavourites(String userId, FirestoreCallback<List<Movie>> callback) {

        // Define a list to hold the favourite movies
        ArrayList<Movie> favourites = new ArrayList<>();

        // Get the favourite movies from Firestore
        db.collection("favourites")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    // Check if the task was successful
                    if(!task.isSuccessful()){
                        Log.w("FirestoreUtil", "Error getting favourites.", task.getException());
                    }

                    // Loop through the documents in the result
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Get the movieId from the document to fetch movie details
                        APIUtil.getMovie(document.getString("movieId"), new Callback() {

                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.e("FirestoreUtil", "Error getting movie details: " + e.getMessage());
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                // ensure response body is not null
                                assert response.body() != null;

                                // parse the response
                                String responseData = response.body().string();

                                // Create a JSON object from the response
                                JSONObject json = null;
                                try{
                                    // parse JSON response
                                    json = new JSONObject(responseData);
                                    Movie movie = new Movie();
                                    movie.setImdbID(json.getString("imdbID"));
                                    movie.setTitle(json.getString("Title"));
                                    movie.setYear(json.getString("Year"));
                                    movie.setRated(json.getString("Rated"));
                                    movie.setReleased(json.getString("Released"));
                                    movie.setGenre(json.getString("Genre"));
                                    movie.setPlot(json.getString("Plot"));
                                    movie.setPoster(json.getString("Poster"));

                                    // Add the movie to the list of favourites
                                    // Use synchronized block to ensure thread safety
                                    synchronized (favourites) {
                                        // Add the movie to the list
                                        favourites.add(movie);
                                        // Check if all movies have been added
                                        if( favourites.size() == task.getResult().size()){
                                            // Call the callback with the list of favourites
                                            callback.onCallback(favourites);
                                        }
                                    }

                                } catch(JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                });
    }

    // Get a specific favourite movie for a user
    public void getFavourite(String userId, String movieId, FirestoreCallback<Movie> callback) {
        // Get the document ID (From the userId and movieId)
        String docId = userId + "_" + movieId;

        // Create a new Movie object
        Movie movie = new Movie();

        // Get the favourite movie from Firestore
        db.collection("favourites")
                .document(docId)
                .get()
                .addOnCompleteListener(task -> {
                    // Check if the task was successful
                    if (!task.isSuccessful()) {
                        Log.w("FirestoreUtil", "Error getting favourite.", task.getException());
                    } else {

                        // Get the document from the result
                        DocumentSnapshot document = task.getResult();

                        // Get the movieId from the document to fetch movie details
                        APIUtil.getMovie(document.getString("movieId"), new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.e("FirestoreUtil", "Error getting movie details: " + e.getMessage());
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                // ensure response body is not null
                                assert response.body() != null;
                                // parse the response
                                String responseData = response.body().string();
                                // Create a JSON object from the response
                                JSONObject json = null;
                                try {
                                    // parse JSON response
                                    json = new JSONObject(responseData);
                                    movie.setImdbID(json.getString("imdbID"));
                                    movie.setTitle(json.getString("Title"));
                                    movie.setYear(json.getString("Year"));
                                    movie.setRated(json.getString("Rated"));
                                    movie.setReleased(json.getString("Released"));
                                    movie.setGenre(json.getString("Genre"));
                                    movie.setPlot(json.getString("Plot"));
                                    movie.setPoster(json.getString("Poster"));

                                    // Call the callback with the movie
                                    synchronized (movie) {
                                        callback.onCallback(movie);
                                    }

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                }).addOnFailureListener(e -> {
                    Log.w("FirestoreUtil", "Error getting favourite.", e);
                });

    }

    // Get the description of a favourite movie
    public void getDesc(String userId, String movieId, FirestoreCallback<String> callback){

        // Get the document ID (From the userId and movieId)
        String docId = userId + "_" + movieId;

        // Get the favourite movie from Firestore
        db.collection("favourites")
                .document(docId)
                .get()
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
                        Log.w("FirestoreUtil", "Error getting favourite.", task.getException());
                    }

                    // Get the document from the result and call the callback
                    callback.onCallback(task.getResult().getString("description"));
                }).addOnFailureListener(e -> {
                    Log.w("FirestoreUtil", "Error getting favourite.", e);
                });
    }


}
