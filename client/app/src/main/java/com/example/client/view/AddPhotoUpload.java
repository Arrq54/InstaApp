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
import com.example.client.model.Tag;
import com.example.client.model.TagChipInfo;
import com.example.client.model.TagUploadResponse;
import com.example.client.model.UploadResponse;
import com.example.client.model.UserData;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
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
                        Log.d("logdev", "response");
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
        int totalTagsQuantity = UserData.getListofTags().size();

        int[] arrayOfTagsId = new int[totalTagsQuantity];
        Arrays.fill(arrayOfTagsId, -1);
        int counter = 0;

        for(TagChipInfo tci: UserData.getListofTags()){
            if(tci.getId() != 123321){
                arrayOfTagsId[counter] = tci.getId();
                counter++;
            }
        }
        int tagsToUploadTotal = totalTagsQuantity - counter;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IpAddress.ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TagsAPI tagsAPI = retrofit.create(TagsAPI.class);
        
        String[] tagsToUpload = new String[tagsToUploadTotal];
        int tagsToUploadCounter = 0;
        for(TagChipInfo tci: UserData.getListofTags()){
            if(tci.getId() == 123321){
                tagsToUpload[tagsToUploadCounter] = tci.getName();
                tagsToUploadCounter++; 
            }
        }
        Call<TagUploadResponse> call = tagsAPI.uploadTags(tagsToUpload);
        final int[] finalCounter = {counter};
        call.enqueue(new Callback<TagUploadResponse>() {
            @Override
            public void onResponse(Call<TagUploadResponse> call, Response<TagUploadResponse> response) {

                for(int i: response.body().getIds()){
                    arrayOfTagsId[finalCounter[0]] = i;
                    finalCounter[0]++;
                }
                Call<Photo> setTagsForPhotoById = tagsAPI.setTagsForPhoto(id, arrayOfTagsId);

                setTagsForPhotoById.enqueue(new Callback<Photo>() {
                    @Override
                    public void onResponse(Call<Photo> call, Response<Photo> response) {
                        ((MainActivity)getActivity()).setHomeFragment();
                    }

                    @Override
                    public void onFailure(Call<Photo> call, Throwable t) {

                    }
                });


            }

            @Override
            public void onFailure(Call<TagUploadResponse> call, Throwable t) {
                Log.d("logdev", t.toString());
            }
        });
    }
}

