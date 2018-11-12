package com.example.mahfuzjibrahim.movieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MovieAdapter.onMovieItemClicked {
    TextView mTextResult;
    ArrayList<MovieItem> daftarFilm = new ArrayList<>();
    RecyclerView rvListMovie;
    MovieAdapter movieAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvListMovie = findViewById(R.id.r_view_list_movie);
        movieAdapter = new MovieAdapter();
        getNowPlayingMovie();
        movieAdapter.setClickHandler(this);

        rvListMovie.setAdapter(movieAdapter);
        rvListMovie.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.refresh_menu){
            Toast.makeText(this,"refresh",Toast.LENGTH_SHORT).show();

            rvListMovie = findViewById(R.id.r_view_list_movie);
            movieAdapter = new MovieAdapter();
            getNowPlayingMovie();
            movieAdapter.setClickHandler(this);

            rvListMovie.setAdapter(movieAdapter);
            rvListMovie.setLayoutManager(new LinearLayoutManager(this));

        }
        return super.onOptionsItemSelected(item);
    }

    private void getNowPlayingMovie() {
        String API_BASE_URL = "https://api.themoviedb.org";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        TmdbClient client =  retrofit.create(TmdbClient.class);
        Call<MovieList> call =
                client.getMovies("fbda90b182cf9ac6b4d4a545c59947b2");

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                MovieList movieList = response.body();
                List<MovieItem> listMovieItem = movieList.results;
                Log.d("caliak", listMovieItem.get(1).getPoster_path());
                movieAdapter.setDataMovie(new ArrayList<MovieItem>(listMovieItem));
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "gagal bro", Toast.LENGTH_LONG);
            }
        });

    }


    @Override
    public void movieItemClicked(MovieItem i) {
        Intent detailMovieIntent = new Intent(this, DetailActivity.class);
        detailMovieIntent.putExtra("movie",i);
        startActivityForResult(detailMovieIntent, 1);
    }


}
