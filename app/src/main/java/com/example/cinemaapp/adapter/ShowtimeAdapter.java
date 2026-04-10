package com.example.cinemaapp.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaapp.MainActivity;
import com.example.cinemaapp.R;
import com.example.cinemaapp.model.Showtime;

import java.util.List;
import java.util.Locale;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.ShowtimeViewHolder> {

    private Context context;
    private List<Showtime> showtimeList;
    private String movieTitle;
    private String movieDuration; // Added: receive movie duration
    private String selectedDate = "";

    // Updated constructor: now requires movie duration
    public ShowtimeAdapter(Context context, List<Showtime> showtimeList, String movieTitle, String movieDuration) {
        this.context = context;
        this.showtimeList = showtimeList;
        this.movieTitle = movieTitle;
        this.movieDuration = movieDuration;
    }
    public void setSelectedDate(String date) {
        this.selectedDate = date;
    }

    @NonNull
    @Override
    public ShowtimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_showtime, parent, false);
        return new ShowtimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowtimeViewHolder holder, int position) {
        Showtime showtime = showtimeList.get(position);

        holder.tvTime.setText(showtime.getTime());
        holder.tvFormat.setText(showtime.getLanguageAndFormat());
        holder.tvHall.setText(showtime.getHallName());
        holder.tvPrice.setText(showtime.getPrice());

        // Key feature: dynamically calculate and display end time
        String endTimeStr = calculateEndTime(showtime.getTime(), movieDuration);
        holder.tvEndTime.setText(endTimeStr);

        holder.btnSelectSeat.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("MOVIE_TITLE", movieTitle);
            String pureEndTime = endTimeStr.replace("Ends ", "");
            //"Today, Apr 09 | 10:00 - 12:49"
            String finalTimeStr = selectedDate + " | " + showtime.getTime() + " - " + pureEndTime;

            intent.putExtra("SHOW_TIME", finalTimeStr);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return showtimeList.size();
    }

    /**
     * Time calculation engine: add "10:00" and "169 min" to get the end time
     */
    private String calculateEndTime(String startTime, String durationStr) {
        try {
            // Parse start time (HH:mm)
            String[] timeParts = startTime.split(":");
            int hours = Integer.parseInt(timeParts[0]);
            int minutes = Integer.parseInt(timeParts[1]);

            // Parse movie duration (extract "169" from "169 min")
            int durationMins = 0;
            if (durationStr != null && durationStr.contains("min")) {
                durationMins = Integer.parseInt(durationStr.replace("min", "").trim());
            }

            // Calculate total minutes and convert back to 24-hour format
            int totalMinutes = (hours * 60) + minutes + durationMins;
            int endHours = (totalMinutes / 60) % 24; // Wrap around after midnight
            int endMins = totalMinutes % 60;

            // Format output, e.g., "Ends 12:49"
            return String.format(Locale.getDefault(), "Ends %02d:%02d", endHours, endMins);
        } catch (Exception e) {
            return "Ends --:--"; // Fallback if parsing fails
        }
    }

    public static class ShowtimeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvEndTime, tvFormat, tvHall, tvPrice; // Added tvEndTime
        Button btnSelectSeat;

        public ShowtimeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvShowTimeValue);
            tvEndTime = itemView.findViewById(R.id.tvEndTime); // Bind end time TextView
            tvFormat = itemView.findViewById(R.id.tvFormat);
            tvHall = itemView.findViewById(R.id.tvHall);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnSelectSeat = itemView.findViewById(R.id.btnSelectSeat);
        }
    }
}
