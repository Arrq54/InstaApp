package com.example.client.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.client.R;
import com.example.client.api.UsersAPI;
import com.example.client.databinding.FragmentEditProfileBinding;
import com.example.client.model.Imager;
import com.example.client.model.IpAddress;
import com.example.client.model.UserData;
import com.example.client.model.UserInfoResponse;
import com.example.client.model.UserUpdateInfoResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class EditProfileFragment extends Fragment {
    private FragmentEditProfileBinding editProfileBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        editProfileBinding = FragmentEditProfileBinding.inflate(getLayoutInflater());

        Glide.with(getActivity())
                .load(IpAddress.ip + "/api/user/pfp/"+UserData.getId()).diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(editProfileBinding.pfp);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IpAddress.ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsersAPI usersAPI = retrofit.create(UsersAPI.class);

        Call<UserInfoResponse> call = usersAPI.postUserInfo(UserData.getUsername(), "Bearer " + UserData.getToken());
        final String[] id = {""};
        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                Log.d("logdev", response.body().toString());

                //TODO EDIT USER PROFILE DATA
                editProfileBinding.nickname.setText(response.body().getName());
                editProfileBinding.bio.setText(response.body().getBio());
                editProfileBinding.lastName.setText(response.body().getLastName());
                editProfileBinding.email.setText(response.body().getEmail());
                id[0] = response.body().getId();
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Log.d("logdev", t.toString());
            }
        });

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

                                editProfileBinding.pfp.setImageURI(Imager.uri);
//                                    ((MainActivity)getActivity()).setAddPhotoUpload();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        editProfileBinding.pfp.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");

            someActivityResultLauncher.launch(intent);

        });

        editProfileBinding.confirm.setOnClickListener(btn->{
            String filePath = "";
            if (Imager.uri != null) {
                Cursor cursor = (EditProfileFragment.this.getActivity().getContentResolver().query(Imager.uri, null, null, null, null));
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

            RequestBody idToUpdate = RequestBody.create(MultipartBody.FORM, id[0]);
            
            RequestBody bio = RequestBody.create(MultipartBody.FORM, editProfileBinding.bio.getText().toString());
            RequestBody email = RequestBody.create(MultipartBody.FORM, editProfileBinding.email.getText().toString());
            RequestBody lastName = RequestBody.create(MultipartBody.FORM, editProfileBinding.lastName.getText().toString());




            Call<UserUpdateInfoResponse> call2 = usersAPI.updateUserInfo(idToUpdate, bio, email, lastName, body);
            
            call2.enqueue(new Callback<UserUpdateInfoResponse>() {
                @Override
                public void onResponse(Call<UserUpdateInfoResponse> call, Response<UserUpdateInfoResponse> response) {
                    Log.d("logdev", "success");
                }

                @Override
                public void onFailure(Call<UserUpdateInfoResponse> call, Throwable t) {
                    Log.d("logdev", t.toString());
                }
            });
        });



        return editProfileBinding.getRoot();
    }
}