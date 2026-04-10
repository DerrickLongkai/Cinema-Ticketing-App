package com.example.cinemaapp.data;

import com.example.cinemaapp.R;
import com.example.cinemaapp.model.Movie;
import com.example.cinemaapp.model.Showtime;

import java.util.ArrayList;
import java.util.List;

public class MovieRepository {

    public static List<Movie> getNowShowingMovies() {
        List<Movie> movies = new ArrayList<>();

        movies.add(new Movie(
                "Interstellar", "", R.drawable.interstellar,
                "Christopher Nolan", "Matthew McConaughey, Anne Hathaway",
                "Sci-Fi, Adventure", "9.3", "169 min"
        ));

        movies.add(new Movie(
                "The Dark Knight", "", R.drawable.thedarkknight,
                "Christopher Nolan", "Christian Bale, Heath Ledger",
                "Action, Crime", "9.0", "152 min"
        ));

        movies.add(new Movie(
                "The Matrix", "", R.drawable.thematrix,
                "Lana Wachowski", "Keanu Reeves, Laurence Fishburne",
                "Sci-Fi, Action", "8.7", "136 min"
        ));

        movies.add(new Movie(
                "Inception", "", R.drawable.inception,
                "Christopher Nolan", "Leonardo DiCaprio, Joseph Gordon-Levitt",
                "Sci-Fi, Thriller", "8.8", "148 min"
        ));

        movies.add(new Movie(
                "The Shawshank Redemption", "", R.drawable.shawshankredemption,
                "Frank Darabont", "Tim Robbins, Morgan Freeman",
                "Drama", "9.3", "142 min"
        ));

        movies.add(new Movie(
                "Green Book", "", R.drawable.greenbook,
                "Hayao Miyazaki", "Rumi Hiiragi, Miyu Irino",
                "Animation, Fantasy", "8.6", "125 min"
        ));

        movies.add(new Movie(
                "Oppenheimer", "", R.drawable.oppenheimer,
                "Christopher Nolan", "Cillian Murphy, Emily Blunt",
                "Biography, Drama", "8.4", "180 min"
        ));

        return movies;
    }

    public static List<Movie> getComingSoonMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(
                "Genius Game", "Aug 15", R.drawable.geniusgame,
                "Cheng Liang", "Peng Yuchang, Ding Yuxi", "Thriller, Suspense", "TBD", "120 min"
        ));

        movies.add(new Movie(
                "Dune: Part Three", "Dec 20", R.drawable.dunepartthree,
                "Denis Villeneuve", "Timothée Chalamet, Zendaya", "Sci-Fi, Adventure", "TBD", "170 min"
        ));

        movies.add(new Movie(
                "Spider-Man: Beyond", "Mar 10", R.drawable.spiderman,
                "Joaquim Dos Santos", "Shameik Moore, Hailee Steinfeld", "Animation, Action", "TBD", "140 min"
        ));
        return movies;
    }

    /**
     *
     */
    public static List<Showtime> getShowtimes(int dateTabIndex) {
        List<Showtime> showtimes = new ArrayList<>();

        showtimes.add(new Showtime("10:00", "English 2D", "Hall 1", "$15.00"));
        showtimes.add(new Showtime("13:30", "English IMAX", "IMAX Hall", "$22.00"));
        showtimes.add(new Showtime("17:00", "English 2D", "VIP Hall 3", "$18.00"));
        showtimes.add(new Showtime("20:30", "English IMAX", "IMAX Hall", "$22.00"));
        showtimes.add(new Showtime("23:40", "English 2D", "Hall 2", "$12.00"));

        return showtimes;
    }

    public static Movie getMovieByTitle(String title) {
        List<Movie> allMovies = new ArrayList<>();
        allMovies.addAll(getNowShowingMovies());
        allMovies.addAll(getComingSoonMovies());

        for (Movie movie : allMovies) {
            if (movie.getTitle().equals(title)) {
                return movie;
            }
        }
        return null;
    }
}