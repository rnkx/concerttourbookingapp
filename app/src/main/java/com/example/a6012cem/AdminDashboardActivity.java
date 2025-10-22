package com.example.a6012cem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button btnManageConcerts;
    private Button btnViewBookings;

    private Button btnLogout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Bind buttons safely
        btnManageConcerts = findViewById(R.id.btnManageConcerts);
        btnViewBookings = findViewById(R.id.btnViewBookings);
        btnLogout = findViewById(R.id.btnLogout);

        // ✅ Manage Concerts button
        btnManageConcerts.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(AdminDashboardActivity.this, ManageConcertsActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "⚠️ Manage Concerts screen not found.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        // ✅ View Orders button
        btnViewBookings.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(AdminDashboardActivity.this, ManageReservationActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "⚠️ View Orders screen not found.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });



        // ✅ Logout button
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminDashboardActivity.this, AdminLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
