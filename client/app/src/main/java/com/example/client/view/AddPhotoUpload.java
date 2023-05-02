package com.example.client.view;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.client.R;
import com.example.client.api.GetPhotosAPI;
import com.example.client.api.TagsAPI;
import com.example.client.api.UploadPhotoAPI;
import com.example.client.databinding.FragmentAddPhotoUploadBinding;
import com.example.client.model.Imager;
import com.example.client.model.IpAddress;
import com.example.client.model.Photo;
import com.example.client.model.TagChipInfo;
import com.example.client.model.TagUploadResponse;
import com.example.client.model.UploadResponse;
import com.example.client.model.UserData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddPhotoUpload extends Fragment {
    private FragmentAddPhotoUploadBinding addPhotoUploadBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addPhotoUploadBinding = FragmentAddPhotoUploadBinding.inflate(getLayoutInflater());


        try {
            addPhotoUploadBinding.imgToUpload.setImageBitmap( MediaStore.Images.Media.getBitmap(AddPhotoUpload.this.getActivity().getContentResolver(), Imager.uri));
        } catch (IOException e) {
            e.printStackTrace();
        };

        if(Imager.description != null){
            addPhotoUploadBinding.postDescription.setText(Imager.description);
        }

        addPhotoUploadBinding.addTags.setText("TAGS ("+UserData.getListofTags().size()+")");

        addPhotoUploadBinding.confirmPost.setOnClickListener(v->{
            if(addPhotoUploadBinding.postDescription.getText().length()>0){
                String filePath = null;
                if (Imager.uri != null) {
                    Cursor cursor = (AddPhotoUpload.this.getActivity().getContentResolver().query(Imager.uri, null, null, null, null));
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        filePath = cursor.getString(columnIndex);
                        cursor.close();
                    }
                }


                File file = new File(filePath);

                RequestBody fileRequest = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fileRequest);
                RequestBody album = RequestBody.create(MultipartBody.FORM, UserData.getUsername());
                RequestBody description = RequestBody.create(MultipartBody.FORM, addPhotoUploadBinding.postDescription.getText().toString());

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(IpAddress.ip)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                UploadPhotoAPI uploadPhotoAPI = retrofit.create(UploadPhotoAPI.class);

                Call<Photo> call = uploadPhotoAPI.sendImage(album, description, body);
                call.enqueue(new Callback<Photo>() {
                    @Override
                    public void onResponse(Call<Photo> call, Response<Photo> response) {
                        uploadTagsForPhoto(response.body().getId());
                    }

                    @Override
                    public void onFailure(Call<Photo> call, Throwable t) {
                        Log.d("logdev", t.toString());
                    }
                });
            }
        });


        addPhotoUploadBinding.addTags.setOnClickListener(v->{
            Imager.description = addPhotoUploadBinding.postDescription.getText().toString();
            ((MainActivity)getActivity()).setTagsForPhoto();
        });


        return addPhotoUploadBinding.getRoot();
    }
    private void uploadTagsForPhoto(String id){
        ArrayList<String> newCustomTags = new ArrayList<>();
        ArrayList<Integer> uploadedIds = new ArrayList<>();
        for(TagChipInfo tci: UserData.getListofTags()){
            if(tci.getId() == 123321){
                newCustomTags.add(tci.getName());
            }else{
                uploadedIds.add(tci.getId());
            }
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IpAddress.ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TagsAPI tagsAPI= retrofit.create(TagsAPI.class);


        for(String s: newCustomTags){
            Call<TagUploadResponse> call = tagsAPI.uploadTag(s,1);
            call.enqueue(new Callback<TagUploadResponse>() {
                @Override
                public void onResponse(Call<TagUploadResponse> call, Response<TagUploadResponse> response) {
                    Log.d("logdev", String.valueOf(response.body().getId()));
                    uploadedIds.add(response.body().getId());
                }

                @Override
                public void onFailure(Call<TagUploadResponse> call, Throwable t) {
                    Log.d("logdev", t.toString());
                }
            });
        }


        Log.d("logdev", uploadedIds.toString());
        int[] intArray = new int[uploadedIds.size()];
        for (int i = 0; i < uploadedIds.size(); i++) {
            intArray[i] = uploadedIds.get(i);
        }

        Log.d("logdev", Arrays.toString(intArray));


/*
*
*           FIXME DZIALAJA TAGI Z SERVERA, ALE NIE MA CZEKANIA NA ID Z CUSTOMOWYO DODANYCH TAGOW, TRZEBA JAKOS ASYNCHRONICZNIE. POWODZONKA :)
*
* */



        
        Call<Photo> call = tagsAPI.setTagsForPhoto(id, intArray);
        call.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                Log.d("logdev", response.body().toString());
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                Log.d("logdev", t.toString());
            }
        });





    }
}