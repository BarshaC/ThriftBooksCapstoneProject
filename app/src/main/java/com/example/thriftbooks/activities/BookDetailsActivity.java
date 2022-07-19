package com.example.thriftbooks.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.thriftbooks.R;
import com.example.thriftbooks.models.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.Date;

public class BookDetailsActivity extends AppCompatActivity {
    private static final String TAG = "BookDetailsActivity";
    private TextView username;
    private ImageView displayPicture;
    private ImageView ivImage;
    private TextView booktitle, bookAuthor, bookType, bookCondition, timestamp;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        post = Parcels.unwrap(getIntent().getParcelableExtra("PostDetails"));
        ivImage = findViewById(R.id.ivPostDetailBookPhoto);
        displayPicture = findViewById(R.id.profilePicture);
        booktitle = findViewById(R.id.tvDetailBookTitle);
        bookAuthor = findViewById(R.id.tvDetailBookAuthor);
        bookCondition = findViewById(R.id.tvDetailBookCondition);
        bookType = findViewById(R.id.tvDetailBookType);
        timestamp = findViewById(R.id.timeAgo);
        booktitle.setText(post.getBookTitle());
        bookAuthor.setText(post.getBookAuthor());
        bookType.setText(post.getBookType());
        bookCondition.setText(post.getBookCondition());
        Date timeStamp = post.getCreatedAt();
        String time = Post.calculateTimeAgo(timeStamp);
        timestamp.setText(time + " ago");
        ParseFile profilePicture = post.getUser().getProfileImage();
        if(profilePicture != null) {
            Glide.with(this).load(profilePicture.getUrl()).circleCrop().into(displayPicture);
        }
        username = findViewById(R.id.username);
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivImage);
        }
    }

}