package com.example.thriftbooks.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {
    private static final String TAG = "PostsFragment";
    private PostsAdapter adapter;
    private List<Post> allPosts;
    EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView rvBooks;
    private SwipeRefreshLayout swipeContainer;
    MainActivity activity;
    ImageButton btnSendMessage;
    User currentUser = (User) ParseUser.getCurrentUser();

    public HomeFragment() {
        // Required empty public constructor
    }

    public HomeFragment(MainActivity mainActivity) {
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
                allPosts.clear();

                queryPosts(0);
            }
        });
        rvBooks = view.findViewById(R.id.rvBooks);
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);
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
        query.whereNotEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.include(Post.KEY_USER);
        query.setLimit(25);
        query.setSkip(i);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void done(List<Post> posts, ParseException e) {
                Double favGenreScore = 0.0;
                Double priceScore = 0.0;
                Double latestUpload = 0.0;
                Double maxPrice = 0.0;
                Double minimum = 0.0;
                if (posts.size() > 1){
                    minimum = posts.get(0).getBookPrice();
                }else{
                    minimum = 0.0;
                }
                Double minPrice  = minimum;
                Map<Post, Double> map = new HashMap<>();
                for (Post post : posts) {
                    Double currentPrice = post.getBookPrice();
                    if (currentPrice > maxPrice) {
                        maxPrice = post.getBookPrice();

                    } else if (currentPrice < minPrice) {
                        minPrice = currentPrice;
                    }
                }

                for (Post post : posts) {
                    favGenreScore = getScaledValue(scoreFavGenre(post.getBookGenre()),1.0,0.0);
                    latestUpload = getScaledValue(timeScore(post.getCreatedAt()), timeScore(posts.get(0).getCreatedAt()), timeScore(posts.get(posts.size() - 1).getCreatedAt()));
                    priceScore = getScaledValue(post.getBookPrice(),maxPrice,minPrice);
                    final Double finalScore = 0.80 * favGenreScore + 0.1 * priceScore + 0.1 * latestUpload;
                    map.put(post, finalScore);

                }
                map.entrySet().stream().sorted(Map.Entry.comparingByValue());
                List<Post> result = map.keySet().stream().collect(Collectors.toList());
                allPosts.addAll(result);
                swipeContainer.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }
    private Double timeScore(Date createdAt) {
        double time = createdAt.getTime();
        return time;

    }

    private Double scoreFavGenre(String bookGenre) {
        String userFavGenre = currentUser.getFavGenre();
        Double score = 0.0;
        if (userFavGenre == bookGenre) {
            score = 1.0;
        } else if (userFavGenre == bookGenre) {
            score = 0.0;
        }
        return score;
    }

    private Double getScaledValue(Double data, Double maxValue, Double minValue) {
        Double dataPoint;
        if (data > minValue) {
            dataPoint = ((data - minValue) / (maxValue - minValue));
        } else {
            dataPoint = ((minValue - data) / (maxValue - minValue));
        }
        return dataPoint;
    }
}