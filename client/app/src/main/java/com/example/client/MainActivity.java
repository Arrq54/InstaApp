package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.client.databinding.ActivityMainBinding;

import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        AtomicReference<MenuItem> previouslySelected = new AtomicReference<>();



        mainBinding.bottomNav.setSelectedItemId(R.id.home);
        previouslySelected.set(mainBinding.bottomNav.getMenu().findItem(R.id.home));
        mainBinding.bottomNav.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home:
                    if(previouslySelected.get()!=null &&  previouslySelected.get().getItemId() == R.id.add){
                        previouslySelected.get().setIcon(R.drawable.addphoto);
                    }
                    item.setIcon(R.drawable.homechosen);
                    break;
                case R.id.search:
                    if(previouslySelected.get()!=null && previouslySelected.get().getItemId() == R.id.home){
                        previouslySelected.get().setIcon(R.drawable.home);
                    }else if(previouslySelected.get()!=null && previouslySelected.get().getItemId() == R.id.add){
                        previouslySelected.get().setIcon(R.drawable.addphoto);
                    }
                    break;
                case R.id.add:
                    if(previouslySelected.get()!=null && previouslySelected.get().getItemId() == R.id.home){
                        previouslySelected.get().setIcon(R.drawable.home);
                    }
                    item.setIcon(R.drawable.addphotochosen);
                    break;
            }
            previouslySelected.set(item);
            return true;
        });
    }
}