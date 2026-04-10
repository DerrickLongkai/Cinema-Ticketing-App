package com.example.cinemaapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemaapp.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    // UI Component Declarations
    private ImageView imgPoster;
    private TextView tvTitle, tvRating, tvDirector, tvStarring, tvGenre,tvDuration;
    private Button btnPurchase;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // 1. Initialize UI components by binding them to their respective XML IDs
        imgPoster = findViewById(R.id.imgDetailPoster);
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvRating = findViewById(R.id.tvDetailRating);
        tvDirector = findViewById(R.id.tvDetailDirector);
        tvStarring = findViewById(R.id.tvDetailStarring);
        tvGenre = findViewById(R.id.tvDetailGenre);
        tvDuration = findViewById(R.id.tvDetailDuration);
        btnPurchase = findViewById(R.id.btnDetailPurchase);
        boolean isComingSoon = getIntent().getBooleanExtra("IS_COMING_SOON", false);

        btnBack = findViewById(R.id.btnBack);

        // 2. Retrieve the serialized Movie object passed via Intent payload
        Movie movie = (Movie) getIntent().getSerializableExtra("MOVIE_OBJECT");

        // 3. Populate the UI dynamically if the data payload is successfully received
        if (movie != null) {
            imgPoster.setImageResource(movie.getPosterImageResId());
            tvTitle.setText(movie.getTitle());
            tvRating.setText(movie.getRating());
            tvDirector.setText("Director: " + movie.getDirector());
            tvStarring.setText("Starring: " + movie.getStarring());
            tvGenre.setText("Genre: " + movie.getGenre());
            tvDuration.setText("Duration: " + movie.getDuration());
        }

        // 4. Configure back navigation behavior (pops the current Activity off the stack)
        btnBack.setOnClickListener(v -> finish());

        // 5. Handle routing to the Schedule Activity when the purchase action is triggered
        btnPurchase.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScheduleActivity.class);
            // Pass the movie title forward to maintain context in the next screen
            if (movie != null) {
                intent.putExtra("MOVIE_TITLE", movie.getTitle());
            }
            startActivity(intent);
        });
        if (isComingSoon) {

            btnPurchase.setText("Releasing Soon");
            btnPurchase.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#B0BEC5"))); // 灰色
            btnPurchase.setEnabled(false);
        } else {

            btnPurchase.setText("Purchase Tickets");
            btnPurchase.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#E91E63"))); // 粉色
            btnPurchase.setEnabled(true);
            btnPurchase.setOnClickListener(v -> {
                Intent intent = new Intent(MovieDetailActivity.this, ScheduleActivity.class);
                intent.putExtra("MOVIE_TITLE", movie.getTitle());
                startActivity(intent);
            });
        }
    }
}