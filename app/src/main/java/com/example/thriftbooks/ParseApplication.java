package com.example.thriftbooks;

import android.app.Application;

import com.example.thriftbooks.models.Book;
import com.example.thriftbooks.models.Comment;
import com.example.thriftbooks.models.Message;
import com.example.thriftbooks.models.Post;
import com.example.thriftbooks.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Book.class);
        ParseObject.registerSubclass(Comment.class);
        OkHttpClient.Builder builder = new OkHttpClient.Builder(); //monitoring Parse traffic
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("igT6i4fRnseJqacXHfylAQuK0wsNWC4saosQ5Vwx")
                .clientKey("IGezFO91Gjj9PY5StRJ8oDNC2aYFSa0BJ2wGrNt7")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
