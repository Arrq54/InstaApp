package com.example.client.api;

import com.example.client.model.Photo;
import com.example.client.model.Tag;
import com.example.client.model.TagUploadResponse;
import com.example.client.model.Token;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TagsAPI {
    @GET("/api/tags")
    Call<List<Tag>> getAllTags();

    @FormUrlEncoded
    @POST("/api/tags/add")
    Call<TagUploadResponse> uploadTags(
            @Field("tags") String[] tags
    );

    @FormUrlEncoded
    @PATCH("/api/photos/tags/mass")
    Call<Photo> setTagsForPhoto(
            @Field("photoid") String id,
            @Field("ids")int[] list
            );
}
