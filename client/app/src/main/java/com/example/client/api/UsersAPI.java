package com.example.client.api;

import com.example.client.model.AuthResponse;
import com.example.client.model.LoginResponse;
import com.example.client.model.Token;
import com.example.client.model.UserInfoResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UsersAPI {

    @FormUrlEncoded
    @POST("/api/user/register")
    Call<Token> register(
            @Field("name") String name,
            @Field("lastName") String lastName,
            @Field("email") String email,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("/api/user/login")
    Call<LoginResponse> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @POST("/api/user/auth")
    Call<AuthResponse> postAuthData(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("/api/user/getInfo")
    Call<UserInfoResponse> postUserInfo(@Field("username") String username, @Header("Authorization") String token);
}
