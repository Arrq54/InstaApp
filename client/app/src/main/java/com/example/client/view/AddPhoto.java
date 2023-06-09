package com.example.client.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.client.databinding.FragmentAddPhotoBinding;
import com.example.client.databinding.FragmentHomeBinding;
import com.example.client.model.Imager;
import com.example.client.model.PostType;

import java.io.IOException;
import java.net.URI;

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
                            try {
//                                Imager.bitmap =  MediaStore.Images.Media.getBitmap(AddPhoto.this.getActivity().getContentResolver(), Uri.parse(data.getDataString()));
                                Imager.uri = Uri.parse(data.getDataString());
                                ((MainActivity)getActivity()).setAddPhotoUpload();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        addPhotoBinding.openGallery.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Choose a post type")
                    .setPositiveButton("Photo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            Imager.type = PostType.PHOTO;
                            someActivityResultLauncher.launch(intent);
                        }
                    })
                    .setNegativeButton("Video", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("video/*");
                            Imager.type = PostType.VIDEO;
                            someActivityResultLauncher.launch(intent);
                        }
                    })
                    .setCancelable(true); // Allow cancellation by tapping outside the dialog or pressing the back button

            AlertDialog dialog = builder.create();
            dialog.show();



        });

        addPhotoBinding.camera.setOnClickListener(v->{
            ((MainActivity)getActivity()).setCameraFragment();
        });
        


        return addPhotoBinding.getRoot();
    }

}