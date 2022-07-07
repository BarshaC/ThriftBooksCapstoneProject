package com.example.thriftbooks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thriftbooks.R;

public class BookDetailsActivity extends AppCompatActivity {
    private static final String TAG = "BookDetailsActivity";
    private EditText etUsername;
    private ImageButton backToMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        backToMain = (ImageButton) findViewById(R.id.ibBack);
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BookDetailsActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}