package com.example.comp3025_assignment3.Models;

import androidx.annotation.NonNull;

public class Movie {
    // Movie Class (Used by MovieView)
    private String title;
    private String year;
    private String rated;
    private String released;
    private String genre;
    private String plot;
    private String poster;

    public Movie(){

    }

    @NonNull
    @Override
    public String toString() {
        return "Summary: \n" + plot + "\nGenre: " + genre + "\nRated: " + rated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
