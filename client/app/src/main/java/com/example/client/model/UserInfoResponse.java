package com.example.client.model;

public class UserInfoResponse {
    private String name;
    private String lastName;
    private String email;
    private String bio;
    private String pfp;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPfp() {
        return pfp;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

    public UserInfoResponse(String name, String lastName, String email, String bio) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "UserInfoResponse{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", bio='" + bio + '\'' +
                ", pfp='" + pfp + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
