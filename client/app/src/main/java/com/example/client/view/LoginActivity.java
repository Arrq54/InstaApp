package com.example.client.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.EditText;
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
        String id = sharedPreferences.getString("id", null);
        if (token != null && username !=null) {
            UserData.setUsername(username);
            UserData.setToken(token);
            UserData.setId(id);

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

        loginBinding.changeIp.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Change ip");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(IpAddress.ip);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String userInput = input.getText().toString();
                    IpAddress.ip = userInput;
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
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
                        editor.putString("id", response.body().getId());
                        editor.apply();

                        UserData.setUsername(response.body().getUsername());
                        UserData.setToken(response.body().getToken());
                        UserData.setId(response.body().getId());

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