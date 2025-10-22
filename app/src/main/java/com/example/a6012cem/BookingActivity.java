package com.example.a6012cem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BookingActivity extends AppCompatActivity {
    private TextView tvConcertTitle, tvConcertPrice;
    private EditText etCustomerName, etEmail, etTickets;
    private Button btnConfirmBooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        initViews();
        loadConcertInfo();
    }

    private void initViews() {
        tvConcertTitle = findViewById(R.id.tvConcertTitle);
        tvConcertPrice = findViewById(R.id.tvConcertPrice);
        etCustomerName = findViewById(R.id.etCustomerName);
        etEmail = findViewById(R.id.etEmail);
        etTickets = findViewById(R.id.etTickets);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);

        btnConfirmBooking.setOnClickListener(v -> confirmBooking());
    }

    private void loadConcertInfo() {
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("concert_title");
            double price = intent.getDoubleExtra("concert_price", 0.0);

            if (title != null) {
                tvConcertTitle.setText(title);
            }
            tvConcertPrice.setText(String.format("Price: $%.2f per ticket", price));
        }
    }

    private void confirmBooking() {
        String name = etCustomerName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String ticketsStr = etTickets.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || ticketsStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int tickets = Integer.parseInt(ticketsStr);
            if (tickets <= 0) {
                Toast.makeText(this, "Please enter valid number of tickets", Toast.LENGTH_SHORT).show();
                return;
            }

            // Process booking
            Toast.makeText(this, "Booking confirmed for " + tickets + " tickets!", Toast.LENGTH_SHORT).show();
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
        }
    }
}