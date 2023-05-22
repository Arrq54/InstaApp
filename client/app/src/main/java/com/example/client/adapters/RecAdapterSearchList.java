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

public class RecAdapterSearchList extends RecyclerView.Adapter<RecAdapterSearchList.ViewHolder> {
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
    public RecAdapterSearchList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result, parent, false);
        return new RecAdapterSearchList.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecAdapterSearchList.ViewHolder holder, int position) {
        Log.d("logdev", String.valueOf(position));
        holder.username.setText(list.get(position));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.text);
        }
    }
}



