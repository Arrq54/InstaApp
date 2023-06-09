package com.example.client.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.client.R;
import com.example.client.databinding.FragmentCameraBinding;
import com.example.client.model.Imager;
import com.example.client.model.PostType;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;


public class CameraFragment extends Fragment {
    private FragmentCameraBinding cameraBinding;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private VideoCapture videoCapture;
    private PostType postType = PostType.PHOTO;
    private boolean facingBack = true;
    private boolean recording = false;


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cameraBinding = FragmentCameraBinding.inflate(getLayoutInflater());


        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();
                bindPreview(cameraProvider, cameraSelector);
            } catch (InterruptedException | ExecutionException e) {
                // No errors need to be handled for this Future. This should never be reached.
            }
        }, ContextCompat.getMainExecutor(getContext()));


        cameraBinding.photo.setOnClickListener(v -> {
            postType = PostType.PHOTO;
            checkIfVideoOrPhoto();
        });

        cameraBinding.video.setOnClickListener(v -> {
            postType = PostType.VIDEO;
            checkIfVideoOrPhoto();
        });
        Vibrator vibe = (Vibrator) getActivity().getSystemService(getContext().VIBRATOR_SERVICE);


        cameraBinding.takePhoto.setOnClickListener(v -> {

            vibe.vibrate(100);
            if (postType == PostType.PHOTO) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis());
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

                ImageCapture.OutputFileOptions outputFileOptions =
                        new ImageCapture.OutputFileOptions.Builder(
                                getActivity().getContentResolver(),
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                contentValues)
                                .build();
                imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(getContext()),
                        new ImageCapture.OnImageSavedCallback() {
                            @Override
                            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                                Imager.uri = outputFileResults.getSavedUri();
                                ((MainActivity) getActivity()).setAddPhotoUpload();


                            }

                            @Override
                            public void onError(@NonNull ImageCaptureException exception) {
                                // error
                            }
                        });
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, System.currentTimeMillis());
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");

                if (!recording) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    cameraBinding.recording.setVisibility(View.VISIBLE);
                    recording = true;
                    videoCapture.startRecording(
                            new VideoCapture.OutputFileOptions.Builder(
                                    getContext().getContentResolver(),
                                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                    contentValues
                            ).build(),
                            ContextCompat.getMainExecutor(getContext()),
                            new VideoCapture.OnVideoSavedCallback() {
                                @Override
                                public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {

                                    Imager.type = PostType.VIDEO;
                                    Imager.uri = outputFileResults.getSavedUri();
                                    ((MainActivity) getActivity()).setAddPhotoUpload();

                                }

                                @Override
                                public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                                    // error
                                }
                            });
                }else{
                    //recording
                    cameraBinding.recording.setVisibility(View.GONE);
                    videoCapture.stopRecording();

                }
            }
        });




        cameraBinding.rotate.setOnClickListener(v->{
            vibe.vibrate(75);
            CameraSelector cs;
            if(facingBack){
                cs = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                        .build();
            }else{
                cs = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();
            }
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                bindPreview(cameraProvider, cs);
            } catch (InterruptedException | ExecutionException ignored) {}
            facingBack = !facingBack;
        });





        return cameraBinding.getRoot();
    }
    private void checkIfVideoOrPhoto(){
        if(postType == PostType.PHOTO){
            cameraBinding.photo.setTextColor(0xff000000);
            cameraBinding.video.setTextColor(0xff6b6b6b);
        }else{
            cameraBinding.video.setTextColor(0xff000000);
            cameraBinding.photo.setTextColor(0xff6b6b6b);
        }
    }

    @SuppressLint("RestrictedApi")
    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider, CameraSelector cameraSelector) {
        Preview preview = new Preview.Builder().build();

        cameraProvider.unbindAll();
        imageCapture =
                new ImageCapture.Builder()
                        .setTargetRotation(cameraBinding.previewView.getDisplay().getRotation())
                        .build();


        Log.d("logdev", "bindpreview");
        
        videoCapture =
                new VideoCapture.Builder()
                        .setTargetRotation(cameraBinding.previewView.getDisplay().getRotation())
                        .build();


        preview.setSurfaceProvider(cameraBinding.previewView.getSurfaceProvider());

        cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, videoCapture, preview);

    }

}
