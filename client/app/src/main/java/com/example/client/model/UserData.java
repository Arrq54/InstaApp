package com.example.client.model;

public class UserData {
    private static String token;
    private static String username;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        UserData.token = token;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserData.username = username;
    }

    public UserData() {
    }
}
