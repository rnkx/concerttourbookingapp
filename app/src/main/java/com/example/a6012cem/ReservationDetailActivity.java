package com.example.a6012cem;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ReservationDetailActivity extends AppCompatActivity {

    private TextView tvConcertTitle, tvCustomerName, tvCustomerEmail, tvTicketCount,
            tvTotalPrice, tvStatus, tvBookingDate, tvConcertDate, tvVenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);

        initializeViews();
        loadReservationData();
    }

    private void initializeViews() {
        tvConcertTitle = findViewById(R.id.tvConcertTitle);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerEmail = findViewById(R.id.tvCustomerEmail);
        tvTicketCount = findViewById(R.id.tvTicketCount);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvStatus = findViewById(R.id.tvStatus);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        tvConcertDate = findViewById(R.id.tvConcertDate);
        tvVenue = findViewById(R.id.tvVenue);
    }

    private void loadReservationData() {
        // Get reservation data from intent
        Reservation reservation = (Reservation) getIntent().getSerializableExtra("reservation");

        if (reservation != null) {
            tvConcertTitle.setText(reservation.getConcertTitle());
            tvCustomerName.setText("Customer: " + reservation.getUserName());
            tvCustomerEmail.setText("Email: " + reservation.getUserEmail());
            tvTicketCount.setText("Tickets: " + reservation.getTicketCount());

            // FIXED: Convert double to String
            tvTotalPrice.setText(String.format("Total Price: $%.2f", reservation.getTotalPrice()));

            tvStatus.setText("Status: " + reservation.getStatus());

            // Format dates
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            String bookingDate = sdf.format(reservation.getBookingDate());
            tvBookingDate.setText("Booked on: " + bookingDate);

            if (reservation.getConcertDate() != null) {
                tvConcertDate.setText("Concert Date: " + reservation.getConcertDate());
            }

            if (reservation.getVenue() != null) {
                tvVenue.setText("Venue: " + reservation.getVenue());
            }
        }
    }
}