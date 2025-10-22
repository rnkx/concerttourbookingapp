package com.example.a6012cem;

public class ImageUploadHelper {

    public interface UploadCallback {
        void onSuccess(String imageUrl);
        void onFailure(String errorMessage);
    }

    // Simple method that returns a placeholder URL
    public void uploadImage(String dummyParam, UploadCallback callback) {
        // For now, just return success with empty URL
        callback.onSuccess("");
    }
}