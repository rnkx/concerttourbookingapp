package com.example.a6012cem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ManageReservationActivity extends AppCompatActivity {

    private RecyclerView recyclerViewReservations;
    private LinearLayout emptyState;
    private Button btnBackToHome;
    private ReservationAdapter adapter;
    private List<Booking> reservationList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_reservation);


        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Manage Reservations");
        }

        initializeViews();
        setupClickListeners();
        setupRecyclerView();
        loadReservations();

    }

    private void initializeViews() {
        recyclerViewReservations = findViewById(R.id.recyclerViewReservations);
        emptyState = findViewById(R.id.emptyState);
        btnBackToHome = findViewById(R.id.btnBackToHome);
    }
    private void setupClickListeners() {
        // Back to Home button click listener
        if (btnBackToHome != null) {
            btnBackToHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goBackToHome();
                }
            });
        } else {
            Toast.makeText(this, "Back button is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView() {
        adapter = new ReservationAdapter(reservationList, new ReservationAdapter.OnReservationClickListener() {
            @Override
            public void onCancelReservation(Booking booking) {
                showCancelConfirmation(booking);
            }

            @Override
            public void onConfirmReservation(Booking booking) {
                confirmReservation(booking);
            }
            @Override
            public void onBackToHome() {
                // Call the same method as your back button
                goBackToHome();
            }
        });

        recyclerViewReservations.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReservations.setAdapter(adapter);
    }

    private void loadReservations() {
        // Load ALL bookings for admin view - make sure BookingHelper exists
        try {
            reservationList = BookingHelper.getAllBookings(this);
        } catch (Exception e) {
            // If BookingHelper doesn't exist yet, use empty list
            reservationList = new ArrayList<>();
            Toast.makeText(this, "Error loading bookings: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (reservationList == null) {
            reservationList = new ArrayList<>();
        }

        if (reservationList.isEmpty()) {
            showEmptyState();
        } else {
            showReservationsList();
        }

        if (adapter != null) {
            adapter.updateReservations(reservationList);
        }
    }
    private void showEmptyState() {
        if (recyclerViewReservations != null && emptyState != null) {
            recyclerViewReservations.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        }
    }

    private void showReservationsList() {
        if (recyclerViewReservations != null && emptyState != null) {
            recyclerViewReservations.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }
    }

    private void showCancelConfirmation(Booking booking) {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Reservation")
                .setMessage("Cancel reservation for " + booking.getConcertTitle() + " by " + booking.getUserName() + "?")
                .setPositiveButton("Yes, Cancel", (dialog, which) -> {
                    cancelReservation(booking);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelReservation(Booking booking) {
        BookingHelper.updateBookingStatus(this, booking.getId(), "Cancelled");
        loadReservations(); // Refresh the list

        Toast.makeText(this, "Reservation cancelled", Toast.LENGTH_SHORT).show();
    }

    private void confirmReservation(Booking booking) {
        BookingHelper.updateBookingStatus(this, booking.getId(), "Confirmed");
        loadReservations(); // Refresh the list

        Toast.makeText(this, "Reservation confirmed", Toast.LENGTH_SHORT).show();
    }

    private void goBackToHome() {
        onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // This will take you back to the previous activity (Admin Dashboard)
        return true;
    }
    // Handle device back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Optional: Add animation
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadReservations();

    }
}