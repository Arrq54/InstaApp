package com.example.client.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.client.R;
import com.example.client.model.ClickedPostData;
import com.example.client.model.IpAddress;
import com.example.client.model.Photo;
import com.example.client.model.SearchListClickedItem;
import com.example.client.model.Tag;
import com.example.client.view.MainActivity;

import java.util.List;

public class RecAdapterSearchList extends RecyclerView.Adapter<RecAdapterSearchList.ViewHolder2> {
    private List<String> list;
    private Context context;

    public void setList(List<String> list) {
        this.list = list;
    }

    public RecAdapterSearchList(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public RecAdapterSearchList.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result, parent, false);
        return new RecAdapterSearchList.ViewHolder2(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecAdapterSearchList.ViewHolder2 holder, int position) {
        holder.username.setText(list.get(position));
        Glide.with(context)
                .load(IpAddress.ip + "/api/user/pfpbyname/"+ holder.username.getText().toString())
                .diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.pfp);

        holder.listitem.setOnClickListener(v->{

            SearchListClickedItem.username = list.get(position);
            ((MainActivity)context).setUseProfile();

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {

        private TextView username;
        private ImageView pfp;
        private LinearLayout listitem;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.text);
            pfp = itemView.findViewById(R.id.pfp);
            listitem = itemView.findViewById(R.id.listitem);
            itemView.findViewById(R.id.listitem).setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200));


        }
    }
}



