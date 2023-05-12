package com.example.client.model;

public class Tag {
    public int id;
    public String name;
    public int popularity;

    public Tag(int id, String name, int popularity) {
        this.id = id;
        this.name = name;
        this.popularity = popularity;
    }

    public int getId() {
        return id;
    }

    public Tag(String name, int popularity) {
        this.name = name;
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return "{\"name\":\""+name+"\",\"popularity\":1}";
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
}
