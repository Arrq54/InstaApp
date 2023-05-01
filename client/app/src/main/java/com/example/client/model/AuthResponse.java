package com.example.client.model;

public class AuthResponse {
    private boolean success;

    public AuthResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
