package com.example.thriftbooks.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thriftbooks.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.facebook.ParseFacebookUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;
    private Button btnForgotPassword;
    private Button btnLoginWithFB;

    //    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_forgot_password, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionGoBack: {
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void Launch(Context context) {
        Intent forgot = new Intent(context, LoginActivity.class);
        context.startActivity(forgot);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ParseUser.getCurrentUser() != null) {
            if (ParseUser.getCurrentUser().isAuthenticated()) {
                goMainActivity();
            } else {
                ParseUser.logOut();
            }
        }
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.etEditUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnLoginWithFB = findViewById(R.id.btnLoginWithFb);

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "You can reset your password now!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, ForgotCredentialActivity.class);
                startActivity(i);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick login Button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });
        btnLoginWithFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                dialog.setTitle("Please, wait a moment.");
                dialog.setMessage("Logging in...");
                dialog.show();
                Collection<String> permissions = Arrays.asList("public_profile", "email");
                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, permissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        dialog.dismiss();
                        if (e != null) {
                            ParseUser.logOut();
                        }  if (user == null) {
                            ParseUser.logOut();
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                            getUserDetailsFromFacebook();
                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            getUserDetailFromParse();
                        }
                    }
                });
            }
        });
    }

    private void getUserDetailFromParse() {
        ParseUser user = ParseUser.getCurrentUser();
        String welcome = "Logged in as: " + user.getUsername();
        showAlert("Welcome Back",welcome);
    }


    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user : " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this, "Issue with Login", Toast.LENGTH_SHORT).show();
                    return;
                }
                goMainActivity();
                finish();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        ParsePush.subscribeInBackground(ParseUser.getCurrentUser().getObjectId());
        startActivity(i);
    }

    private void getUserDetailsFromFacebook() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
               ParseUser user = ParseUser.getCurrentUser();
               try {
                   user.setUsername(jsonObject.getString("name"));
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               try {
                   user.setEmail(jsonObject.getString("email"));
               } catch (Exception e) {
                   e.printStackTrace();
               }
               user.saveInBackground(new SaveCallback() {
                   @Override
                   public void done(ParseException e) {
                       showAlert("First time Logging in ", "Welcome to ThriftBooks");
                   }
               });
            }
        });

    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent info) {
        super.onActivityResult(requestCode, resultCode, info);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, info);
    }

}