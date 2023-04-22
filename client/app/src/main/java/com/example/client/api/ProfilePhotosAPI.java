package com.example.client.api;

import com.example.client.model.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProfilePhotosAPI {
    @GET("/api/photos/default")
    Call<List<Photo>> gettAllPhotos();
}
