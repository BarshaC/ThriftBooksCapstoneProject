package com.example.thriftbooks.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thriftbooks.EndlessRecyclerViewScrollListener;
import com.example.thriftbooks.ProfileAdapter;
import com.example.thriftbooks.R;
import com.example.thriftbooks.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private ImageView ivProfileBookImage;
    private TextView tvGridUsername;
    private ProfileAdapter adaptProfile;
    private RecyclerView rvBooksProfile;
    protected List<Post> profilePosts;
    private SwipeRefreshLayout swipeContainerProfile;
    private GridLayoutManager gridLayoutManager;
    EndlessRecyclerViewScrollListener scrollListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profilePosts = new ArrayList<>();
        swipeContainerProfile = view.findViewById(R.id.swipeContainerProfile);
        swipeContainerProfile.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPosts(0);
            }
        });
        gridLayoutManager = new GridLayoutManager(getContext(),3);
        tvGridUsername = view.findViewById(R.id.tvGridProfileUsername);
        ivProfileBookImage = view.findViewById(R.id.ivGridBookImage);
        rvBooksProfile = view.findViewById(R.id.rvProfilePosts);
        adaptProfile = new ProfileAdapter(getContext(), profilePosts);
        rvBooksProfile.setAdapter(adaptProfile);
        rvBooksProfile.setLayoutManager(gridLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryPosts(profilePosts.size());
            }
        };
        rvBooksProfile.addOnScrollListener(scrollListener);
        queryPosts(0);
    }
    public void queryPosts(int i) {
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
                    Log.i(TAG, "Posts : " + post.getDescription() + ", " + post.getUser().getUsername());
                }
                profilePosts.addAll(posts);
                swipeContainerProfile.setRefreshing(false);
                adaptProfile.notifyDataSetChanged();
            }
        });
    }
}