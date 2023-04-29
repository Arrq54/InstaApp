package com.example.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Log;
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


        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Log.d("logdev", data.getDataString());
                            //TODO TUTAJ RETROFIT UPLOAD ZDJECIA ITP
                        }
                    }
                });
        addPhotoBinding.openGallery.setOnClickListener(v->{
            Log.d("logdev", "click");
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");

            someActivityResultLauncher.launch(intent);

        });
        


        return addPhotoBinding.getRoot();
    }

}