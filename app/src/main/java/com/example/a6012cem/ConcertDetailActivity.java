package com.example.a6012cem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ConcertDetailActivity extends AppCompatActivity {

    private ImageView imgConcert;
    private TextView tvTitle, tvArtist, tvVenue, tvDateTime, tvPrice, tvTickets, tvDescription;
    private Button btnBook, btnBack;
    private Concert concert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concert_detail);

        initializeViews();
        loadConcertData();
        setupClickListeners();
    }

    private void initializeViews() {
        imgConcert = findViewById(R.id.imgConcert);
        tvTitle = findViewById(R.id.tvTitle);
        tvArtist = findViewById(R.id.tvArtist);
        tvVenue = findViewById(R.id.tvVenue);
        tvDateTime = findViewById(R.id.tvDateTime);
        tvPrice = findViewById(R.id.tvPrice);
        tvTickets = findViewById(R.id.tvTickets);
        tvDescription = findViewById(R.id.tvDescription);
        btnBook = findViewById(R.id.btnBook);
        btnBack = findViewById(R.id.btnBack);
    }

    private void loadConcertData() {
        String concertId = getIntent().getStringExtra("CONCERT_ID");
        // Load concert data and populate views
    }

    private void setupClickListeners() {
        btnBook.setOnClickListener(v -> {
            // Start booking process
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}