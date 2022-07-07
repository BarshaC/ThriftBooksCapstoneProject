package com.example.thriftbooks.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.thriftbooks.EndlessRecyclerViewScrollListener;
import com.example.thriftbooks.PostsAdapter;
import com.example.thriftbooks.R;
import com.example.thriftbooks.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment{
    private static final String TAG = "PostsFragment";
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView rvBooks;
    private SwipeRefreshLayout swipeContainer;

    public HomeFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_message, menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSettings:  {
                // navigate to settings screen
                return true;
            }
            case R.id.actionSearch:  {
                // navigate to settings screen
                return false;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeContainer = view.findViewById(R.id.swipeContainer);
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
        query.include(Post.KEY_USER);
        query.setLimit(25);
        query.setSkip(i);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts on HomePage",e);
                } for (Post post: posts) {
                    //Log.i(TAG, "Posts : " + post.getDescription() + ", " + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                swipeContainer.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }
}