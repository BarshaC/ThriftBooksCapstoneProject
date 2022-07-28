package com.example.thriftbooks;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.thriftbooks.models.Book;
import com.example.thriftbooks.models.Comment;
import com.example.thriftbooks.models.Message;
import com.example.thriftbooks.models.MessageThread;
import com.example.thriftbooks.models.Post;
import com.example.thriftbooks.models.User;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.facebook.ParseFacebookUtils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    public static final String CHANNEL_ID_DEFAULT = "Messages";
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Book.class);
        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(MessageThread.class);
        OkHttpClient.Builder builder = new OkHttpClient.Builder(); //monitoring Parse traffic
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server("https://parseapi.back4app.com").enableLocalDataStore()
                .build()
        );
        ParseFacebookUtils.initialize(ParseApplication.this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId",
                "994839956430");
        installation.saveInBackground();
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        createNotificationChannels();
    }
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_DEFAULT,"Messages", importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
