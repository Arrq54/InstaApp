package com.example.client.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.client.R;
import com.example.client.api.GetPhotosAPI;
import com.example.client.api.TagsAPI;
import com.example.client.databinding.FragmentTagsForPhotoBinding;
import com.example.client.model.IpAddress;
import com.example.client.model.Photo;
import com.example.client.model.Tag;
import com.example.client.model.TagChipInfo;
import com.example.client.model.UserData;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TagsForPhoto extends Fragment {
    private com.example.client.databinding.FragmentTagsForPhotoBinding tagsForPhotoBinding;
    private ArrayList<TagChipInfo> listOfTags;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        tagsForPhotoBinding = FragmentTagsForPhotoBinding.inflate(getLayoutInflater());
        listOfTags = UserData.getListofTags();
        refreshCurrentTags();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IpAddress.ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TagsAPI tagsAPI = retrofit.create(TagsAPI.class);

        Call<List<Tag>> call = tagsAPI.getAllTags();

        call.enqueue(new Callback<List<Tag>>() {
            @Override
            public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                LayoutInflater inflater = TagsForPhoto.this.getLayoutInflater();
                for(Tag tag: response.body()){
                    Chip chip = (Chip) inflater.inflate(R.layout.popular_tag_chip, null, false);
                    chip.setTag(tag.getId());
                    chip.setText(tag.getName());
                    tagsForPhotoBinding.popularTags.addView(chip);
                }
            }

            @Override
            public void onFailure(Call<List<Tag>> call, Throwable t) {
                Log.d("logdev", t.toString());
            }
        });

        tagsForPhotoBinding.popularTags.setOnCheckedStateChangeListener((group, checked)->{
            for (int i = 0; i < group.getChildCount(); i++) {
                Chip chip = (Chip) group.getChildAt(i);
                chip.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (chip.isChecked()) {
                        listOfTags.add(new TagChipInfo((Integer) chip.getTag(), (String) chip.getText()));

                    }else{
                        int ind=0;
                        for(TagChipInfo tci: listOfTags){
                            if(tci.getId() == (Integer)chip.getTag()){
                                listOfTags.remove(ind);
                                break;
                            }
                            ind++;
                        }
                    }
                    refreshCurrentTags();
                });
                
            }
        });

        tagsForPhotoBinding.addTagButton.setOnClickListener(btn->{
            if(tagsForPhotoBinding.customTag.getText().length() !=0){
                String text = tagsForPhotoBinding.customTag.getText().toString();
                if(!text.startsWith("#")){
                    text = "#" + text;
                }
                boolean flag = true;
                for(TagChipInfo tci: listOfTags){
                   if(tci.getName().equals(text)){
                       flag = false;
                   }
                }
                if(flag){
                    listOfTags.add(new TagChipInfo(123321,text ));
                    refreshCurrentTags();
                }

            }
        });

        tagsForPhotoBinding.confirmAddTags.setOnClickListener(v->{
            UserData.setListofTags(listOfTags);
            ((MainActivity)getActivity()).setAddPhotoUpload();
        });







        return tagsForPhotoBinding.getRoot();
    }
    private void refreshCurrentTags(){
        tagsForPhotoBinding.chosenTags.removeAllViews();
        LayoutInflater inflater = TagsForPhoto.this.getLayoutInflater();
        for(TagChipInfo t:listOfTags){
            Chip chip = (Chip) inflater.inflate(R.layout.popular_tag_chip, null, false);
            chip.setTag(t.getId());
            chip.setText(t.getName());
            tagsForPhotoBinding.chosenTags.addView(chip);
            chip.setOnClickListener(v->{
                int ind=0;
                for(TagChipInfo tci: listOfTags){
                    if(tci.getId() == (Integer)chip.getTag()){
                        listOfTags.remove(ind);
                        refreshCurrentTags();
                        break;
                    }
                    ind++;
                }
            });
        }
    }
}