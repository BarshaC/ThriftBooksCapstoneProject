package com.example.thriftbooks.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.thriftbooks.R;
import com.example.thriftbooks.fragments.ComposeFragment;
import com.example.thriftbooks.fragments.HomeFragment;
import com.example.thriftbooks.fragments.LogoutFragment;
import com.example.thriftbooks.fragments.ProfileFragment;
import com.example.thriftbooks.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;
    private FrameLayout flContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.actionHome:
                        fragment = new HomeFragment();
                        break;
                    case R.id.actionCompose:
                        fragment = new ComposeFragment();
                        break;
                    case R.id.actionProfile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.actionSearch:
                        fragment = new SearchFragment();
                        break;
                    case R.id.actionLogout:
                        fragment = new LogoutFragment();
                        break;
//                    case R.id.actionMessage:
//                        fragment = new MessageFragment(); //Will later use it for message Fragment
                    default:
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }

        });
        bottomNavigationView.setSelectedItemId(R.id.actionHome);


    }
}