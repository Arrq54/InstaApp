package com.example.client.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.client.adapters.RecAdapterProfilePics;
import com.example.client.databinding.FragmentUserProfileBinding;
import com.example.client.viewmodel.ProfilePhotosViewModel;


public class UserProfile extends Fragment {

    private FragmentUserProfileBinding userProfileBinding;
    private ProfilePhotosViewModel profilePhotosViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        userProfileBinding = FragmentUserProfileBinding.inflate(getLayoutInflater());


        profilePhotosViewModel = new ViewModelProvider(UserProfile.this).get(ProfilePhotosViewModel.class);

        getParentFragmentManager()
                .setFragmentResultListener("username", this, (s, b) -> {
                    profilePhotosViewModel.setUsername(b.getString("username"));
                    profilePhotosViewModel.setUp();
                });

        StaggeredGridLayoutManager staggeredGridLayoutManager
                = new StaggeredGridLayoutManager(3, LinearLayout.VERTICAL);
        userProfileBinding.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        profilePhotosViewModel.getObservedPhotos().observe(getViewLifecycleOwner(), s -> {
            RecAdapterProfilePics adapter = new RecAdapterProfilePics(s);
            userProfileBinding.recyclerView.setAdapter(adapter);
        });



        return userProfileBinding.getRoot();


    }
}