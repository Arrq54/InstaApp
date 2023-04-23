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

import com.example.client.adapters.RecAdapterHomePage;
import com.example.client.adapters.RecAdapterProfilePics;
import com.example.client.databinding.FragmentHomeBinding;
import com.example.client.viewmodel.HomePageViewModel;
import com.example.client.viewmodel.ProfilePhotosViewModel;


public class Home extends Fragment {

    private HomePageViewModel homePageViewModel;
    private FragmentHomeBinding homeBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        homeBinding = FragmentHomeBinding.inflate(getLayoutInflater());
        homePageViewModel = new ViewModelProvider(Home.this).get(HomePageViewModel.class);

        StaggeredGridLayoutManager staggeredGridLayoutManager
                = new StaggeredGridLayoutManager(1, LinearLayout.VERTICAL);

        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        staggeredGridLayoutManager.offsetChildrenVertical(16);


        homeBinding.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        homePageViewModel.setUp();
        homePageViewModel.getObservedPhotos().observe(getViewLifecycleOwner(), s -> {

            RecAdapterHomePage adapter = new RecAdapterHomePage(s);
            homeBinding.recyclerView.setAdapter(adapter);
        });

        return homeBinding.getRoot();
    }
}