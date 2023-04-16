package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.client.databinding.ActivityMainBinding;

import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private Home homeFragment;
    private AddPhoto addPhotoFragment;
    private Search searchFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        AtomicReference<MenuItem> previouslySelected = new AtomicReference<>();


        homeFragment = new Home();
        addPhotoFragment = new AddPhoto();
        searchFragment = new Search();


        replaceFragment(homeFragment, "home");


        //DISABLE DRAWER SCROLL
        mainBinding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);



        //HANDLE BOTTOM MENU
        mainBinding.bottomNav.setSelectedItemId(R.id.home);
        previouslySelected.set(mainBinding.bottomNav.getMenu().findItem(R.id.home));
        mainBinding.bottomNav.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home:
                    if(previouslySelected.get()!=null &&  previouslySelected.get().getItemId() == R.id.add){
                        previouslySelected.get().setIcon(R.drawable.addphoto);
                    }
                    if(!homeFragment.isVisible()){
                        replaceFragment(homeFragment, "home");
                    }
                    item.setIcon(R.drawable.homechosen);
                    break;
                case R.id.search:
                    if(previouslySelected.get()!=null && previouslySelected.get().getItemId() == R.id.home){
                        previouslySelected.get().setIcon(R.drawable.home);
                    }else if(previouslySelected.get()!=null && previouslySelected.get().getItemId() == R.id.add){
                        previouslySelected.get().setIcon(R.drawable.addphoto);
                    }
                    if(!searchFragment.isVisible()){
                        replaceFragment(searchFragment, "search");
                    }
                    break;
                case R.id.add:
                    if(previouslySelected.get()!=null && previouslySelected.get().getItemId() == R.id.home){
                        previouslySelected.get().setIcon(R.drawable.home);
                    }
                    item.setIcon(R.drawable.addphotochosen);
                    if(!addPhotoFragment.isVisible()){
                        replaceFragment(addPhotoFragment, "addPhoto");
                    }

                    break;
            }
            previouslySelected.set(item);
            return true;
        });


        mainBinding.drawerNav.setNavigationItemSelectedListener(item->{
            mainBinding.drawer.closeDrawer(GravityCompat.START);

            int id = item.getItemId();

            switch(id){
                case R.id.thememode:
                    if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }else{
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }

                    break;
            }

            return true;
        });






        //OPEN DRAWER ON SETTINGS CLICK
        mainBinding.settings.setOnClickListener(view->{
            mainBinding.drawer.openDrawer(GravityCompat.START);
        });
    }

    public void replaceFragment(Fragment fragment, String tag) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root, fragment, tag)
                .commit();
    }
}