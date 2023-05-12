package com.example.client.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.client.R;
import com.example.client.databinding.ActivityMainBinding;
import com.example.client.model.IpAddress;
import com.example.client.model.UserData;

import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private Home homeFragment;
    private AddPhoto addPhotoFragment;
    private Search searchFragment;
    private UserProfile userProfile;
    private AddPhotoUpload addPhotoUpload;
    private TagsForPhoto tagsForPhoto;
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
        userProfile = new UserProfile();
        addPhotoUpload = new AddPhotoUpload();
        tagsForPhoto = new TagsForPhoto();

        replaceFragment(homeFragment, "home");


        //DISABLE DRAWER SCROLL
        mainBinding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);



        //HANDLE BOTTOM MENU
        mainBinding.bottomNav.setSelectedItemId(R.id.home);
        previouslySelected.set(mainBinding.bottomNav.getMenu().findItem(R.id.home));
        mainBinding.bottomNav.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home:
                    mainBinding.profile.setImageResource(R.drawable.user);
                    if(previouslySelected.get()!=null &&  previouslySelected.get().getItemId() == R.id.add){
                        previouslySelected.get().setIcon(R.drawable.addphoto);
                    }
                    if(!homeFragment.isVisible()){
                        replaceFragment(homeFragment, "home");
                    }
                    item.setIcon(R.drawable.homechosen);
                    break;
                case R.id.search:
                    mainBinding.profile.setImageResource(R.drawable.user);
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
                    mainBinding.profile.setImageResource(R.drawable.user);
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
                case R.id.ip:
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

                    break;
                case R.id.logoutMenu:
                    SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("username");
                    editor.remove("token");
                    editor.apply();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    break;
            }

            return true;
        });






        //OPEN DRAWER ON SETTINGS CLICK
        mainBinding.settings.setOnClickListener(view->{
            mainBinding.drawer.openDrawer(GravityCompat.START);
        });


        mainBinding.profile.setOnClickListener(view->{
            mainBinding.profile.setImageResource(R.drawable.userchosen);
            if(previouslySelected.get()!=null && previouslySelected.get().getItemId() == R.id.home){
                previouslySelected.get().setIcon(R.drawable.home);
            }else if(previouslySelected.get()!=null && previouslySelected.get().getItemId() == R.id.add){
                previouslySelected.get().setIcon(R.drawable.addphoto);
            }
            Bundle bundle = new Bundle();
            bundle.putString("username", UserData.getUsername());
            getSupportFragmentManager().setFragmentResult("username", bundle);
            replaceFragment(userProfile, "userProfile");

        });


    }

    public void replaceFragment(Fragment fragment, String tag) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root, fragment, tag)
                .commit();
    }

    public void setAddPhotoUpload(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root, addPhotoUpload, "addphotoupload")
                .commit();
    }
    public void setHomeFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root, homeFragment, "home")
                .commit();
    }
    public void setTagsForPhoto(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root, tagsForPhoto, "tagsforphoto")
                .commit();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("logdev", String.valueOf(requestCode));
//    }
}