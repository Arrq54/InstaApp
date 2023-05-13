package com.example.client.model;

import java.util.Arrays;

public class TagUploadResponse {
    private String message;
    private boolean success;
    private int[] ids;

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "TagUploadResponse{" +
                "message='" + message + '\'' +
                ", success=" + success +
                ", ids=" + Arrays.toString(ids) +
                '}';
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

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    public TagUploadResponse(String message, boolean success, int[] ids) {
        this.message = message;
        this.success = success;
        this.ids = ids;
    }
}
