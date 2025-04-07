package com.example.comp3025_assignment3.Utils;

import com.example.comp3025_assignment3.Models.Movie;

import java.util.List;

public interface FirestoreCallback<T> {
    void onCallback(T data);
}
