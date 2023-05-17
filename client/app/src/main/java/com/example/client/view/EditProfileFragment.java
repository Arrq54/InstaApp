package com.example.client.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.client.R;
import com.example.client.api.UsersAPI;
import com.example.client.databinding.FragmentEditProfileBinding;
import com.example.client.model.IpAddress;
import com.example.client.model.UserData;
import com.example.client.model.UserInfoResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EditProfileFragment extends Fragment {
    private FragmentEditProfileBinding editProfileBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        editProfileBinding = FragmentEditProfileBinding.inflate(getLayoutInflater());



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IpAddress.ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsersAPI usersAPI = retrofit.create(UsersAPI.class);

        Call<UserInfoResponse> call = usersAPI.postUserInfo(UserData.getUsername(), "Bearer " + UserData.getToken());

        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                Log.d("logdev", response.body().toString());
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Log.d("logdev", t.toString());
            }
        });



        return editProfileBinding.getRoot();
    }
}