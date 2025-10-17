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
    private Button loginButton;
    private TextView registerTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToHome();
            return;
        }

        initializeViews();
        setupClickListeners();
        loadDemoCredentials();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);
    }

    private void setupClickListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());

        registerTextView.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
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

        emailEditText.setText(savedEmail);
        passwordEditText.setText(savedPassword);
        showToast("Demo credentials loaded. Press Login to continue.");
    }

    private void navigateToHome() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Quick check for demo data on app start
        loadDemoCredentials();
    }
}