package com.example.client.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;

import com.example.client.api.GetPhotosAPI;
import com.example.client.api.UsersAPI;
import com.example.client.databinding.ActivityRegisterBinding;
import com.example.client.model.IpAddress;
import com.example.client.model.Photo;
import com.example.client.model.Token;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding registerBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());

        setContentView(registerBinding.getRoot());


        SpannableString content = new SpannableString("I already have an account");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        registerBinding.login.setText(content);

        registerBinding.login.setOnClickListener(v->{
            finish();
        });

        registerBinding.registerButton.setOnClickListener(v->{
            if(registerBinding.usernameText.getText().length()>0 && registerBinding.lastNameText.getText().length()>0 &&registerBinding.emailText.getText().length()>0 &&registerBinding.passwordText.getText().length()>0){
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(IpAddress.ip)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                UsersAPI usersAPI = retrofit.create(UsersAPI.class);

                Call<Token> call = usersAPI.register(
                        registerBinding.usernameText.getText().toString(),
                        registerBinding.lastNameText.getText().toString(),
                        registerBinding.emailText.getText().toString(),
                        registerBinding.passwordText.getText().toString()
                );

                call.enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        Intent intent = new Intent(RegisterActivity.this, ConfirmAccountActivity.class);
                        intent.putExtra("token", response.body().getToken());
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Log.d("logdev", t.toString());
                    }
                });


            }
        });


    }
}