package com.example.a6012cem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.UUID;

public class AddConcertActivity extends AppCompatActivity {

    private EditText editTitle, editArtist, editVenue, editDateTime, editPrice, editTickets;
    private Button btnAddConcert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_concert);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        editTitle = findViewById(R.id.editTitle);
        editArtist = findViewById(R.id.editArtist);
        editVenue = findViewById(R.id.editVenue);
        editDateTime = findViewById(R.id.editDateTime);
        editPrice = findViewById(R.id.editPrice);
        editTickets = findViewById(R.id.editTickets);
        btnAddConcert = findViewById(R.id.btnAddConcert); // Make sure this ID matches your XML
    }

    private void setupClickListeners() {
        btnAddConcert.setOnClickListener(v -> {
            try {
                String title = editTitle.getText().toString().trim();
                String artist = editArtist.getText().toString().trim();
                String venue = editVenue.getText().toString().trim();
                String dateTime = editDateTime.getText().toString().trim();
                double price = Double.parseDouble(editPrice.getText().toString().trim());
                int tickets = Integer.parseInt(editTickets.getText().toString().trim());

                if (title.isEmpty() || artist.isEmpty() || venue.isEmpty() || dateTime.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Concert newConcert = new Concert(
                        UUID.randomUUID().toString(),
                        title,
                        artist,
                        venue,
                        dateTime,
                        price,
                        tickets,
                        R.drawable.ic_grace, // Default image
                        true
                );

                LocalDataHelper dataHelper = new LocalDataHelper();
                dataHelper.addConcert(this, newConcert);

                Toast.makeText(this, "Concert added successfully!", Toast.LENGTH_SHORT).show();
                finish();

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter valid numbers for price and tickets", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error adding concert", Toast.LENGTH_SHORT).show();
            }
        });
    }
}