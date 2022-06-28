package com.example.thriftbooks.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.example.thriftbooks.BooksAdapter;
import com.example.thriftbooks.R;
import com.example.thriftbooks.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    private final String TAG = "FeedActivity";
    private SwipeRefreshLayout swipeContainer;
    protected BooksAdapter adapter;
    protected RecyclerView rvBooks;
    protected List<Post> allPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.thriftbooks.R.layout.activity_feed);
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPosts();
            }
        });
        rvBooks = findViewById(R.id.rvBooks1);
        allPosts = new ArrayList<>();
        adapter = new BooksAdapter(this, allPosts);
        rvBooks.setAdapter(adapter);
        rvBooks.setLayoutManager(new LinearLayoutManager(this));

    }

    private void queryPosts() {
        ParseQuery<Post> query  = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(25);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting all the posts",e);
                    return;
                } for (Post post : posts) {
                    Log.i(TAG, "Posts :" + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                swipeContainer.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }
}