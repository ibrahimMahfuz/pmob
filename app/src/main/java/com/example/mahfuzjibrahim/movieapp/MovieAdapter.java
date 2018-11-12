package com.example.mahfuzjibrahim.movieapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    onMovieItemClicked clickHandler;

    public void setClickHandler(onMovieItemClicked clickHandler) {
        this.clickHandler = clickHandler;
    }
    private ArrayList<MovieItem> dataMovie;
    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.row_movie,viewGroup,false);
        MovieHolder holder = new MovieHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder movieHolder, int i) {
        final MovieItem movieItem = dataMovie.get(i);

        movieHolder.tvTitle.setText(String.valueOf(movieItem.title));
        movieHolder.tvRate.setText(String.valueOf(movieItem.vote_average));
        String url = "http://image.tmdb.org/t/p/w300" + movieItem.getPoster_path();
                Glide.with(movieHolder.itemView)
                .load(url)
                .into(movieHolder.imagePoster);

        movieHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler.movieItemClicked(movieItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(dataMovie == null){
            return 0;
        }
        return dataMovie.size();
    }
    public ArrayList<MovieItem> getDataMovie() {
        return dataMovie;
    }

    public void setDataMovie(ArrayList<MovieItem> dataMovie) {
        this.dataMovie = dataMovie;
        notifyDataSetChanged();
    }

    public class MovieHolder extends RecyclerView.ViewHolder{

        ImageView imagePoster;
        TextView tvTitle;
        TextView tvRate;
        CardView cardView;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);

            imagePoster = itemView.findViewById(R.id.imageView_poster);
            tvTitle = itemView.findViewById(R.id.textTitle);
            tvRate = itemView.findViewById(R.id.textRate);
            cardView = itemView.findViewById(R.id.card_list_movie);
        }
    }

    public interface onMovieItemClicked{
        void movieItemClicked(MovieItem i);
    }
}
