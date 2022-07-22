package com.example.thriftbooks;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.example.thriftbooks.models.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookClient {
    private static final String TAG = "BookClient";
    public static final String baseAPI_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    String api_key = BuildConfig.API_KEY;
    private RequestQueue mRequestQueue;
    private final AsyncHttpClient client;
    private RequestQueue requestQuery;
    private ArrayList<Book> queryBookArrayList;

    public BookClient() {
        this.client = new AsyncHttpClient();
    }

    private String getBaseAPI_URL(String relativeURL) {
        return baseAPI_URL + relativeURL;
    }

    public ArrayList getBooks(final String query) {
        queryBookArrayList = new ArrayList<>();
        String url = getBaseAPI_URL(query + "&key=" + api_key);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray itemBooks = response.getJSONArray("items");
                            Log.d(TAG, "Checking for bookitems");
                            for (int i = 0; i < itemBooks.length(); i++) {
                                JSONObject itemObject = itemBooks.getJSONObject(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        /* Add your Requests to the RequestQueue to execute */
        mRequestQueue.add(req);
        return queryBookArrayList;
    }

}
