package com.example.a6012cem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, adminLoginButton;
    private TextView registerTextView;
    private FirebaseAuth mAuth;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();




        initializeViews();
        setupClickListeners();
        loadDemoCredentials();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        adminLoginButton = findViewById(R.id.adminLoginButton);
        registerTextView = findViewById(R.id.registerTextView);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());

        // Admin login button listener
        adminLoginButton.setOnClickListener(v -> {
            navigateToAdminLogin();
        });

        registerTextView.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showToast("Please enter email and password");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Please enter a valid email address");
            return;
        }

        // Check if user is trying to login as admin with regular login
        if (isAdminEmail(email)) {
            showToast("Please use Admin Login for admin accounts");
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        // Try Firebase authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    loginButton.setEnabled(true);
                    loginButton.setText("LOGIN");

                    if (task.isSuccessful()) {
                        // Login successful
                        showToast("Login successful!");
                        navigateToHome();
                    } else {
                        // Handle login errors
                        handleLoginError(task.getException(), email, password);
                    }
                });
    }

    private void navigateToAdminLogin() {
        Toast.makeText(this, "Admin Login Button Clicked!", Toast.LENGTH_LONG).show();
        try {
            Intent intent = new Intent(LoginActivity.this, AdminLoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            Toast.makeText(this, "Opening Admin Login...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    private void handleLoginError(Exception exception, String email, String password) {
        if (exception == null) {
            showToast("Login failed. Please try again.");
            return;
        }

        String errorMessage = exception.getMessage();

        if (errorMessage.contains("network error") ||
                errorMessage.contains("timeout") ||
                errorMessage.contains("unreachable") ||
                errorMessage.contains("connection")) {

            // Network error - check if we have locally saved credentials
            if (hasValidLocalCredentials(email, password)) {
                showToast("No internet! Using local credentials. Limited functionality.");
                navigateToHome(); // Allow offline access
            } else {
                showToast("No internet connection and no local account found");
            }

        } else if (errorMessage.contains("user not found") || errorMessage.contains("no user record")) {
            showToast("No account found with this email");
        } else if (errorMessage.contains("wrong password") || errorMessage.contains("invalid password")) {
            showToast("Incorrect password");
        } else if (errorMessage.contains("invalid email")) {
            showToast("Invalid email format");
        } else if (errorMessage.contains("The email address is already in use by another account")) {
            showToast("Email already in use");
        } else if (errorMessage.contains("We have blocked all requests from this device due to unusual activity.")) {
            showToast("Too many attempts. Try again later.");
        } else {
            showToast("Login failed: " + errorMessage);
        }
    }

    private boolean hasValidLocalCredentials(String email, String password) {
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String savedEmail = prefs.getString("email", "");
        String savedPassword = prefs.getString("password", "");

        return email.equals(savedEmail) && password.equals(savedPassword);
    }

    private boolean isAdminUser(String email) {
        if (email == null) return false;
        SharedPreferences adminPrefs = getSharedPreferences("AdminData", MODE_PRIVATE);
        String savedAdminEmail = adminPrefs.getString("adminEmail", "");
        return email.equals(savedAdminEmail) || email.contains("admin");
    }

    private boolean isAdminEmail(String email) {
        return email != null &&
                (email.contains("admin") ||
                        email.contains("administrator") ||
                        email.equals("admin@concert.com"));
    }

    private void loadDemoCredentials() {
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String savedEmail = prefs.getString("email", "");
        String savedPassword = prefs.getString("password", "");

        // If no saved credentials, use demo data
        if (savedEmail.isEmpty()) {
            savedEmail = "demo@concert.com";
            savedPassword = "demo123";

            // Save demo credentials
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", savedEmail);
            editor.putString("password", savedPassword);
            editor.apply();
        }

        // Only auto-fill if it's the demo account (not admin)
        if (!savedEmail.contains("admin")) {
            emailEditText.setText(savedEmail);
            passwordEditText.setText(savedPassword);
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void navigateToAdminDashboard() {
        Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadDemoCredentials();
    }

    @Override
    protected void onResume() {
        super.onResume();
        passwordEditText.setText("");
    }

    @Override
    public void onBackPressed() {
        // Double tap to exit with 2 second interval
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity(); // Complete exit only after double tap
            return;
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}