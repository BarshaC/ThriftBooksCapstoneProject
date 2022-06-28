package com.example.thriftbooks.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.thriftbooks.R;
import com.example.thriftbooks.models.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnRegister, btnLogin;
    private Spinner spinnerFavGenre;
    private EditText etEmail;
    private EditText firstName;
    private EditText secondName;
    private EditText etDateOfBirth;
    private String [] genreOption = {"Select Your Fav Genre", "Fiction", "NonFiction", "TextBook", "FolkTale", "Plays"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);
        etEmail = findViewById(R.id.etEmail);
        firstName = findViewById(R.id.etFirstName);
        secondName = findViewById(R.id.etSecondName);
        etDateOfBirth = findViewById(R.id.etDateOfBirth);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLgn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        spinnerFavGenre = findViewById(R.id.spinnerGenre);
        ArrayAdapter<String> adapterSignUp = new ArrayAdapter<String>(SignUpActivity.this, android.R.layout.simple_spinner_item, genreOption);
        adapterSignUp.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerFavGenre.setAdapter(adapterSignUp);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setUsername(etUsername.getText().toString());
                user.setPassword(etPassword.getText().toString());
                //user.setEmail(etEmail.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null ) {
                            Log.e(TAG,e.toString());
                            return;
                        }
                        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
            }
        });
    }
}