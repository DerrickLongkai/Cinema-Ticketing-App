package com.example.cinemaapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;

import com.example.cinemaapp.adapter.MovieAdapter;
import com.example.cinemaapp.model.Movie;
import com.example.cinemaapp.data.MovieRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private TabLayout tabLayoutCategories;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> currentMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabLayoutCategories = findViewById(R.id.tabLayoutCategories);
        recyclerView = findViewById(R.id.recyclerViewMovies);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        currentMovieList = new ArrayList<>();
        adapter = new MovieAdapter(this, currentMovieList);
        recyclerView.setAdapter(adapter);

        setupCategoryTabs();
    }

    /**
     * Initializes static categories: "Now in theaters" & "Coming soon"
     */
    private void setupCategoryTabs() {
        // 1. Add static categories
        tabLayoutCategories.addTab(tabLayoutCategories.newTab().setText("Now in theaters"));
        tabLayoutCategories.addTab(tabLayoutCategories.newTab().setText("Coming soon"));

        // 2. Load default data on startup
        loadNowShowingMovies();

        // 3. Handle tab switching
        tabLayoutCategories.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    loadNowShowingMovies();
                } else {
                    loadComingSoonMovies();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    /**
     * 🚀 Fetches "Now Showing" data from the Repository and updates the UI.
     */
    private void loadNowShowingMovies() {
        currentMovieList.clear();
        currentMovieList.addAll(MovieRepository.getNowShowingMovies());
        adapter.setComingSoon(false);
        adapter.notifyDataSetChanged();
    }

    /**
     * 🚀 Fetches "Coming Soon" data from the Repository and updates the UI.
     */
    private void loadComingSoonMovies() {
        currentMovieList.clear();
        currentMovieList.addAll(MovieRepository.getComingSoonMovies());
        adapter.setComingSoon(true);
        adapter.notifyDataSetChanged();
    }
}