package com.example.thriftbooks.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.thriftbooks.CommentAdapter;
import com.example.thriftbooks.R;
import com.example.thriftbooks.models.Comment;
import com.example.thriftbooks.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {
    private static final String TAG = "BookDetailsActivity";
    private TextView username;
    private ImageView displayPicture;
    private ImageView ivImage;
    private TextView bookTitle, bookAuthor, bookType, bookCondition, timestamp;
    private List<Comment> allComments;
    private RecyclerView recyclerViewComment;
    private SwipeRefreshLayout swipeRefreshLayoutComments;
    protected CommentAdapter commentAdapter;
    private Post post;
    private TextView tvShowNoComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        post = Parcels.unwrap(getIntent().getParcelableExtra("PostDetails"));
        ivImage = findViewById(R.id.ivPostDetailBookPhoto);
        displayPicture = findViewById(R.id.profilePicture);
        bookTitle = findViewById(R.id.tvDetailBookTitle);
        bookAuthor = findViewById(R.id.tvDetailBookAuthor);
        bookCondition = findViewById(R.id.tvDetailBookCondition);
        bookType = findViewById(R.id.tvDetailBookType);
        timestamp = findViewById(R.id.timeAgo);
        bookTitle.setText(post.getBookTitle());
        bookAuthor.setText(post.getBookAuthor());
        bookType.setText(post.getBookType());
        tvShowNoComments = findViewById(R.id.tvShowNoComments);
        bookCondition.setText(post.getBookCondition());
        Date timeStamp = post.getCreatedAt();
        String time = Post.calculateTimeAgo(timeStamp);
        timestamp.setText(time + " ago");
        ParseFile profilePicture = post.getUser().getProfileImage();
        if (profilePicture != null) {
            Glide.with(this).load(profilePicture.getUrl()).circleCrop().into(displayPicture);
        }
        username = findViewById(R.id.username);
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivImage);
        }
        recyclerViewComment = findViewById(R.id.rvComments);
        allComments = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, allComments);
        recyclerViewComment.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewComment.setAdapter(commentAdapter);
        swipeRefreshLayoutComments = findViewById(R.id.swipeContainerComments);
        swipeRefreshLayoutComments.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryComments();
            }
        });
        queryComments();
    }

    private void queryComments() {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.setLimit(25);
        query.whereContains(Comment.KEY_POST_ID_COMMENT_REFERENCE,post.getObjectId());
        query.addDescendingOrder(Comment.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts on HomePage", e);
                }
                allComments.clear();
                allComments.addAll(comments);
                Log.i("myComments", allComments.toString());
                if (allComments.size() == 0) {
                    tvShowNoComments.setVisibility(View.VISIBLE);
                }
                swipeRefreshLayoutComments.setRefreshing(false);
                commentAdapter.notifyDataSetChanged();
                }
        });
    }


}