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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FirestoreUtil {

    FirebaseFirestore db;

    public FirestoreUtil() {
        db = FirebaseFirestore.getInstance();
    }

    public void addFavourite(String userId, String movieId) {
        String docId = userId + "_" + movieId;

        Map<String, Object> favData = new HashMap<>();
        favData.put("userId", userId);
        favData.put("movieId", movieId);
        favData.put("timestamp", FieldValue.serverTimestamp());

        db.collection("favourites")
                .document(docId)
                .set(favData)
                .addOnSuccessListener(aVoid -> Log.d("FirestoreUtil", "Favourite added successfully"))
                .addOnFailureListener(e -> Log.w("FirestoreUtil", "Error adding favourite", e));
    }

    public void updateFavouriteDescription(String userId, String movieId, String description){
        String docId = userId + "_" + movieId;

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("description", description);


        db.collection("favourites")
                .document(docId)
                .update(updateData)
                .addOnSuccessListener(aVoid -> Log.d("FirestoreUtil", "Favourite updated successfully"))
                .addOnFailureListener(e -> Log.w("FirestoreUtil", "Error updating favourite", e));
    }

    public void deleteFavourite(String userId, String movieId) {
        String docId = userId + "_" + movieId;

        db.collection("favourites")
                .document(docId)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("FirestoreUtil", "Favourite deleted successfully"))
                .addOnFailureListener(e -> Log.w("FirestoreUtil", "Error deleting favourite", e));
    }

    public List<Movie> getFavourites(String userId) {

        List<Movie> favourites = List.of();
        db.collection("favourites")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
                        Log.w("FirestoreUtil", "Error getting favourites.", task.getException());
                    }
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        APIUtil.getMovie(document.getString("movieId"), new Callback() {

                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.e("FirestoreUtil", "Error getting movie details: " + e.getMessage());
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                assert response.body() != null;
                                String responseData = response.body().string();
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

                                    // Add movie to favourites list
                                    favourites.add(movie);
                                    return;

                                } catch(JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                });

        return favourites;
    }

    public Movie getFavourite(String userId, String movieId) {
        String docId = userId + "_" + movieId;
        Movie movie = new Movie();

        db.collection("favourites")
                .document(docId)
                .get()
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
                        Log.w("FirestoreUtil", "Error getting favourite.", task.getException());
                    }

                    DocumentSnapshot document = task.getResult();

                    APIUtil.getMovie(document.getString("movieId"), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            Log.e("FirestoreUtil", "Error getting movie details: " + e.getMessage());
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            assert response.body() != null;
                            String responseData = response.body().string();
                            JSONObject json = null;
                            try{
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

                            } catch(JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }).addOnFailureListener(e -> {
                    Log.w("FirestoreUtil", "Error getting favourite.", e);
                });

        return movie;
    }

    public String getDesc(String userId, String movieId){
        String docId = userId + "_" + movieId;
        AtomicReference<String> description = new AtomicReference<>("");

        db.collection("favourites")
                .document(docId)
                .get()
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()){
                        Log.w("FirestoreUtil", "Error getting favourite.", task.getException());
                    }

                    DocumentSnapshot document = task.getResult();
                    description.set(document.getString("description"));
                }).addOnFailureListener(e -> {
                    Log.w("FirestoreUtil", "Error getting favourite.", e);
                });

        return description.get();

    }


}
