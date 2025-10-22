package com.example.a6012cem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class AdminRegisterActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin, tvFanRegister;
    private ProgressBar progressBar;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        tvFanRegister = findViewById(R.id.tvFanRegister);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> registerAdmin());

        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(AdminRegisterActivity.this, AdminLoginActivity.class);
            startActivity(intent);
            finish();
        });

        tvFanRegister.setOnClickListener(v -> {
            Intent intent = new Intent(AdminRegisterActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerAdmin() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (!validateInputs(fullName, email, password, confirmPassword)) {
            return;
        }

        showProgress(true);

        // Simulate network delay
        new android.os.Handler().postDelayed(
                () -> {
                    boolean success = dbHelper.addAdmin(fullName, email, password);

                    runOnUiThread(() -> {
                        showProgress(false);

                        if (success) {
                            Toast.makeText(AdminRegisterActivity.this,
                                    "Admin account created successfully!",
                                    Toast.LENGTH_LONG).show();

                            // Navigate to login screen
                            Intent intent = new Intent(AdminRegisterActivity.this, AdminLoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(AdminRegisterActivity.this,
                                    "Registration failed. Email may already exist.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }, 1500
        );
    }

    private boolean validateInputs(String fullName, String email, String password, String confirmPassword) {
        // Validate Full Name
        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return false;
        }

        if (fullName.length() < 2) {
            etFullName.setError("Please enter a valid full name");
            etFullName.requestFocus();
            return false;
        }

        // Validate Email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            etEmail.requestFocus();
            return false;
        }

        // Check if email already exists
        if (dbHelper.checkAdminExists(email)) {
            etEmail.setError("Email already registered");
            etEmail.requestFocus();
            return false;
        }

        // Validate Password
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

        // Validate Confirm Password
        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Please confirm your password");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void showProgress(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            btnRegister.setEnabled(false);
            tvLogin.setEnabled(false);
            tvFanRegister.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnRegister.setEnabled(true);
            tvLogin.setEnabled(true);
            tvFanRegister.setEnabled(true);
        }
    }
}