package com.example.client.adapters;

import android.content.Context;
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
import com.example.client.model.ClickedPostData;
import com.example.client.model.IpAddress;
import com.example.client.model.Photo;
import com.example.client.view.MainActivity;

import java.util.List;

public class RecAdapterProfilePics extends RecyclerView.Adapter<RecAdapterProfilePics.ViewHolder> {
    private List<Photo> photosList;
    private Context context;
    public RecAdapterProfilePics(List<Photo> list, Context context ) {
        this.photosList = list;
        this.context = context;
    }
    @NonNull
    @Override
    public RecAdapterProfilePics.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_photo_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecAdapterProfilePics.ViewHolder holder, int position) {
        Photo photo = photosList.get(position);
        Glide.with(holder.img.getContext())
                .load(IpAddress.ip + "/api/photos/getfile/"+photo.getId())
                .into(holder.img);


        holder.img.setOnClickListener(v->{
            ClickedPostData.setPostURL(IpAddress.ip + "/api/photos/getfile/"+photo.getId());
            ClickedPostData.setDescription(photo.getLastChange());
            ClickedPostData.setUsername(photo.getAlbum());
            ClickedPostData.setTags(photo.getTags());
            Log.d("logdev", photo.getLocation().toString());
            ClickedPostData.setLocation(photo.getLocation());
            ((MainActivity)context).setPost();
        });

    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);

            itemView.findViewById(R.id.cardView).setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,500));;

            img.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,500));




        }
    }
}