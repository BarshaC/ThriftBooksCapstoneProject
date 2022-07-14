package com.example.thriftbooks.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.thriftbooks.R;
import com.example.thriftbooks.ThreadMessageAdapter;
import com.example.thriftbooks.models.MessageThread;
import com.example.thriftbooks.models.Post;
import com.example.thriftbooks.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MessageThreadActivity extends AppCompatActivity {
    private static final String TAG = "MessageThreadActivity";
    protected ThreadMessageAdapter adapter;
    protected RecyclerView rvThreads;
    protected List<MessageThread> allThreads;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_thread);
        rvThreads = findViewById(R.id.rvMessageThreads);
        allThreads = new ArrayList<>();
        adapter = new ThreadMessageAdapter(this,allThreads);
        rvThreads.setAdapter(adapter);
        rvThreads.setLayoutManager(new LinearLayoutManager(this));
        swipeContainer = findViewById(R.id.swipeContainerThreads);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryThreads();
                //Will have to put the recent message on top which has not been listed yet
            }
        });
        queryThreads();
    }
    private void queryThreads() {
        ParseQuery<MessageThread> query = ParseQuery.getQuery(MessageThread.class);
        query.include(MessageThread.KEY_THREAD_POST_ID);
        query.setLimit(25);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.whereNotEqualTo(MessageThread.KEY_THREAD_BUYER_ID, (User) ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<MessageThread>() {
            @Override
            public void done(List<MessageThread> threads, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts on HomePage", e);
                } for (MessageThread thread: threads) {
                    Log.i(TAG, "Posts : " + thread.getPostId() + ", " );
                }
                allThreads.clear();
                allThreads.addAll(threads);
                swipeContainer.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }
}