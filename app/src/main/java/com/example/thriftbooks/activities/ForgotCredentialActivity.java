package com.example.thriftbooks.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.thriftbooks.R;

public class ForgotCredentialActivity extends AppCompatActivity {
    private Button buttonPwdReset;
    private EditText editTextPwdResetEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_credential);
        getSupportActionBar().setTitle("Forgot Password");
        editTextPwdResetEmail = findViewById(R.id.etPasswordResetEmail);
        buttonPwdReset = findViewById(R.id.btnSendLink);
        buttonPwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextPwdResetEmail.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ForgotCredentialActivity.this, "Please Enter Registered Email!", Toast.LENGTH_SHORT).show();
                    editTextPwdResetEmail.setError("Email is Required!");
                    editTextPwdResetEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(ForgotCredentialActivity.this, "Please Enter Valid Email!", Toast.LENGTH_SHORT).show();
                    editTextPwdResetEmail.setError("Valid email is Required!");
                    editTextPwdResetEmail.requestFocus();
                } else {
                    resetPassword();
                }

            }
        });
    }

    private void resetPassword() {

    }

}