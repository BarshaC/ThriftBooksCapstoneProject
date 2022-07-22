package com.example.thriftbooks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thriftbooks.R;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgotCredentialActivity extends AppCompatActivity {
    private static final String TAG = "ForgotCredentialActivity";
    private Button buttonPwdReset;
    private EditText editTextPwdResetEmail;
    private ProgressBar progressBar;
    private ImageButton backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_credential);
        editTextPwdResetEmail = findViewById(R.id.etPasswordResetEmail);
        buttonPwdReset = findViewById(R.id.btnSendLink);
        progressBar = findViewById(R.id.progressBarForgotPage);
        backToLogin = findViewById(R.id.ibBack);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgotCredentialActivity.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            }
        });
        buttonPwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String emailAddress = editTextPwdResetEmail.getText().toString();
        ParseUser.requestPasswordResetInBackground(emailAddress,
                new RequestPasswordResetCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            Toast.makeText(ForgotCredentialActivity.this, "Go check your email!", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            Toast.makeText(ForgotCredentialActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}