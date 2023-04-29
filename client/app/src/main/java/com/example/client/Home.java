package com.example.client;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.client.databinding.FragmentHomeBinding;


public class Home extends Fragment {


    private FragmentHomeBinding homeBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        homeBinding = FragmentHomeBinding.inflate(getLayoutInflater());



        return homeBinding.getRoot();
    }
}