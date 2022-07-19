package com.example.thriftbooks.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.thriftbooks.EndlessRecyclerViewScrollListener;
import com.example.thriftbooks.PostsAdapter;
import com.example.thriftbooks.R;
import com.example.thriftbooks.activities.MainActivity;
import com.example.thriftbooks.activities.MessageThreadActivity;
import com.example.thriftbooks.models.Post;
import com.example.thriftbooks.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment{
    private static final String TAG = "PostsFragment";
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView rvBooks;
    private SwipeRefreshLayout swipeContainer;
    MainActivity activity;
    ImageButton btnSendMessage;

    public HomeFragment() {
        // Required empty public constructor
    }
    public HomeFragment(MainActivity mainActivity){
        activity = mainActivity;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.getSupportActionBar().hide();
        swipeContainer = view.findViewById(R.id.swipeContainer);
        btnSendMessage = view.findViewById(R.id.ibMessageBtn);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMessageThread = new Intent(getContext(), MessageThreadActivity.class);
                startActivity(intentMessageThread);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               queryPosts(0);
            }
        });
        rvBooks = view.findViewById(R.id.rvBooks);
        allPosts = new ArrayList<Post>();
        adapter = new PostsAdapter(getContext(),allPosts);
        rvBooks.setAdapter(adapter);
        rvBooks.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryPosts(allPosts.size());
            }
        };
        rvBooks.addOnScrollListener(scrollListener);
        queryPosts(0);

    }

    private void queryPosts(int i) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereNotEqualTo(Post.KEY_USER, (User) ParseUser.getCurrentUser());
        query.include(Post.KEY_USER);
        query.setLimit(25);
        query.setSkip(i);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts on HomePage",e);
                    Toast.makeText(getContext(),"Issue with getting posts!",Toast.LENGTH_LONG).show();
                    return;
                }
                for (Post post: posts) {
                    Log.i(TAG, "Posts : " + post.getDescription() + ", " + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                swipeContainer.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }

}