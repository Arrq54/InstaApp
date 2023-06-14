package com.example.client.view;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.client.R;
import com.example.client.databinding.FragmentPostBinding;
import com.example.client.model.ClickedPostData;
import com.example.client.model.IpAddress;
import com.example.client.model.PostType;
import com.example.client.model.SearchListClickedItem;
import com.example.client.model.Tag;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;


public class PostFragment extends Fragment {

    private FragmentPostBinding postBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        postBinding = FragmentPostBinding.inflate(getLayoutInflater());

        Log.d("logdev", ClickedPostData.getPostURL());


        postBinding.username.setText(ClickedPostData.getUsername());
        postBinding.description.setText(ClickedPostData.getDescription());
        postBinding.usernamePost.setText(ClickedPostData.getUsername());


        postBinding.location.setText(ClickedPostData.getLocation().getName());


        postBinding.location.setOnClickListener(tv->{
            Intent intent = new Intent(getActivity(), ShowLocationOnMap.class);
            intent.putExtra("id", ClickedPostData.getLocation().getId());
            startActivity(intent);
        });



        ArrayList<Tag> tags = ClickedPostData.getTags();


        if(ClickedPostData.getFiletype() == PostType.VIDEO){
            postBinding.photo.setVisibility(ImageView.GONE);
            postBinding.video.setVisibility(VideoView.VISIBLE);

            String imageUrl = ClickedPostData.getPostURL();


            ExoPlayer player = new ExoPlayer.Builder(getContext()).build();
            postBinding.video.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(imageUrl));

            player.setMediaItem(mediaItem);

            player.prepare();

            player.play();

        }else{

            Glide.with(getContext())
                    .load(ClickedPostData.getPostURL())
                    .into(postBinding.photo);
        }


        Glide.with(getActivity()).load(IpAddress.ip + "/api/user/pfpbyname/"+ClickedPostData.getUsername())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(postBinding.pfp);


        postBinding.pfp.setOnClickListener(v->{
            SearchListClickedItem.username = ClickedPostData.getUsername();
            ((MainActivity)getContext()).setUseProfile();
        });



        for(Tag tag: tags){
            Chip chip = (Chip) inflater.inflate(R.layout.post_tag_chip, null, false);
            chip.setTag(tag.getId());
            chip.setText(tag.getName());
            postBinding.tags.addView(chip);
        }
        
        return postBinding.getRoot();
    }
}