package com.example.cinemaapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaapp.MovieDetailActivity;
import com.example.cinemaapp.ScheduleActivity;
import com.example.cinemaapp.R;
import com.example.cinemaapp.model.Movie;

import java.util.List;

/**
 * Adapter responsible for binding detailed Movie data to the RecyclerView.
 * Implements the ViewHolder pattern for optimal scroll performance.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private Context context;
    private boolean isComingSoon = false;
    public void setComingSoon(boolean comingSoon) {
        this.isComingSoon = comingSoon;
    }

    public MovieAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for individual list items
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie currentMovie = movieList.get(position);

        // 1. Bind UI data (Now including Director and Starring)
        holder.tvMovieTitle.setText(currentMovie.getTitle());
        holder.imgPoster.setImageResource(currentMovie.getPosterImageResId());

        // Add null checks or defaults in case data is missing
        if (currentMovie.getDirector() != null) {
            holder.tvDirector.setText("Director: " + currentMovie.getDirector());
        }
        if (currentMovie.getStarring() != null) {
            holder.tvStarring.setText("Starring: " + currentMovie.getStarring());
        }

        if (isComingSoon) {
            // Coming soon
            holder.btnPurchase.setText(currentMovie.getShowTime()); //
            holder.btnPurchase.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#B0BEC5"))); // 灰色
            holder.btnPurchase.setEnabled(false); //
        } else {
            // Now in theaters
            holder.btnPurchase.setText("Purchase");
            holder.btnPurchase.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#E91E63"))); // 粉色
            holder.btnPurchase.setEnabled(true);
        }

        // 2. Click on the entire row -> Route to Movie Detail Activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailActivity.class);
            // CRITICAL FIX: Pass the entire serialized object, not just the title
            intent.putExtra("MOVIE_OBJECT", currentMovie);

            intent.putExtra("IS_COMING_SOON", isComingSoon);
            context.startActivity(intent);
        });

        // 3. Click strictly on the Purchase Button -> Route to Schedule Activity
        holder.btnPurchase.setOnClickListener(v -> {
            Intent intent = new Intent(context, ScheduleActivity.class);
            intent.putExtra("MOVIE_TITLE", currentMovie.getTitle());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    /**
     * ViewHolder caches UI references to avoid expensive findViewById() calls during scrolling.
     */
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvMovieTitle;
        TextView tvDirector;
        TextView tvStarring;
        Button btnPurchase;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            // Syncing with the new item_movie.xml IDs
            imgPoster = itemView.findViewById(R.id.imgPoster);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvDirector = itemView.findViewById(R.id.tvDirector);
            tvStarring = itemView.findViewById(R.id.tvStarring);
            btnPurchase = itemView.findViewById(R.id.btnPurchase); // Updated ID
        }
    }
}