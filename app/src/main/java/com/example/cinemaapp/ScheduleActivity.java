package com.example.cinemaapp;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;

import com.example.cinemaapp.adapter.ShowtimeAdapter;
import com.example.cinemaapp.model.Showtime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {

    private TextView tvMovieTitle;
    private ImageButton btnBack;
    private TabLayout tabLayoutDates;
    private RecyclerView recyclerView;

    private ShowtimeAdapter adapter;
    private List<Showtime> currentShowtimes;
    private String passedMovieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // 1. Initialize Views
        tvMovieTitle = findViewById(R.id.tvScheduleMovieTitle);
        btnBack = findViewById(R.id.btnScheduleBack);
        tabLayoutDates = findViewById(R.id.tabLayoutScheduleDates);
        recyclerView = findViewById(R.id.recyclerViewShowtimes);

        // 2. Retrieve passed movie title
        passedMovieTitle = getIntent().getStringExtra("MOVIE_TITLE");
        if (passedMovieTitle != null) {
            tvMovieTitle.setText(passedMovieTitle);
        }

        // Core fix: fetch the actual movie duration from the repository
        String movieDuration = "120 min"; // Safe default value
        if (passedMovieTitle != null) {
            com.example.cinemaapp.model.Movie currentMovie =
                    com.example.cinemaapp.data.MovieRepository.getMovieByTitle(passedMovieTitle);

            if (currentMovie != null && currentMovie.getDuration() != null) {
                movieDuration = currentMovie.getDuration();
            }
        }

        // 3. Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        currentShowtimes = new ArrayList<>();

        // Core fix: pass all 4 parameters (including movieDuration) to the adapter
        adapter = new ShowtimeAdapter(this, currentShowtimes, passedMovieTitle, movieDuration);
        recyclerView.setAdapter(adapter);

        // 4. Setup UI interactions
        btnBack.setOnClickListener(v -> finish());
        setupDateTabs();
    }

    /**
     * Generates 7 rolling date tabs (Today + next 6 days).
     */
    private void setupDateTabs() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 7; i++) {
            String dateString = dateFormat.format(calendar.getTime());
            String displayDate = dateString;

            if (i == 0) displayDate = "Today, " + dateString;
            else if (i == 1) displayDate = "Tomorrow, " + dateString;

            tabLayoutDates.addTab(tabLayoutDates.newTab().setText(displayDate));
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        tabLayoutDates.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadShowtimes(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Load today's showtimes by default
        if (tabLayoutDates.getTabCount() > 0) {
            loadShowtimes(0);
        }
    }

    /**
     * Loads mock showtimes based on the selected date tab.
     */
    private void loadShowtimes(int tabPosition) {
        currentShowtimes.clear();
        currentShowtimes.addAll(
                com.example.cinemaapp.data.MovieRepository.getShowtimes(tabPosition)
        );
        if (tabLayoutDates.getTabAt(tabPosition) != null) {
            String dateText = tabLayoutDates.getTabAt(tabPosition).getText().toString();
            adapter.setSelectedDate(dateText);
        }
        adapter.notifyDataSetChanged();
    }
}
