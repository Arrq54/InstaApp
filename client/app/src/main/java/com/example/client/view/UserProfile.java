package com.example.client.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.client.adapters.RecAdapter;
import com.example.client.api.ProfilePhotosAPI;
import com.example.client.databinding.FragmentSearchBinding;
import com.example.client.databinding.FragmentUserProfileBinding;
import com.example.client.model.IpAddress;
import com.example.client.model.Photo;
import com.example.client.viewmodel.ProfilePhotosViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserProfile extends Fragment {

    private FragmentUserProfileBinding userProfileBinding;
    private ProfilePhotosViewModel profilePhotosViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        userProfileBinding = FragmentUserProfileBinding.inflate(getLayoutInflater());


        profilePhotosViewModel = new ViewModelProvider(UserProfile.this).get(ProfilePhotosViewModel.class);

        StaggeredGridLayoutManager staggeredGridLayoutManager
                = new StaggeredGridLayoutManager(3, LinearLayout.VERTICAL);
        userProfileBinding.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        profilePhotosViewModel.getObservedPhotos().observe(getViewLifecycleOwner(), s -> {
            RecAdapter adapter = new RecAdapter(s);
            userProfileBinding.recyclerView.setAdapter(adapter);
        });



        return userProfileBinding.getRoot();


    }
}