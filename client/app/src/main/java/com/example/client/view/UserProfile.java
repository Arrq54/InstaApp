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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.client.adapters.RecAdapterProfilePics;
import com.example.client.api.UsersAPI;
import com.example.client.databinding.FragmentUserProfileBinding;
import com.example.client.model.Bio;
import com.example.client.model.IpAddress;
import com.example.client.model.SearchListClickedItem;
import com.example.client.model.UserData;
import com.example.client.viewmodel.ProfilePhotosViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UserProfile extends Fragment {

    private FragmentUserProfileBinding userProfileBinding;
    private ProfilePhotosViewModel profilePhotosViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        userProfileBinding = FragmentUserProfileBinding.inflate(getLayoutInflater());



        profilePhotosViewModel = new ViewModelProvider(UserProfile.this).get(ProfilePhotosViewModel.class);


        String username = UserData.getUsername();


        if(SearchListClickedItem.username != null){
            username = SearchListClickedItem.username;
            profilePhotosViewModel.setUsername(username);
            profilePhotosViewModel.setUp();
        }else{
            getParentFragmentManager()
                    .setFragmentResultListener("username", this, (s, b) -> {
                        profilePhotosViewModel.setUsername(b.getString("username"));
                        profilePhotosViewModel.setUp();
                    });
        }





        userProfileBinding.username.setText(username);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IpAddress.ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsersAPI usersAPI = retrofit.create(UsersAPI.class);

        Call<Bio> call = usersAPI.getBioByName(username);

        call.enqueue(new Callback<Bio>() {
            @Override
            public void onResponse(Call<Bio> call, Response<Bio> response) {
                userProfileBinding.bio.setText(response.body().getBio());
            }

            @Override
            public void onFailure(Call<Bio> call, Throwable t) {
                Log.d("logdev", t.toString());
            }
        });



        StaggeredGridLayoutManager staggeredGridLayoutManager
                = new StaggeredGridLayoutManager(3, LinearLayout.VERTICAL);
        userProfileBinding.recyclerView.setLayoutManager(staggeredGridLayoutManager);
        profilePhotosViewModel.getObservedPhotos().observe(getViewLifecycleOwner(), s -> {
            RecAdapterProfilePics adapter = new RecAdapterProfilePics(s, getActivity());
            userProfileBinding.recyclerView.setAdapter(adapter);
        });


        
        Glide.with(getActivity()).load(IpAddress.ip + "/api/user/pfpbyname/"+username).diskCacheStrategy(DiskCacheStrategy.NONE).into(userProfileBinding.pfp);





        return userProfileBinding.getRoot();


    }
}