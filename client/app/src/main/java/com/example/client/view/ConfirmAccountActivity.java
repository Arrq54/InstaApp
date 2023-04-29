package com.example.client.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.client.R;
import com.example.client.databinding.ActivityConfirmAccountBinding;

public class ConfirmAccountActivity extends AppCompatActivity {
    private ActivityConfirmAccountBinding confirmAccountBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        confirmAccountBinding = ActivityConfirmAccountBinding.inflate(getLayoutInflater());
        setContentView(confirmAccountBinding.getRoot());

        Intent intent = getIntent();
        String value = intent.getStringExtra("token");
        confirmAccountBinding.tokenConfirm.setText(value);

        confirmAccountBinding.tokenConfirm.setOnClickListener(v->{
            finish();
        });
    }
}