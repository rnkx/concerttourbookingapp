package com.example.a6012cem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class ManageConcertsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewConcerts;
    private ConcertAdapter adapter;
    private Button btnCancelConcert, btnBackToHome;
    private FloatingActionButton fabAddConcert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_concert);

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
    }

    private void initializeViews() {
        recyclerViewConcerts = findViewById(R.id.recyclerViewConcerts);
        btnCancelConcert = findViewById(R.id.btnCancelConcert);
        fabAddConcert = findViewById(R.id.fabAddConcert);
        btnBackToHome = findViewById(R.id.btnBackToHome);
    }

    private void setupRecyclerView() {
        List<Concert> concerts = LocalDataHelper.loadConcerts(this);

        adapter = new ConcertAdapter(concerts, this, new ConcertAdapter.OnConcertClickListener() {
            @Override
            public void onEditClick(Concert concert) {
                editConcert(concert);
            }

            @Override
            public void onDeleteClick(Concert concert) {
                deleteConcert(concert);
            }

            @Override
            public void onBookClick(Concert concert) {
                // Not used in admin mode
            }

            @Override
            public void onConcertClick(Concert concert) {
                // Handle when user clicks on the concert item itself
                // You can show details or do nothing in admin mode
                showConcertDetails(concert);
            }
        });

        adapter.setAdminMode(true);
        recyclerViewConcerts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewConcerts.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnCancelConcert.setOnClickListener(v -> {
            showCancelAllConfirmationDialog();
        });

        fabAddConcert.setOnClickListener(v -> {
            Intent intent = new Intent(ManageConcertsActivity.this, AddConcertActivity.class);
            startActivity(intent);
        });

        btnBackToHome.setOnClickListener(v ->{
            Intent intent = new Intent(ManageConcertsActivity.this, AdminDashboardActivity.class);
              startActivity(intent);
        });

    }

    private void showCancelAllConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cancel All Concerts")
                .setMessage("Are you sure you want to cancel all concerts? This action cannot be undone.")
                .setPositiveButton("Yes, Cancel All", (dialog, which) -> {
                    cancelAllConcerts();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelAllConcerts() {
        LocalDataHelper dataHelper = new LocalDataHelper();
        List<Concert> concerts = dataHelper.getAllConcerts(this);

        for (Concert concert : concerts) {
            concert.setActive(false);
            LocalDataHelper.updateConcert(this, concert);
        }

        List<Concert> updatedConcerts = dataHelper.getAllConcerts(this);
        adapter.updateConcerts(updatedConcerts);
        Toast.makeText(this, "All concerts have been cancelled", Toast.LENGTH_SHORT).show();
    }

    private void editConcert(Concert concert) {
        Intent intent = new Intent(this, EditConcertActivity.class);
        intent.putExtra("CONCERT_ID", concert.getId());
        startActivity(intent);
    }

    private void deleteConcert(Concert concert) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Concert")
                .setMessage("Are you sure you want to delete '" + concert.getTitle() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    LocalDataHelper dataHelper = new LocalDataHelper();
                    dataHelper.deleteConcert(this, concert.getId());

                    List<Concert> updatedConcerts = dataHelper.getAllConcerts(this);
                    adapter.updateConcerts(updatedConcerts);
                    Toast.makeText(this, "Concert deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showConcertDetails(Concert concert) {
        // Optional: Show concert details
        Toast.makeText(this, concert.getTitle() + " - " + concert.getArtist(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            List<Concert> updatedConcerts = LocalDataHelper.loadConcerts(this);
            adapter.updateConcerts(updatedConcerts);
        }
    }
}