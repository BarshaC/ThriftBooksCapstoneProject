package com.example.thriftbooks;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("igT6i4fRnseJqacXHfylAQuK0wsNWC4saosQ5Vwx")
                .clientKey("IGezFO91Gjj9PY5StRJ8oDNC2aYFSa0BJ2wGrNt7")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
