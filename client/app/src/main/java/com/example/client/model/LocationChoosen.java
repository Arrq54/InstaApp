package com.example.client.model;

public class LocationChoosen {
    private static LocationForPhoto locationForPhoto;

    public static LocationForPhoto getLocationForPhoto() {
        return locationForPhoto;
    }

    public static void setLocationForPhoto(LocationForPhoto locationForPhoto) {
        LocationChoosen.locationForPhoto = locationForPhoto;
    }
}
