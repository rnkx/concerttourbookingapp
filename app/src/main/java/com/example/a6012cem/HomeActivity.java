package com.example.a6012cem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button btnViewConcerts, btnMyBookings, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        btnViewConcerts = findViewById(R.id.btnViewConcerts);
        btnMyBookings = findViewById(R.id.btnMyBookings);
        btnLogout = findViewById(R.id.logoutButton);
    }

    private void setupClickListeners() {
        // Navigate to View Concerts (Customer can only view)
        btnViewConcerts.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ViewConcertsActivity.class);
            startActivity(intent);
        });

        // My Bookings - Show booking history or message
        btnMyBookings.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyBookingsActivity.class);
            startActivity(intent);
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}