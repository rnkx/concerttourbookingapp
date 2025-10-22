package com.example.a6012cem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthHelper {
    private FirebaseAuth mAuth;

    public AuthHelper() {
        mAuth = FirebaseAuth.getInstance();
    }

    // Get current user ID
    public String getCurrentUserId() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }
        return null;
    }

    // Check if user is logged in
    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    // Sign in user (you can expand this with email/password)
    public void signInAnonymously(AuthCallback callback) {
        mAuth.signInAnonymously()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess("Signed in successfully");
                    } else {
                        callback.onError(task.getException().getMessage());
                    }
                });
    }

    public interface AuthCallback {
        void onSuccess(String message);
        void onError(String error);
    }
}