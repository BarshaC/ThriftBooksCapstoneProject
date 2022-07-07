package com.example.thriftbooks.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.thriftbooks.R;
import com.example.thriftbooks.models.Book;
import com.example.thriftbooks.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class ComposeFragment extends Fragment {
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1002;
    private static final String TAG = "ComposeFragment";
    public String photoFileName = "photo.jpg";
    private Button btnCaptureImage, btnSubmit;
    private ImageView ivImageBook;
    private File photoFile;
    private EditText etBookTitle, etBookAuthor, etDescription, etBookType;
    private Spinner spinnerCondition, spinnerType;
    public String [] bookCondition = {"Condition of Book","Torn", "Bad Cover","Good", "Like New"};
    public String [] bookType = {"For Sale/Borrow/Rent","Sale", "Borrow","Rent"};
    private Book book;

    public ComposeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_compose, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etDescription = view.findViewById(R.id.etDescription);
        ivImageBook = view.findViewById(R.id.ivClickedBook);
        btnSubmit = view.findViewById(R.id.btnAddPhoto);
        etBookTitle = view.findViewById(R.id.etBookTitle);
        etBookAuthor = view.findViewById(R.id.etBookAuthor);
        etBookType = view.findViewById(R.id.tvPostBookType);
        //Spinner for Conditions of Books
        spinnerCondition = view.findViewById(R.id.spinnerBookCondition);
        ArrayAdapter <String> adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item,bookCondition);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerCondition.setAdapter(adapter1);
        //Spinner for types of Book for example : For Sale/Borrow/Rent
        spinnerType = view.findViewById(R.id.spinnerBookType);
        ArrayAdapter <String> adapter2 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item,bookType);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerType.setAdapter(adapter2);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(getContext(), "You must add description", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null || ivImageBook.getDrawable() == null) {
                    Toast.makeText(getContext(), "You must add image!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String bookTitle = etBookTitle.getText().toString();
                String bookAuthor = etBookAuthor.getText().toString();
                ParseUser currentUser = ParseUser.getCurrentUser();
                String valueBookCondition = spinnerCondition.getSelectedItem().toString();
                String valueBookType = spinnerType.getSelectedItem().toString();
                savePost(description, currentUser, bookTitle, bookAuthor,valueBookCondition, valueBookType, photoFile);
            }
        });
        btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivImageBook.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);
        Log.d(TAG, "failed to create directory");
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    private void savePost(String description, ParseUser currentUser, String bookTitle, String bookAuthor, String bookCondition, String bookType, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setBookTitle(bookTitle);
        post.setBookAuthor(bookAuthor);
        post.setImage(new ParseFile(photoFile));
        post.setBookType(bookType);
        post.setBookCondition(bookCondition);
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving the post", e);
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Saved the post!");
                ivImageBook.setImageResource(0);
            }
        });
    }
}