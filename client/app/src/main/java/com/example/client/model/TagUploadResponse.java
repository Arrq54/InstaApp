package com.example.client.model;

public class TagUploadResponse {
    private String message;
    private boolean success;
    private int id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TagUploadResponse(String message, boolean success, int id) {
        this.message = message;
        this.success = success;
        this.id = id;
    }
}
