package com.example.comp3025_assignment3.Views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comp3025_assignment3.Models.MovieSearch;
import com.example.comp3025_assignment3.Utils.ItemClickListener;
import com.example.comp3025_assignment3.Utils.MovieSearchAdapter;
import com.example.comp3025_assignment3.ViewModels.MovieSearchViewModel;
import com.example.comp3025_assignment3.databinding.MovieSearchBinding;

import java.util.List;

public class MovieSearchView extends AppCompatActivity implements ItemClickListener {

    List<MovieSearch> movies;
    MovieSearchBinding binding;
    MovieSearchAdapter adapter;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MovieSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recycler.setLayoutManager(layoutManager);

        MovieSearchViewModel viewModel = new ViewModelProvider(this).get(MovieSearchViewModel.class);

        viewModel.getMovieSearchResults().observe(this, searchResults -> {
            movies = searchResults;
            adapter = new MovieSearchAdapter(this, movies);
            adapter.setClickListener(this);
            binding.recycler.setAdapter(adapter);
        });

        binding.button.setOnClickListener(view -> {
            String search = binding.txtSearch.getText().toString();

            Log.println(Log.DEBUG, "Button Clicked", "Button Clicked" + search);
            if(search.isEmpty()){
                Toast.makeText(getApplicationContext(), "No Search value provided", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.searchMovies(search);
            }
        });

    }


    @Override
    public void onClick(View view, int pos) {
        // Handle item click
        MovieSearch movie = movies.get(pos);
        String title = movie.getTitle();
        String year = movie.getYear();
        String poster = movie.getPoster();

        // Show a toast message with the movie details
        Toast.makeText(this, "Title: " + title + "\nYear: " + year + "\nPoster: " + poster, Toast.LENGTH_SHORT).show();

        // You can also start a new activity or perform any other action here
    }
}
