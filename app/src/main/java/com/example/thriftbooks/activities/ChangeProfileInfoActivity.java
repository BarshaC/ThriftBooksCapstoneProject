package com.example.thriftbooks.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.thriftbooks.R;
import com.example.thriftbooks.models.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;

public class ChangeProfileInfoActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 12;
    private static final int SELECT_PICTURE = 10;
    private EditText etEditUsername;
    private EditText etEditFirstName;
    private EditText etEditSecondName;
    private TextView tvChangeProfilePic;
    private Button tvDone;
    public String profilePhotoFileName = "profilePhoto.jpg";
    private Button tvCancel;
    private ImageView ivImageProfilePic;
    private File profileImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_info);
        etEditUsername = findViewById(R.id.etEditUsername);
        etEditFirstName = findViewById(R.id.etEditFirstName);
        etEditSecondName = findViewById(R.id.etEditLastName);
        tvChangeProfilePic = findViewById(R.id.tvChangeProfilePic);
        tvCancel = findViewById(R.id.btnCancel);
        tvChangeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvChangeProfilePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ChangeProfileInfoActivity.this);
                        bottomSheetDialog.setContentView(R.layout.bottom_sheet_profile_change);
                        bottomSheetDialog.setCanceledOnTouchOutside(false);
                        LinearLayout LLUploadPhoto = bottomSheetDialog.findViewById(R.id.LLUploadPhoto);
                        LinearLayout LLCancelBottomSheet = bottomSheetDialog.findViewById(R.id.LLCancelBottomSheet);
                        LLCancelBottomSheet.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                selectPicture();
                                bottomSheetDialog.dismiss();
                            }
                        });
                        LLUploadPhoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 4);
                            }
                        });
                        LinearLayout LLTakePhoto = bottomSheetDialog.findViewById(R.id.LLtakephoto);
                        LLTakePhoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                {
                                    launchCamera();
                                    bottomSheetDialog.dismiss();
                                }
                            }
                        });
                        bottomSheetDialog.show();
                    }
                });
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeProfileInfoActivity.super.onBackPressed();
            }
        });
        tvDone = findViewById(R.id.btnDone);
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etEditUsername.getText().toString();
                String firstName = etEditFirstName.getText().toString();
                String lastName = etEditSecondName.getText().toString();
                editUserData(username, firstName, lastName, profileImageFile);

            }
        });
        ivImageProfilePic = findViewById(R.id.ivClickedProfilePic);
        ParseFile image = ParseUser.getCurrentUser().getParseFile("profilePicture");
        if (image != null ) {
            Glide.with(this).load(image.getUrl()).circleCrop().into(ivImageProfilePic);
        } else {
            Glide.with(this).load(getPhotoFileUri(ParseUser.getCurrentUser().getObjectId())).circleCrop().into(ivImageProfilePic);
        }
        if (profileImageFile == null || ivImageProfilePic.getDrawable() == null) {
            Toast.makeText(getApplicationContext(), "You must add image to change profile picture", Toast.LENGTH_SHORT).show();
            return;
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(profileImageFile.getAbsolutePath());
                ivImageProfilePic.setImageBitmap(takenImage);
            } else {
                Toast.makeText(getApplicationContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {
                ivImageProfilePic.setImageURI(data.getData());
                }
            }
    }

    private void selectPicture() {
        Intent intent = new Intent();
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, SELECT_PICTURE );
        //startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_PICTURE);

    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        profileImageFile = getPhotoFileUri(profilePhotoFileName);
        Log.d(TAG, "failed to create directory");
        Uri fileProvider = FileProvider.getUriForFile(getApplicationContext(), "com.codepath.fileprovider", profileImageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    public void editUserData(String username, String firstName, String lastName, File photoFile ) {
        User user = (User) ParseUser.getCurrentUser();
        user.setProfileImage(new ParseFile(photoFile));
        user.setUserLastName(lastName);
        user.setUserFirstName(firstName);
        user.setUsername(username);
        user.saveInBackground();
    }

}