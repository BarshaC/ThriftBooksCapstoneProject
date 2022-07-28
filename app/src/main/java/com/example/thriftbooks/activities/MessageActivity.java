package com.example.thriftbooks.activities;

import android.app.Notification;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thriftbooks.MessageAdapter;
import com.example.thriftbooks.ParseApplication;
import com.example.thriftbooks.R;
import com.example.thriftbooks.models.Message;
import com.example.thriftbooks.models.MessageThread;
import com.example.thriftbooks.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import org.parceler.Parcels;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity";
    private EditText etMessage;
    private ImageButton ibSend;
    private TextView username;
    private ImageView userImage;
    private MessageAdapter mAdapter;
    private RecyclerView rvMessage;
    private ArrayList<Message> mMessages;
    static final int MAX_MESSAGES_TO_SHOW = 50;
    private MessageThread thread;
    private List<ParseQuery<Message>> allQueries;
    private NotificationManagerCompat notificationManagerCompat;
    private User otherUser;
    private User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        thread = Parcels.unwrap(getIntent().getParcelableExtra("messageThreadInfo"));
        currentUser = (User) ParseUser.getCurrentUser();
        if (thread.getBuyerId().getObjectId().equals(currentUser)){
            otherUser = thread.getSellerId();
        } else{
            otherUser = thread.getBuyerId();
        }
        if (ParseUser.getCurrentUser() != null) {
            startWithCurrentUser();
        }
        userImage = findViewById(R.id.ivProfileImage);
        ParseFile image = thread.getBuyerId().getProfileImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(userImage);
        }
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(userImage);
        }
        refreshMessages();
        String webSocketUrl = "wss://" + "thriftbooksapp.b4a.io";
        ParseLiveQueryClient parseLiveQueryClient = null;
        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(webSocketUrl));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        // Listen for CREATE events on the Message class
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, message) -> {

            Message newMessage = (Message) message;
            User sender = null;
            User receiver = null;
            try {
                sender = (User) newMessage.getSenderId().fetchIfNeeded();
                receiver = (User) newMessage.getReceiver().fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
//
            if (sender.getObjectId().equals(otherUser.getObjectId()) && (receiver.getObjectId().equals(currentUser.getObjectId()))) {
                mMessages.add(0, newMessage);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyItemInserted(0);
                        rvMessage.smoothScrollToPosition(0);
                    }
                });
            }

        });
        notificationManagerCompat = NotificationManagerCompat.from(this);
        rvMessage = findViewById(R.id.rvChat);
        username = findViewById(R.id.tvOtherUsername);
        mMessages = new ArrayList<>();
        final User currentUserId = thread.getSellerId();
        final User otherUserId = thread.getBuyerId();
        mAdapter = new MessageAdapter(MessageActivity.this, currentUserId, otherUserId, mMessages);
        rvMessage.setAdapter(mAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessageActivity.this);
        linearLayoutManager.setReverseLayout(true);
        String usernameBuyer = thread.getBuyerId().getUsername();
        username.setText(usernameBuyer);
        rvMessage.setLayoutManager(linearLayoutManager);


    }


    void messagePosting() {
        etMessage = findViewById(R.id.etMessage);

        ibSend = findViewById(R.id.ibSend);

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                Message message = new Message();
                message.setReceiver(otherUser);
                message.setSenderId(currentUser);
                message.setBody(data);
                message.setThreadId(thread);
                mMessages.add(0, message);
                mAdapter.notifyItemInserted(0);
                rvMessage.smoothScrollToPosition(0);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
//                        mMessages.clear();

                    }
                });
                etMessage.setText(null);
                Notification notification = new NotificationCompat.Builder(getApplicationContext(), ParseApplication.CHANNEL_ID_DEFAULT).setSmallIcon(R.drawable.ic_baseline_message_24).setContentTitle("ThriftBooks").setContentText(data).setPriority(NotificationManagerCompat.IMPORTANCE_UNSPECIFIED).setCategory(NotificationCompat.CATEGORY_MESSAGE).build();
                ParseQuery pushQuery = ParseInstallation.getQuery();
                ArrayList<String>  channels = new ArrayList<String>();
                channels.add((String) message.getSenderId().getObjectId());
                channels.add((String) message.getReceiver().getObjectId());
                ParsePush push = new ParsePush();
                push.setQuery(pushQuery);
                push.setMessage(data);
                push.setChannel(otherUser.getObjectId());
                push.sendInBackground();

            }
        });
    }

    void refreshMessages() {
        ParseQuery<Message> query1 = ParseQuery.getQuery(Message.class);
        query1.whereEqualTo(Message.MESSAGE_SENDER_ID_KEY, currentUser);
        query1.whereEqualTo(Message.MESSAGE_RECEIVER_POINTER_KEY, otherUser);

        ParseQuery<Message> query2 = ParseQuery.getQuery(Message.class);
        query2.whereEqualTo(Message.MESSAGE_SENDER_ID_KEY, otherUser);
        query2.whereEqualTo(Message.MESSAGE_RECEIVER_POINTER_KEY, currentUser);

        allQueries = new ArrayList<>();
        allQueries.add(query1);
        allQueries.add(query2);

        ParseQuery<Message> mainQuery = ParseQuery.or(allQueries);
        mainQuery.setLimit(MAX_MESSAGES_TO_SHOW);
        mainQuery.include(Message.MESSAGE_RECEIVER_POINTER_KEY);
        mainQuery.orderByDescending("createdAt");
        mainQuery.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    Log.i(TAG, messages.toString());
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged();
                    rvMessage.smoothScrollToPosition(0);
                }
            }
        });
    }

    void startWithCurrentUser() {
        messagePosting();
    }
}