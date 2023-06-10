package com.example.client.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.client.R;
import com.example.client.databinding.FragmentFilterBinding;
import com.example.client.model.Imager;


public class FilterFragment extends Fragment {

    private FragmentFilterBinding fragmentFilterBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentFilterBinding = FragmentFilterBinding.inflate(getLayoutInflater());

        fragmentFilterBinding.none.setOnClickListener(v->{
            Imager.filter = "";
            ((MainActivity)getActivity()).setAddPhotoUpload();
        });
        fragmentFilterBinding.retro.setOnClickListener(v->{
            Imager.filter = "retro";
            ((MainActivity)getActivity()).setAddPhotoUpload();
        });
        fragmentFilterBinding.grayscale.setOnClickListener(v->{
            Imager.filter = "grayscale";
            ((MainActivity)getActivity()).setAddPhotoUpload();
        });
        fragmentFilterBinding.pastelpink.setOnClickListener(v->{
            Imager.filter = "pastelpink";
            ((MainActivity)getActivity()).setAddPhotoUpload();
        });
        fragmentFilterBinding.goldenhour.setOnClickListener(v->{
            Imager.filter = "goldenhour";
            ((MainActivity)getActivity()).setAddPhotoUpload();
        });
        fragmentFilterBinding.cinematic.setOnClickListener(v->{
            Imager.filter = "cinematic";
            ((MainActivity)getActivity()).setAddPhotoUpload();
        });





        return fragmentFilterBinding.getRoot();
    }
}