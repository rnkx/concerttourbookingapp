package com.example.a6012cem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class BookConcertActivity extends AppCompatActivity {

    private ImageView imgConcert;
    private TextView tvConcertTitle, tvArtist, tvVenue, tvDateTime, tvPrice, tvTicketsAvailable;
    private TextView tvTicketCount, tvTotalPrice;
    private Button btnMinus, btnPlus, btnConfirmBooking, btnCancel;
    private Concert concert;
    private int ticketsToBook = 1;
    private double ticketPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_concert);

        initializeViews();
        loadConcertData();
        setupClickListeners();
        updateTotalPrice();
    }

    private void initializeViews() {
        imgConcert = findViewById(R.id.imgConcert);
        tvConcertTitle = findViewById(R.id.tvConcertTitle);
        tvArtist = findViewById(R.id.tvArtist);
        tvVenue = findViewById(R.id.tvVenue);
        tvDateTime = findViewById(R.id.tvDateTime);
        tvPrice = findViewById(R.id.tvPrice);
        tvTicketsAvailable = findViewById(R.id.tvTicketsAvailable);
        tvTicketCount = findViewById(R.id.tvTicketCount);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void loadConcertData() {
        String concertId = getIntent().getStringExtra("CONCERT_ID");

        if (concertId == null) {
            Toast.makeText(this, "No concert selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        LocalDataHelper dataHelper = new LocalDataHelper();
        concert = dataHelper.getConcertById(this, concertId);

        if (concert != null) {
            displayConcertData();
        } else {
            Toast.makeText(this, "Concert not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayConcertData() {
        if (concert == null) return;

        tvConcertTitle.setText(concert.getTitle());
        tvArtist.setText(concert.getArtist());
        tvVenue.setText(concert.getVenue());
        tvDateTime.setText(concert.getDateTime());
        tvPrice.setText(String.format("RM %.2f", concert.getPrice()));
        tvTicketsAvailable.setText(String.valueOf(concert.getTicketsAvailable()));

        ticketPrice = concert.getPrice();

        if (concert.getImageResId() != 0) {
            imgConcert.setImageResource(concert.getImageResId());
        }
    }

    private void setupClickListeners() {
        btnMinus.setOnClickListener(v -> {
            if (ticketsToBook > 1) {
                ticketsToBook--;
                updateTicketCount();
                updateTotalPrice();
            }
        });

        btnPlus.setOnClickListener(v -> {
            int availableTickets = concert != null ? concert.getTicketsAvailable() : 0;
            if (ticketsToBook < availableTickets) {
                ticketsToBook++;
                updateTicketCount();
                updateTotalPrice();
            } else {
                Toast.makeText(this, "Cannot exceed available tickets", Toast.LENGTH_SHORT).show();
            }
        });

        btnConfirmBooking.setOnClickListener(v -> {
            confirmBooking();
        });

        btnCancel.setOnClickListener(v -> {
            finish();
        });
    }

    private void updateTicketCount() {
        tvTicketCount.setText(String.valueOf(ticketsToBook));
    }

    private void updateTotalPrice() {
        double total = ticketsToBook * ticketPrice;
        tvTotalPrice.setText(String.format("RM %.2f", total));
    }

    private void confirmBooking() {
        if (concert == null) {
            Toast.makeText(this, "No concert selected", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ticketsToBook > concert.getTicketsAvailable()) {
            Toast.makeText(this, "Not enough tickets available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current user info (you might have this from login)
        String currentUserId = ""; // Replace with actual user ID from your auth system
        String currentUserName = ""; // Replace with actual user name
        // Create and save booking
        Booking booking = new Booking(
                concert.getId(),
                concert.getTitle(),
                concert.getArtist(),
                ticketsToBook,
                ticketsToBook * ticketPrice,
                currentUserId,
                currentUserName
        );

        BookingHelper.saveBooking(this, booking);

        // Update available tickets
        concert.setTicketsAvailable(concert.getTicketsAvailable() - ticketsToBook);
        LocalDataHelper.updateConcert(this, concert);

        // Process booking logic here
        Toast.makeText(this,
                String.format("Booking confirmed! %d ticket(s) for %s",
                        ticketsToBook, concert.getTitle()),
                Toast.LENGTH_LONG).show();

        // Update available tickets
        concert.setTicketsAvailable(concert.getTicketsAvailable() - ticketsToBook);
        LocalDataHelper.updateConcert(this, concert);

        finish();
    }
}