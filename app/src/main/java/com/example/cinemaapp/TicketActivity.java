package com.example.cinemaapp;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class TicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        TextView tvTitle = findViewById(R.id.tvTicketTitle);
        TextView tvTime = findViewById(R.id.tvTicketTime);
        TextView tvSeats = findViewById(R.id.tvTicketSeats);
        TextView tvPrice = findViewById(R.id.tvTicketPrice);
        ImageView ivQrCode = findViewById(R.id.ivQrCode);

        // Retrieve final booking data passed from the seat‑selection screen
        String title = getIntent().getStringExtra("FINAL_TITLE");
        String time = getIntent().getStringExtra("FINAL_TIME");
        String seats = getIntent().getStringExtra("FINAL_SEATS");
        String price = getIntent().getStringExtra("FINAL_PRICE");

        if (title != null) tvTitle.setText(title);
        if (time != null) tvTime.setText(time);
        if (seats != null) tvSeats.setText(seats);
        if (price != null) tvPrice.setText(price);

        // Build a text block containing ticket information for QR generation
        String ticketInfo = "Movie: " + title + "\nTime: " + time + "\nSeats: " + seats;

        // Generate QR code and display it
        Bitmap qrBitmap = generateQRCode(ticketInfo);
        if (qrBitmap != null) {
            ivQrCode.setImageBitmap(qrBitmap);
        }
    }

    /**
     * Core feature: generate a QR code bitmap using ZXing from a given text string.
     */
    private Bitmap generateQRCode(String text) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            // Create a 512x512 QR code
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            // Draw each pixel (black or white)
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bmp;

        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}

