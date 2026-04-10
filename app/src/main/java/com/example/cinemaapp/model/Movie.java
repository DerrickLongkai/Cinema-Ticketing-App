package com.example.cinemaapp.model;

import java.io.Serializable;

public class Movie implements Serializable {
    private String title;
    private String showTime;
    private int posterImageResId;
    private String director;
    private String starring;
    private String genre;
    private String rating;
    private String duration;

    public Movie(String title, String showTime, int posterImageResId, String director, String starring, String genre, String rating, String duration) {
        this.title = title;
        this.showTime = showTime;
        this.posterImageResId = posterImageResId;
        this.director = director;
        this.starring = starring;
        this.genre = genre;
        this.rating = rating;
        this.duration = duration;
    }

    // Getters
    public String getTitle() { return title; }
    public String getShowTime() { return showTime; }
    public int getPosterImageResId() { return posterImageResId; }
    public String getDirector() { return director; }
    public String getStarring() { return starring; }
    public String getGenre() { return genre; }
    public String getRating() { return rating; }
    public String getDuration() { return duration; }
}