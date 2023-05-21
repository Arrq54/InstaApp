package com.example.client.model;

import java.util.ArrayList;

public class ClickedPostData {
    private static String username;
    private static String postURL;
    private static String description;
    private static ArrayList<Tag> tags;
    private static LocationForPhoto location;

    public static LocationForPhoto getLocation() {
        return location;
    }

    public static void setLocation(LocationForPhoto location) {
        ClickedPostData.location = location;
    }

    public static ArrayList<Tag> getTags() {
        return tags;
    }

    public static void setTags(ArrayList<Tag> tags) {
        ClickedPostData.tags = tags;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        ClickedPostData.username = username;
    }

    public static String getPostURL() {
        return postURL;
    }

    public static void setPostURL(String postURL) {
        ClickedPostData.postURL = postURL;
    }

    public static String getDescription() {
        return description;
    }

    public static void setDescription(String description) {
        ClickedPostData.description = description;
    }
}
