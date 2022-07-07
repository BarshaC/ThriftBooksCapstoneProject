package com.example.thriftbooks.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thriftbooks.R;
import com.example.thriftbooks.models.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnRegister, btnLogin;
    private Spinner spinnerFavGenre;
    private EditText etEmail;
    private EditText etFirstName;
    private EditText etSecondName;
    private EditText etDateOfBirth;
    private String [] genreOption = {"Select Your Fav Genre", "Fiction", "NonFiction", "TextBook", "FolkTale", "Plays"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);
        etEmail = findViewById(R.id.etEmail);
        etFirstName = findViewById(R.id.etFirstName);
        etSecondName = findViewById(R.id.etSecondName);
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
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String firstName = etFirstName.getText().toString();
                String lastName = etSecondName.getText().toString();
                String dateOfBirth = etDateOfBirth.getText().toString();
                String email = etEmail.getText().toString();
                String favGenre = spinnerFavGenre.getSelectedItem().toString();
                saveUserData(username, password, firstName, lastName, dateOfBirth, email, favGenre);
            }
        });
    }
    public void saveUserData(String username,String password, String firstName, String lastName, String dateOfBirth, String email, String favGenre) {
        User user = new User();
        user.setUserFirstName(firstName);
        user.setUserLastName(lastName);
        user.setDateOfBirth(dateOfBirth);
        user.setFavGenre(favGenre);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null ) {
                    Log.e(TAG,"All sign up data saved successfully!");
                    return;
                } else {
                    ParseUser.logOut();
                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                showAlert("Successful Sign Up!", "Welcome " + firstName  +"!");
            }
        });
    }
    private void showAlert(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}