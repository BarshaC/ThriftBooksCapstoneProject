package com.example.thriftbooks.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Handler;

import com.example.thriftbooks.R;

public class WelcomeActivity extends AppCompatActivity {

    boolean handler = new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }, 2000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
}