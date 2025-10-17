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
import com.google.firebase.auth.FirebaseAuthException;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private TextView loginTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginTextView = findViewById(R.id.loginTextView);
    }

    private void setupClickListeners() {
        registerButton.setOnClickListener(v -> attemptRegister());

        loginTextView.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void attemptRegister() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Basic validation
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Please fill all fields");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Please enter a valid email address");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showToast("Passwords don't match");
            return;
        }

        if (password.length() < 6) {
            showToast("Password must be at least 6 characters");
            return;
        }

        registerButton.setEnabled(false);
        registerButton.setText("Registering...");

        // Try Firebase registration
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    registerButton.setEnabled(true);
                    registerButton.setText("REGISTER");

                    if (task.isSuccessful()) {
                        // Success - save credentials for demo and navigate to login
                        saveCredentialsLocally(email, password);
                        showToast("Registration successful!");
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        // Handle different types of errors
                        handleRegistrationError(task.getException());
                    }
                });
    }

    private void handleRegistrationError(Exception exception) {
        if (exception == null) {
            showToast("Registration failed. Please try again.");
            return;
        }

        String errorMessage = exception.getMessage();

        if (errorMessage.contains("network error") ||
                errorMessage.contains("timeout") ||
                errorMessage.contains("unreachable") ||
                errorMessage.contains("connection")) {

            // Network error - save credentials locally for later sync
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            saveCredentialsLocally(email, password);

            showToast("No internet! Credentials saved locally. Try login when online.");
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();

        } else if (errorMessage.contains("email already in use")) {
            showToast("This email is already registered");
        } else if (errorMessage.contains("invalid email")) {
            showToast("Invalid email format");
        } else if (errorMessage.contains("weak password")) {
            showToast("Password is too weak");
        } else {
            showToast("Registration failed: " + errorMessage);
        }
    }

    private void saveCredentialsLocally(String email, String password) {
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putBoolean("pending_registration", true);
        editor.apply();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}