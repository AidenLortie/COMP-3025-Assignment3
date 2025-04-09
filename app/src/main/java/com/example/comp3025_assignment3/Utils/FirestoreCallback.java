package com.example.comp3025_assignment3.Utils;

import com.example.comp3025_assignment3.Models.Movie;

import java.util.List;

public interface FirestoreCallback<T> {
    // Callback method to handle the data returned from Firestore
    // This method will be called when the data is successfully retrieved
    // The data can be of any type, so we use a generic type T

    // (I did this cause I was experiencing issues with firestore / okhttp together)
    void onCallback(T data);
}
