package com.example.client.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.client.R;
import com.example.client.databinding.FragmentAddPhotoUploadBinding;
import com.example.client.model.Imager;

public class AddPhotoUpload extends Fragment {
    private FragmentAddPhotoUploadBinding addPhotoUploadBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addPhotoUploadBinding = FragmentAddPhotoUploadBinding.inflate(getLayoutInflater());


        addPhotoUploadBinding.imgToUpload.setImageBitmap(Imager.bitmap);



        return addPhotoUploadBinding.getRoot();
    }
}