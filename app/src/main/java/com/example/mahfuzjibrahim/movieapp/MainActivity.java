package com.example.mahfuzjibrahim.movieapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
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

public class MainActivity extends AppCompatActivity implements MovieAdapter.onMovieItemClicked
{
    TextView mTextResult;
    ArrayList<MovieItem> daftarFilm = new ArrayList<>();
    RecyclerView rvListMovie;
    MovieAdapter movieAdapter;
    ProgressBar mprogressBar;
    SearchView mSearchView;
    int pil = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleIntent(getIntent());
        mprogressBar = findViewById(R.id.progressBar);
        rvListMovie = findViewById(R.id.r_view_list_movie);
        movieAdapter = new MovieAdapter();
        int pref = getPreferences(Context.MODE_PRIVATE).getInt("display_status_key",1);
        if(pref == 1){
            getNowPlayingMovie();
        }else if(pref == 2){
            getUpomingMovie();
        }else{
            getSearchingMovie(getPreferences(Context.MODE_PRIVATE).getString("query",null));
        }
        movieAdapter.setClickHandler(this);

        rvListMovie.setAdapter(movieAdapter);
        rvListMovie.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.refresh_menu){
            Toast.makeText(this,"refresh",Toast.LENGTH_SHORT).show();

            rvListMovie = findViewById(R.id.r_view_list_movie);
            movieAdapter = new MovieAdapter();
            if(pil == 0){
                getNowPlayingMovie();
            }else{
                getUpomingMovie();
            }
            movieAdapter.setClickHandler(this);

            rvListMovie.setAdapter(movieAdapter);
            rvListMovie.setLayoutManager(new LinearLayoutManager(this));

        }
        else if(item.getItemId() == R.id.menu_now_playing){
            pil = 0;
            Toast.makeText(this,"refresh",Toast.LENGTH_SHORT).show();

            rvListMovie = findViewById(R.id.r_view_list_movie);
            movieAdapter = new MovieAdapter();
            getNowPlayingMovie();
            movieAdapter.setClickHandler(this);

            rvListMovie.setAdapter(movieAdapter);
            rvListMovie.setLayoutManager(new LinearLayoutManager(this));
        }else if(item.getItemId() == R.id.menu_upcoming){
            pil = 1;
            Toast.makeText(this,"refresh",Toast.LENGTH_SHORT).show();

            rvListMovie = findViewById(R.id.r_view_list_movie);
            movieAdapter = new MovieAdapter();
            getUpomingMovie();
            movieAdapter.setClickHandler(this);

            rvListMovie.setAdapter(movieAdapter);
            rvListMovie.setLayoutManager(new LinearLayoutManager(this));
        }else if(item.getItemId() == R.id.menu_search){

        }
        return super.onOptionsItemSelected(item);
    }

    private void getNowPlayingMovie() {
        getSupportActionBar().setTitle("Now Playing");
        mprogressBar.setVisibility(View.VISIBLE);
        rvListMovie.setVisibility(View.INVISIBLE);
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
                client.getNowPlaying("fbda90b182cf9ac6b4d4a545c59947b2");

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                MovieList movieList = response.body();
                List<MovieItem> listMovieItem = movieList.results;
                Log.d("caliak", listMovieItem.get(1).getPoster_path());
                movieAdapter.setDataMovie(new ArrayList<MovieItem>(listMovieItem));
                mprogressBar.setVisibility(View.GONE);
                rvListMovie.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "gagal bro", Toast.LENGTH_LONG);
                mprogressBar.setVisibility(View.GONE);
                rvListMovie.setVisibility(View.VISIBLE);
            }
        });

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("display_status_key", 1);
        editor.commit();

    }

    private void getUpomingMovie() {
        getSupportActionBar().setTitle("Upcoming");
        mprogressBar.setVisibility(View.VISIBLE);
        rvListMovie.setVisibility(View.INVISIBLE);
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
                client.getUpComing("fbda90b182cf9ac6b4d4a545c59947b2");

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                MovieList movieList = response.body();
                List<MovieItem> listMovieItem = movieList.results;
                Log.d("caliak", listMovieItem.get(1).getPoster_path());
                movieAdapter.setDataMovie(new ArrayList<MovieItem>(listMovieItem));
                mprogressBar.setVisibility(View.GONE);
                rvListMovie.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "gagal bro", Toast.LENGTH_LONG);
                mprogressBar.setVisibility(View.GONE);
                rvListMovie.setVisibility(View.VISIBLE);
            }
        });
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("display_status_key", 2);
        editor.commit();

    }

    private void getSearchingMovie(String query) {
        getSupportActionBar().setTitle("SearchResult for : "+query);
        mprogressBar.setVisibility(View.VISIBLE);
        rvListMovie.setVisibility(View.INVISIBLE);
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
                client.getSearch("fbda90b182cf9ac6b4d4a545c59947b2",query);

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                MovieList movieList = response.body();
                List<MovieItem> listMovieItem = movieList.results;
                Log.d("caliak", listMovieItem.get(1).getPoster_path());
                movieAdapter.setDataMovie(new ArrayList<MovieItem>(listMovieItem));
                mprogressBar.setVisibility(View.GONE);
                rvListMovie.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "gagal bro", Toast.LENGTH_LONG);
                mprogressBar.setVisibility(View.GONE);
                rvListMovie.setVisibility(View.VISIBLE);
            }
        });

        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("display_status_key", 3);
        editor.commit();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void movieItemClicked(MovieItem i) {
        Intent detailMovieIntent = new Intent(this, DetailActivity.class);
        detailMovieIntent.putExtra("movie",i);
        startActivityForResult(detailMovieIntent, 1);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("ibrahimPntr", query);
            SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("display_status_key", 3);
            editor.putString("query",query);
            editor.commit();
        }
    }


}
