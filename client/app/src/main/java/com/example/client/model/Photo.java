package com.example.client.model;

import java.util.ArrayList;

public class Photo {
    public String id;
    public String album;
    public String originalName;
    public String url;
    public String lastChange;
    public ArrayList<History> history;
    public ArrayList<Tag> tags;
    public LocationForPhoto location;

    public LocationForPhoto getLocation() {
        return location;
    }

    public void setLocation(LocationForPhoto location) {
        this.location = location;
    }

    public Photo(String id, String album, String originalName, String url, String lastChange, ArrayList<History> history, ArrayList<Tag> tags, LocationForPhoto location) {
        this.id = id;
        this.album = album;
        this.originalName = originalName;
        this.url = url;
        this.lastChange = lastChange;
        this.history = history;
        this.tags = tags;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLastChange() {
        return lastChange;
    }

    public void setLastChange(String lastChange) {
        this.lastChange = lastChange;
    }

    public ArrayList<History> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<History> history) {
        this.history = history;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                ", album='" + album + '\'' +
                ", originalName='" + originalName + '\'' +
                ", url='" + url + '\'' +
                ", lastChange='" + lastChange + '\'' +
                ", history=" + history +
                ", tags=" + tags +
                ", location=" + location +
                '}';
    }
}
