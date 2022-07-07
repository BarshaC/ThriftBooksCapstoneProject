package com.example.thriftbooks.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.thriftbooks.BookClient;
import com.example.thriftbooks.BooksAdapter;
import com.example.thriftbooks.BuildConfig;
import com.example.thriftbooks.R;
import com.example.thriftbooks.models.Book;
import com.parse.ParseException;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchActivity";
    private ArrayList<Book> searchedBookArrayList;
    private ProgressBar progressBarSearch;
    private RecyclerView searchRV;
    private ImageButton ibSearch;
    private EditText etSearchBook;
    private RequestQueue requestQuery;
    private BookClient client;
    String api_key = BuildConfig.API_KEY;

    public SearchFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
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
        progressBarSearch = view.findViewById(R.id.pbSearch);
        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarSearch.setVisibility(View.VISIBLE);
                if (etSearchBook.getText().toString().isEmpty()) {
                    etSearchBook.setError("Please enter name of book to search!");
                    return;
                }
                searchedBookArrayList = new ArrayList<>();
                requestQuery = Volley.newRequestQueue(getActivity().getApplicationContext());
                String googleUrl = "https://www.googleapis.com/books/v1/volumes?q=" + "intitle:"+ etSearchBook.getText().toString()+ "&key=" + api_key;
                Log.i(TAG, googleUrl);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                JsonObjectRequest requestBookObject = new JsonObjectRequest(Request.Method.GET, googleUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBarSearch.setVisibility(View.GONE);
                        try {
                            JSONArray itemBooks = response.getJSONArray("items");
                            Log.d(TAG, "Checking for bookitems");
                            for (int i = 0; i < itemBooks.length(); i++) {
                                Book book = new Book();
                                JSONObject itemObject = itemBooks.getJSONObject(i);
                                JSONObject itemBook = itemObject.getJSONObject("volumeInfo");
                                String bookTitle = itemBook.optString("title");
                                String bookSubtitle = itemBook.optString("subtitle");
                                String bookPublisher = itemBook.optString("publisher");
                                String bookDatePublished = itemBook.optString("publishedDate");
                                String aboutVolume = itemBook.optString("description");
                                int pageCount = itemBook.optInt("pageCount");JSONArray authorList = itemBook.getJSONArray("authors");
                                ArrayList<String> authorsList = new ArrayList<>();
                                if (authorList.length() != 0 ) {
                                    for (int x = 0; x <authorList.length(); x++) {
                                        authorsList.add(authorList.optString(i));
                                    }
                                }
                                Log.i(TAG, "onResponse: ");
                                book.setAboutVolume(aboutVolume);
                                book.setPageCount(pageCount);
                                book.setSearchAuthors(authorsList);
                                book.setSearchPublisher(bookPublisher);
                                book.setPublisherDate(bookDatePublished);
                                book.setSearchTitle(bookTitle);
                                book.setSearchSubtitle(bookSubtitle);
                                book.saveInBackground(new SaveCallback() {
                                                          @Override
                                                          public void done(ParseException e) {
                                                              if (e != null ) {
                                                                  Log.i(TAG, "Exeception " + e);
                                                              } else {
                                                                  Log.i(TAG, "Saved !");
                                                              }
                                                          }
                                                      });
                                        searchedBookArrayList.add(book);
                                Log.i(TAG,searchedBookArrayList.toString());
                                BooksAdapter searchAdapter = new BooksAdapter(getContext(),searchedBookArrayList);
                                LinearLayoutManager linearLayoutManagerSearch = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                                RecyclerView recyclerViewSearch = (RecyclerView) view.findViewById(R.id.idRVBooks);
                                recyclerViewSearch.setLayoutManager(linearLayoutManagerSearch);
                                recyclerViewSearch.setAdapter(searchAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "No data found ",e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error is found" + error, Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(requestBookObject);

            }

        });

    }
}