package com.example.a6012cem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBookings;
    private LinearLayout emptyState;
    private Button btnBackToHome;
    private BookingAdapter adapter;
    private List<Booking> bookingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        initializeViews();
        setupRecyclerView();
        loadBookings();
    }

    private void initializeViews() {
        recyclerViewBookings = findViewById(R.id.recyclerViewBookings);
        emptyState = findViewById(R.id.emptyState);
        btnBackToHome = findViewById(R.id.btnBackToHome);

        btnBackToHome.setOnClickListener(v -> {
            goBackToHome();
        });
    }
    private void goBackToHome() {
        // Check if user is admin or customer and navigate accordingly
        // You can use a flag from intent or check user type from your authentication system

        // Option 1: Check intent extra for user type
        String userType = getIntent().getStringExtra("USER_TYPE");

        if ("admin".equals(userType)) {
            // Navigate to Admin Dashboard
            Intent intent = new Intent(this, AdminDashboardActivity.class);
            startActivity(intent);
        } else {
            // Navigate to Customer Home
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }

        // Optional: finish current activity
        // finish();
    }

    private void setupRecyclerView() {
        adapter = new BookingAdapter(bookingList, new BookingAdapter.OnBookingClickListener() {
            @Override
            public void onCancelBooking(Booking booking) {
                showCancelConfirmation(booking);
            }
        });

        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBookings.setAdapter(adapter);
    }

    private void loadBookings() {
        String currentUserId = "";
        //load actual bookings from storage
        bookingList = BookingHelper.getAllBookings(this);

        if (bookingList.isEmpty()) {
            showEmptyState();
        } else {
            showBookingsList();
        }

        adapter.updateBookings(bookingList);
    }

    private void showEmptyState() {
        recyclerViewBookings.setVisibility(View.GONE);
        emptyState.setVisibility(View.VISIBLE);
    }

    private void showBookingsList() {
        recyclerViewBookings.setVisibility(View.VISIBLE);
        emptyState.setVisibility(View.GONE);
    }

    private void showCancelConfirmation(Booking booking) {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel your booking for " + booking.getConcertTitle() + "?")
                .setPositiveButton("Yes, Cancel", (dialog, which) -> {
                    cancelBooking(booking);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelBooking(Booking booking) {
        // Remove booking from storage
        BookingHelper.deleteBooking(this, booking.getId());

        // Remove booking from list
        bookingList.remove(booking);
        adapter.updateBookings(bookingList);

        // Show empty state if no bookings left
        if (bookingList.isEmpty()) {
            showEmptyState();
        }

        Toast.makeText(this, "Booking cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh bookings when returning to this activity
        loadBookings();
    }
}