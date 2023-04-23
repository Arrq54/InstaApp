package com.example.client.api;

import com.example.client.model.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetPhotosAPI {
    @GET("/api/photos/{album}")
    Call<List<Photo>> gettAllPhotos(@Path("album") String album);

    @GET("/api/photos")
    Call<List<Photo>> gettHomePhotos();
}
