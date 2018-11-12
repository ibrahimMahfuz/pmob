package com.example.mahfuzjibrahim.movieapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TmdbClient {
    @GET("/3/movie/now_playing")
    Call<MovieList> getMovies(
            @Query("api_key") String api_key
    );
}
