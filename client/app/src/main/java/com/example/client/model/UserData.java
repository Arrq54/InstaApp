package com.example.client.model;

import java.util.ArrayList;

public class UserData {
    private static String token;
    private static String username;
    private static ArrayList<TagChipInfo> listofTags = new ArrayList<>();


    public static ArrayList<TagChipInfo> getListofTags() {
        return listofTags;
    }

    public static void setListofTags(ArrayList<TagChipInfo> listofTags) {
        UserData.listofTags = listofTags;
    }

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
