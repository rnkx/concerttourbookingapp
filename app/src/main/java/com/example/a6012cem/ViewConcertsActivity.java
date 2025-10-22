package com.example.a6012cem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ViewConcertsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewConcerts;
    private TextView tvEmptyState;
    private ConcertAdapter adapter;
    private List<Concert> concertList;
    private Button btnBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_concerts);

        initializeViews();
        setupRecyclerView();
        loadConcerts();
        setupClickListeners();
    }

    private void initializeViews() {
        recyclerViewConcerts = findViewById(R.id.recyclerViewConcerts);
        tvEmptyState = findViewById(R.id.tvEmptyState);
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
        // Create adapter with click listener
        adapter = new ConcertAdapter(concertList, this, new ConcertAdapter.OnConcertClickListener() {
            @Override
            public void onEditClick(Concert concert) {
                // Not used in customer view
            }

            @Override
            public void onDeleteClick(Concert concert) {
                // Not used in customer view
            }

            @Override
            public void onBookClick(Concert concert) {
                // Handle booking when user clicks BOOK button
                bookConcert(concert);
            }

            @Override
            public void onConcertClick(Concert concert) {
                // Optional: Show concert details when item is clicked
                showConcertDetails(concert);
            }
        });

        // Set to customer mode (shows BOOK buttons, hides EDIT/DELETE)
        adapter.setAdminMode(false);

        // Setup RecyclerView
        recyclerViewConcerts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewConcerts.setAdapter(adapter);
    }

    private void loadConcerts() {
        // Load concerts from LocalDataHelper
        LocalDataHelper dataHelper = new LocalDataHelper();
        concertList = dataHelper.getAllConcerts(this);

        // Filter only active concerts
        List<Concert> activeConcerts = concertList;
        // If you have an 'active' field, use this:
        // List<Concert> activeConcerts = new ArrayList<>();
        // for (Concert concert : concertList) {
        //     if (concert.isActive()) {
        //         activeConcerts.add(concert);
        //     }
        // }

        if (activeConcerts.isEmpty()) {
            // Show empty state
            recyclerViewConcerts.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            // Show concerts
            recyclerViewConcerts.setVisibility(View.VISIBLE);
            tvEmptyState.setVisibility(View.GONE);
            adapter.updateConcerts(activeConcerts);
        }
    }

    private void bookConcert(Concert concert) {
        // Start booking process
        Intent intent = new Intent(this, BookConcertActivity.class);
        intent.putExtra("CONCERT_ID", concert.getId());
        startActivity(intent);

        Toast.makeText(this, "Booking: " + concert.getTitle(), Toast.LENGTH_SHORT).show();
    }

    private void showConcertDetails(Concert concert) {
        // Optional: Show detailed view of concert
        Intent intent = new Intent(this, ConcertDetailActivity.class);
        intent.putExtra("CONCERT_ID", concert.getId());
        startActivity(intent);
    }

    private void goBackToHome(){
        onBackPressed();
    }
public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
}
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh concerts when returning to this activity
        loadConcerts();
    }
}