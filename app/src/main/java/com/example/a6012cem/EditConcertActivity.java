package com.example.a6012cem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class EditConcertActivity extends AppCompatActivity {

    private EditText editTitle, editArtist, editVenue, editDateTime, editPrice, editTickets;
    private Button btnUpdateConcert, btnCancel;
    private Concert concert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_concert);

        initializeViews();
        loadConcertData();
        setupClickListeners();
    }

    private void initializeViews() {
        editTitle = findViewById(R.id.editTitle);
        editArtist = findViewById(R.id.editArtist);
        editVenue = findViewById(R.id.editVenue);
        editDateTime = findViewById(R.id.editDateTime);
        editPrice = findViewById(R.id.editPrice);
        editTickets = findViewById(R.id.editTickets);
        btnUpdateConcert = findViewById(R.id.btnUpdateConcert);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void loadConcertData() {
        String concertId = getIntent().getStringExtra("CONCERT_ID");
        List<Concert> concerts = LocalDataHelper.loadConcerts(this);

        for (Concert c : concerts) {
            if (c.getId().equals(concertId)) {
                concert = c;
                break;
            }
        }

        if (concert == null) {
            Toast.makeText(this, "Concert not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Populate the form with existing data
        editTitle.setText(concert.getTitle());
        editArtist.setText(concert.getArtist());
        editVenue.setText(concert.getVenue());
        editDateTime.setText(concert.getDateTime());
        editPrice.setText(String.valueOf(concert.getPrice()));
        editTickets.setText(String.valueOf(concert.getTicketsAvailable()));
    }

    private void setupClickListeners() {
        btnUpdateConcert.setOnClickListener(v -> updateConcert());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void updateConcert() {
        try {
            // Validate input
            String title = editTitle.getText().toString().trim();
            String artist = editArtist.getText().toString().trim();
            String venue = editVenue.getText().toString().trim();
            String dateTime = editDateTime.getText().toString().trim();
            String priceStr = editPrice.getText().toString().trim();
            String ticketsStr = editTickets.getText().toString().trim();

            if (title.isEmpty() || artist.isEmpty() || venue.isEmpty() ||
                    dateTime.isEmpty() || priceStr.isEmpty() || ticketsStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);
            int tickets = Integer.parseInt(ticketsStr);

            if (price < 0 || tickets < 0) {
                Toast.makeText(this, "Price and tickets must be positive numbers", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update concert object
            concert.setTitle(title);
            concert.setArtist(artist);
            concert.setVenue(venue);
            concert.setDateTime(dateTime);
            concert.setPrice(price);
            concert.setTicketsAvailable(tickets);

            // Save to database
            LocalDataHelper.updateConcert(this, concert);

            Toast.makeText(this, "Concert updated successfully!", Toast.LENGTH_SHORT).show();
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers for price and tickets", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error updating concert", Toast.LENGTH_SHORT).show();
        }
    }
}