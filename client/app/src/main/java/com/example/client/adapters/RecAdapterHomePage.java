package com.example.client.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.client.R;
import com.example.client.model.ClickedPostData;
import com.example.client.model.IpAddress;
import com.example.client.model.Photo;
import com.example.client.model.Tag;
import com.example.client.view.MainActivity;

import java.util.List;

public class RecAdapterHomePage extends RecyclerView.Adapter<RecAdapterHomePage.ViewHolder> {
    private List<Photo> photosList;
    private Context context;
    public RecAdapterHomePage(List<Photo> list, Context context) {


        this.photosList = list;
        this.context = context;
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
//        holder.username.setText(photo.getAlbum());
//        holder.username2.setText(photo.getAlbum());

//        holder.description.setText(photo.getLastChange());

        holder.img.setOnClickListener(v->{
            ClickedPostData.setPostURL(IpAddress.ip + "/api/photos/getfile/"+photo.getId());
            ClickedPostData.setDescription(photo.getLastChange());
            ClickedPostData.setUsername(photo.getAlbum());
            ClickedPostData.setTags(photo.getTags());
            ((MainActivity)context).setPost();
        });



    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView username;
        private TextView username2;
        private TextView description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.photo);
//            username = itemView.findViewById(R.id.usernamePost);
//            username2 = itemView.findViewById(R.id.username);
//            description = itemView.findViewById(R.id.description);
            itemView.findViewById(R.id.homePagePost).setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,875));;

//            img.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,500));
        }
    }
}



