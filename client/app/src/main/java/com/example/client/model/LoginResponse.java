package com.example.client.model;

public class LoginResponse {
    private boolean success;
    private String token;
    private String username;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LoginResponse(boolean success, String token, String username) {
        this.success = success;
        this.token = token;
        this.username = username;
    }

    public LoginResponse(boolean success, String token) {
        this.success = success;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "success=" + success +
                ", token='" + token + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
