package com.example.client.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.client.api.GetPhotosAPI;
import com.example.client.model.IpAddress;
import com.example.client.model.Photo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfilePhotosViewModel extends ViewModel {
    private MutableLiveData<List<Photo>> photosList;
    private String username = "";

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUp(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IpAddress.ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetPhotosAPI getPhotosAPI = retrofit.create(GetPhotosAPI.class);

        Log.d("logdev", username);
        Call<List<Photo>> call = getPhotosAPI.gettAllPhotos(username);

        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                photosList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Log.d("logdev", t.toString());
                photosList.setValue(new ArrayList<>());
            }
        });
    }
    public ProfilePhotosViewModel() {
        this.photosList = new MutableLiveData<>();

    }

    public MutableLiveData<List<Photo>> getObservedPhotos() {
        return photosList;
    }



}
