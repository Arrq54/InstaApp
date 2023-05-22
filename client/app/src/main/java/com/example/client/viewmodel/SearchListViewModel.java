package com.example.client.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.client.api.GetPhotosAPI;
import com.example.client.api.UsersAPI;
import com.example.client.model.IpAddress;
import com.example.client.model.Photo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchListViewModel  extends ViewModel {
    private MutableLiveData<List<String>> usersList;

    public void getSearchResult(String query){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IpAddress.ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UsersAPI usersAPI = retrofit.create(UsersAPI.class);
        Call<List<String>> call = usersAPI.getUsers(query);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                usersList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }
    public SearchListViewModel() {
        this.usersList = new MutableLiveData<>();

    }

    public MutableLiveData<List<String>> getUsersList() {
        return usersList;
    }
}

