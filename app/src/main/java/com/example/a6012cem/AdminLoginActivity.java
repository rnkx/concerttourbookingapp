package com.example.a6012cem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class AdminLoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignUp, tvFanLogin;
    private ProgressBar progressBar;

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        // Initialize database helper and session manager
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);



        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvFanLogin = findViewById(R.id.tvFanLogin);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> loginAdmin());

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(AdminLoginActivity.this, AdminRegisterActivity.class);
            startActivity(intent);
        });

        tvFanLogin.setOnClickListener(v -> {
            Intent intent = new Intent(AdminLoginActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loginAdmin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!validateInputs(email, password)) {
            return;
        }

        showProgress(true);

        // Simulate network delay
        new android.os.Handler().postDelayed(
                () -> {
                    boolean isVerified = dbHelper.verifyAdmin(email, password);

                    runOnUiThread(() -> {
                        showProgress(false);

                        if (isVerified) {
                            Admin admin = dbHelper.getAdminByEmail(email);
                            if (admin != null) {
                                // Create login session
                                sessionManager.createLoginSession(
                                        admin.getId(),
                                        admin.getFullName(),
                                        admin.getEmail()
                                );

                                Toast.makeText(AdminLoginActivity.this,
                                        "Login successful!",
                                        Toast.LENGTH_SHORT).show();

                                navigateToDashboard();
                            }
                        } else {
                            Toast.makeText(AdminLoginActivity.this,
                                    "Invalid email or password",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }, 1500
        );
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
            tvSignUp.setEnabled(false);
            tvFanLogin.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            tvSignUp.setEnabled(true);
            tvFanLogin.setEnabled(true);
        }
    }
}