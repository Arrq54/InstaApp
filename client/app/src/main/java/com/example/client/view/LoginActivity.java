package com.example.client.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.Toast;

import com.example.client.api.UsersAPI;
import com.example.client.databinding.ActivityLoginBinding;
import com.example.client.model.AuthResponse;
import com.example.client.model.IpAddress;
import com.example.client.model.LoginResponse;
import com.example.client.model.UserData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding loginBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IpAddress.ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsersAPI usersAPI = retrofit.create(UsersAPI.class);


        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        String username = sharedPreferences.getString("username", null);

        if (token != null && username !=null) {
            UserData.setUsername(username);
            UserData.setToken(token);

            Call<AuthResponse> call = usersAPI.postAuthData("Bearer " + token);
            call.enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if(response.body().isSuccess()){
                        changeActivity();
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Log.d("logdev", t.toString());
                }
            });






        }


        SpannableString content = new SpannableString("I don't have an account yet");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        loginBinding.register.setText(content);



        loginBinding.register.setOnClickListener(textview->{


            Intent int2 = new Intent(this, RegisterActivity.class);
            startActivity(int2);
        });



        loginBinding.loginButton.setOnClickListener(btn->{
            Call<LoginResponse> call = usersAPI.login(
                    loginBinding.usernameText.getText().toString(),
                    loginBinding.passwordText.getText().toString()
            );
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if(response.body().isSuccess()){
                        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", response.body().getUsername());
                        editor.putString("token", response.body().getToken());
                        editor.apply();
                        UserData.setUsername(response.body().getUsername());
                        UserData.setToken(response.body().getToken());

                        changeActivity();
                    }else{
                        Toast.makeText(LoginActivity.this, "WRONG DATA", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d("logdev", t.toString());
                }
            });

        });

        setContentView(loginBinding.getRoot());
    }
    private void changeActivity(){
        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(myIntent);
        finish();
    }
}