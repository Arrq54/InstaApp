package com.example.client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.client.databinding.FragmentAddPhotoBinding;
import com.example.client.databinding.FragmentHomeBinding;

public class AddPhoto extends Fragment {


    private FragmentAddPhotoBinding addPhotoBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        addPhotoBinding = FragmentAddPhotoBinding.inflate(getLayoutInflater());


        return addPhotoBinding.getRoot();
    }
}