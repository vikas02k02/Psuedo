package com.vikas.pseudo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.vikas.pseudo.screens.ProfileFragment;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNV= findViewById(R.id.BottomNV);
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();
        ft.add(R.id.MainFrame,new HomeFragment()).commit();

        bottomNV.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.BottomNV_post){
                   loadFragment(new CreatePostFragment());
                   return true;
                } else if (item.getItemId()==R.id.BottomNV_profile) {
                    loadFragment(new ProfileFragment());
                    return true;

                }else if (item.getItemId()==R.id.BottomNV_home){
                    loadFragment(new HomeFragment());
                    return true;
                }
                return false;
            }
        });

    }
    public void loadFragment(Fragment fragment){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft =fm.beginTransaction();
        ft.replace(R.id.MainFrame,fragment).commit();
    }
    public void checkLogin(){
        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null){
            //user stay here

        }else {
            Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onStart() {
        checkLogin();
        super.onStart();
    }

}
