package com.example.client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.client.databinding.FragmentSearchBinding;
import com.example.client.databinding.FragmentUserProfileBinding;


public class UserProfile extends Fragment {

    private FragmentUserProfileBinding userProfileBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        userProfileBinding = FragmentUserProfileBinding.inflate(getLayoutInflater());

        return userProfileBinding.getRoot();



    }
}