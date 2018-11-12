package com.example.mahfuzjibrahim.movieapp;

import java.util.List;

public class MovieList {

    public List<MovieItem> results;

    public MovieList(List<MovieItem> result) {
        this.results = result;
    }

    public List<MovieItem> getResult() {
        return results;
    }

    public void setResult(List<MovieItem> result) {
        this.results = result;
    }
}
