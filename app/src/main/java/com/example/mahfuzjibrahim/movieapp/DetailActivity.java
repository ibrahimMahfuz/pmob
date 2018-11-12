package com.example.mahfuzjibrahim.movieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {
    TextView mTextTitle, mTextVoteAverage, mTextReleaseDate, mTextOverview;
    ImageView mImagePoster;
    MovieItem movieItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movieItem = getIntent().getParcelableExtra("movie");
        Log.d("overviewIbrahim", movieItem.getOverview());
        String title = movieItem.getTitle();
        Double voteAverage = movieItem.getVote_average();
        String releaseDate = movieItem.getRelease_date();
        String overview = movieItem.getOverview();
        String poster_path = movieItem.getPoster_path();

        mTextTitle = findViewById(R.id.text_detail_title);
        mTextVoteAverage = findViewById(R.id.text_detail_vote_average);
        mTextReleaseDate = findViewById(R.id.text_detail_release_date);
        mTextOverview = findViewById(R.id.text_detail_overview);
        mImagePoster = findViewById(R.id.image_detail_poster);

        mTextTitle.setText(title);
        mTextVoteAverage.setText(voteAverage.toString());
        mTextReleaseDate.setText(releaseDate);
        mTextOverview.setText(overview);
        String url = "http://image.tmdb.org/t/p/w300" + poster_path;
        Log.d("caliakk", url);
        Glide.with(this)
                .load(url)
                .into(mImagePoster);

    }

}
