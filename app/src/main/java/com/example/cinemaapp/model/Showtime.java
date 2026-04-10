package com.example.cinemaapp.model;

public class Showtime {
    private String time;
    private String languageAndFormat; // e.g., "English 2D"
    private String hallName;          // e.g., "IMAX Hall 1"
    private String price;             // e.g., "$15.00"

    public Showtime(String time, String languageAndFormat, String hallName, String price) {
        this.time = time;
        this.languageAndFormat = languageAndFormat;
        this.hallName = hallName;
        this.price = price;
    }

    public String getTime() { return time; }
    public String getLanguageAndFormat() { return languageAndFormat; }
    public String getHallName() { return hallName; }
    public String getPrice() { return price; }
}