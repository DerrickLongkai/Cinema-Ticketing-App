package com.example.cinemaapp;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvSelectedTitle, tvSelectedTime, tvSelectedSeatsInfo, tvTotalPrice;
    private Button btnCheckout;
    private GridLayout seatGrid;

    // Seat configuration
    private final int ROWS = 8;
    private final int COLS = 8;
    private final double TICKET_PRICE = 15.0; // Ticket price

    private List<String> selectedSeatsList = new ArrayList<>();

    // Seat state constants
    private final int STATE_AVAILABLE = 0;
    private final int STATE_SOLD = 1;
    private final int STATE_SELECTED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Bind UI components
        tvSelectedTitle = findViewById(R.id.tvSelectedTitle);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        tvSelectedSeatsInfo = findViewById(R.id.tvSelectedSeatsInfo);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        seatGrid = findViewById(R.id.seatGrid);

        // 2. Receive passed movie data
        String passedTitle = getIntent().getStringExtra("MOVIE_TITLE");
        String passedTime = getIntent().getStringExtra("SHOW_TIME");
        if (passedTitle != null) tvSelectedTitle.setText(passedTitle);
        if (passedTime != null) tvSelectedTime.setText("Showtime: " + passedTime);

        // 3. Generate dynamic seat layout
        generateSeats();

        // 4. Checkout button: triggers the “final step”
        btnCheckout.setOnClickListener(v -> {

            // Package all final booking information and send it to the e‑ticket page
            android.content.Intent intent = new android.content.Intent(MainActivity.this, TicketActivity.class);
            intent.putExtra("FINAL_TITLE", tvSelectedTitle.getText().toString());
            intent.putExtra("FINAL_TIME", tvSelectedTime.getText().toString());
            intent.putExtra("FINAL_SEATS", String.join(", ", selectedSeatsList));
            intent.putExtra("FINAL_PRICE", tvTotalPrice.getText().toString());

            startActivity(intent);

            // Close the seat‑selection screen (users shouldn’t return to seat selection after purchase)
            finish();
        });
    }

    private void generateSeats() {
        seatGrid.setRowCount(ROWS);
        seatGrid.setColumnCount(COLS);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {

                // Calculate seat label (e.g., Row 1 Seat 1)
                int rowNum = r + 1;
                int seatNum = c + 1;
                String seatLabel = "Row " + rowNum + " Seat " + seatNum; // Original Chinese label

                // Create a TextView to represent a seat
                TextView seatView = new TextView(this);

                // Set seat size and margins
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = dpToPx(36);
                params.height = dpToPx(36);
                params.setMargins(dpToPx(6), dpToPx(6), dpToPx(6), dpToPx(6));

                // Add aisle space after column 4
                if (c == 4) {
                    params.leftMargin = dpToPx(24);
                }
                seatView.setLayoutParams(params);

                // Randomly mark some seats as sold (20% chance)
                int initialState = Math.random() < 0.2 ? STATE_SOLD : STATE_AVAILABLE;

                // Store seat state and label using tags
                seatView.setTag(R.id.seatGrid, initialState);
                seatView.setTag(R.id.tvSelectedTitle, seatLabel);

                updateSeatBackground(seatView, initialState);

                // Click listener for seat selection
                seatView.setOnClickListener(v -> handleSeatClick((TextView) v));

                seatGrid.addView(seatView);
            }
        }
    }

    private void handleSeatClick(TextView seatView) {
        int currentState = (int) seatView.getTag(R.id.seatGrid);
        String seatLabel = (String) seatView.getTag(R.id.tvSelectedTitle);

        if (currentState == STATE_SOLD) {
            Toast.makeText(this, "This seat is already sold.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentState == STATE_AVAILABLE) {
            // Limit selection to 4 seats
            if (selectedSeatsList.size() >= 4) {
                Toast.makeText(this, "You can only select up to 4 seats.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Select seat
            seatView.setTag(R.id.seatGrid, STATE_SELECTED);
            selectedSeatsList.add(seatLabel);

        } else if (currentState == STATE_SELECTED) {
            // Deselect seat
            seatView.setTag(R.id.seatGrid, STATE_AVAILABLE);
            selectedSeatsList.remove(seatLabel);
        }

        // Update visuals and bottom bar
        updateSeatBackground(seatView, (int) seatView.getTag(R.id.seatGrid));
        updateBottomBar();
    }

    private void updateSeatBackground(TextView seatView, int state) {
        if (state == STATE_AVAILABLE) {
            seatView.setBackgroundResource(R.drawable.seat_available);
        } else if (state == STATE_SOLD) {
            seatView.setBackgroundResource(R.drawable.seat_sold);
        } else if (state == STATE_SELECTED) {
            seatView.setBackgroundResource(R.drawable.seat_selected);
        }
    }

    private void updateBottomBar() {
        if (selectedSeatsList.isEmpty()) {
            tvSelectedSeatsInfo.setText("Please select seats");
            tvTotalPrice.setText("$0.00");
            btnCheckout.setEnabled(false);
        } else {
            tvSelectedSeatsInfo.setText(String.join(", ", selectedSeatsList));
            double total = selectedSeatsList.size() * TICKET_PRICE;
            tvTotalPrice.setText("$" + String.format("%.2f", total));
            btnCheckout.setEnabled(true);
        }
    }

    // Helper: convert dp to px
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
