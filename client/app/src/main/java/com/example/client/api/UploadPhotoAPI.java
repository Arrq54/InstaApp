package com.example.client.api;

import com.example.client.model.LocationForPhoto;
import com.example.client.model.Photo;
import com.example.client.model.UploadResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadPhotoAPI {

    @Multipart
    @POST("api/photos")
    Call<Photo> sendImage(
            @Part("album") RequestBody album,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file,
            @Part("location")LocationForPhoto location,
            @Part("filter")RequestBody filter
            );

}
