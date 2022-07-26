package com.example.thriftbooks.fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.thriftbooks.EndlessRecyclerViewScrollListener;
import com.example.thriftbooks.R;
import com.example.thriftbooks.SearchAdapter;
import com.example.thriftbooks.models.Book;
import com.example.thriftbooks.models.Post;
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

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    private ArrayList<Book> searchedBookArrayList;
    private ProgressBar progressBarSearch;
    private RecyclerView searchRV;
    private ImageButton ibSearch;
    private EditText etSearchBook;
    private EndlessRecyclerViewScrollListener scrollListener;
    protected SearchAdapter searchAdapter;
    protected List<Post> allPostsSearch;
    private RecyclerView rvBooksSearch;
    RadioGroup rGroup;
    private SwipeRefreshLayout swipeRefreshLayoutSearch;


    public SearchFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearchBook = view.findViewById(R.id.etSearchBooks);
        ibSearch = view.findViewById(R.id.ibSearch);
        rvBooksSearch = view.findViewById(R.id.rvSearchBooks);
        progressBarSearch = view.findViewById(R.id.pbSearch);
        swipeRefreshLayoutSearch = view.findViewById(R.id.swipeContainerSearch);
        rGroup = (RadioGroup) view.findViewById(R.id.radioGroupSearch);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        swipeRefreshLayoutSearch.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPostsSearch(0);
            }
        });
        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarSearch.setVisibility(View.VISIBLE);
                if (!etSearchBook.getText().toString().isEmpty()) {
                    queryPostsSearch(0);
                    progressBarSearch.setVisibility(View.GONE);

                }
                if (etSearchBook.getText().toString().isEmpty()) {
                    if (rGroup.callOnClick()) {
                        queryPostsSearch(0);
                        progressBarSearch.setVisibility(View.GONE);
                    }
                } else if (etSearchBook.getText().toString().isEmpty()) {
                    etSearchBook.setError("Please enter name of book to search!");
                }
            }
        });
        allPostsSearch = new ArrayList<>();
        searchAdapter = new SearchAdapter(getContext(), allPostsSearch);
        rvBooksSearch.setAdapter(searchAdapter);
        rvBooksSearch.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryPostsSearch(allPostsSearch.size());
            }
        };
        rvBooksSearch.addOnScrollListener(scrollListener);
    }

    private void queryPostsSearch(int i) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereNotEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.whereContains(Post.KEY_POST_BOOK_TITLE, etSearchBook.getText().toString());
        query.setLimit(25);
        query.setSkip(i);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        RadioButton checkedRadioButton = (RadioButton) rGroup.findViewById(rGroup.getCheckedRadioButtonId());
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbNonFictionOption) {
                    query.whereEqualTo(Post.KEY_POST_BOOK_GENRE, "NonFiction");
                } else if (checkedId == R.id.rbFictionOption) {
                    query.whereEqualTo(Post.KEY_POST_BOOK_GENRE, "Fiction");
                } else if (checkedId == R.id.rbPlaysOption) {
                    query.whereEqualTo(Post.KEY_POST_BOOK_GENRE, "Plays");
                } else if (checkedId == R.id.rbFolktaleOption) {
                    query.whereEqualTo(Post.KEY_POST_BOOK_GENRE, "Folktale");
                } else if (checkedId == R.id.rbTextBookOption) {
                    query.whereEqualTo(Post.KEY_POST_BOOK_GENRE, "TextBook");
                }
            }
        });
        query.findInBackground(new FindCallback<Post>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void done(List<Post> posts, ParseException e) {
                Double conditionScore = 0.0;
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
                    conditionScore = getScaledValue(scoreCondition(post.getBookCondition()), 4.0, 1.0);
                    latestUpload = getScaledValue(timeScore(post.getCreatedAt()), timeScore(posts.get(0).getCreatedAt()), timeScore(posts.get(posts.size() - 1).getCreatedAt()));
                    priceScore = getScaledValue(post.getBookPrice(), maxPrice, minPrice);
                    final Double finalScore = 0.75 * conditionScore + 0.15 * priceScore + 0.15 * latestUpload;
                    Log.i("Score: " + finalScore + "; ", post.getBookTitle());
                    map.put(post, finalScore);
                }
                map.entrySet().stream().sorted(Map.Entry.comparingByValue());
                List<Post> result = map.keySet().stream().collect(Collectors.toList());
                allPostsSearch.addAll(result);
                swipeRefreshLayoutSearch.setRefreshing(false);
                searchAdapter.notifyDataSetChanged();
            }
        });
    }

    private Double timeScore(Date createdAt) {
        double time = createdAt.getTime();
        return time;

    }

    private Double scoreCondition(String bookCondition) {
        Double score = 0.0;
        if (bookCondition == "Like New") {
            score = 4.0;
        } else if (bookCondition == "Good") {
            score = 3.0;
        } else if (bookCondition == "Bad Cover") {
            score = 2.0;
        } else if (bookCondition == "Torn") {
            score = 1.0;
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