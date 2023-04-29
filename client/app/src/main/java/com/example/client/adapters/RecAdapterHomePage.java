package com.example.client.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.client.R;
import com.example.client.model.IpAddress;
import com.example.client.model.Photo;

import java.util.List;

public class RecAdapterHomePage extends RecyclerView.Adapter<RecAdapterHomePage.ViewHolder> {
    private List<Photo> photosList;
    public RecAdapterHomePage(List<Photo> list) {


        this.photosList = list;
    }
    @NonNull
    @Override
    public RecAdapterHomePage.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_page_post, parent, false);
        return new RecAdapterHomePage.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecAdapterHomePage.ViewHolder holder, int position) {
        Photo photo = photosList.get(position);
        Glide.with(holder.img.getContext())
                .load(IpAddress.ip + "/api/photos/getfile/"+photo.getId())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.photo);

            itemView.findViewById(R.id.homePagePost).setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1950));;

//            img.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,500));
        }
    }
}



