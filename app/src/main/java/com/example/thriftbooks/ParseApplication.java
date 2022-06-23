package com.example.thriftbooks;

import android.app.Application;

import com.example.thriftbooks.models.Book;
import com.example.thriftbooks.models.Post;
import com.example.thriftbooks.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Book.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("igT6i4fRnseJqacXHfylAQuK0wsNWC4saosQ5Vwx")
                .clientKey("IGezFO91Gjj9PY5StRJ8oDNC2aYFSa0BJ2wGrNt7")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
