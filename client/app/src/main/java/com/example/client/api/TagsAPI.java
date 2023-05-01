package com.example.client.api;

import com.example.client.model.Photo;
import com.example.client.model.Tag;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TagsAPI {
    @GET("/api/tags")
    Call<List<Tag>> getAllTags();
}
