package com.example.thriftbooks.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thriftbooks.MessageAdapter;
import com.example.thriftbooks.R;
import com.example.thriftbooks.models.Message;
import com.example.thriftbooks.models.MessageThread;
import com.example.thriftbooks.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

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
    boolean mFirstLoad;
    private MessageThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        if (ParseUser.getCurrentUser() != null ) {
            startWithCurrentUser();
        }
        userImage = (ImageView) findViewById(R.id.ivProfileImage);
        ParseFile image = thread.getBuyerId().getProfileImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(userImage);
        }
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(userImage);
        }
        refreshMessages();
    }
    void messagePosting() {
        etMessage = (EditText) findViewById(R.id.etMessage);
        rvMessage = (RecyclerView) findViewById(R.id.rvChat);
        username = (TextView) findViewById(R.id.tvOtherUsername);
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        thread = Parcels.unwrap(getIntent().getParcelableExtra("messageThreadInfo"));
        final User currentUserId = thread.getSellerId();
        final User otherUserId = thread.getBuyerId();
        mAdapter = new MessageAdapter(MessageActivity.this, currentUserId, otherUserId, mMessages);
        rvMessage.setAdapter(mAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessageActivity.this);
        linearLayoutManager.setReverseLayout(true);
        String usernameBuyer = thread.getBuyerId().getUsername();
        username.setText(usernameBuyer);
        rvMessage.setLayoutManager(linearLayoutManager);
        ibSend = (ImageButton) findViewById(R.id.ibSend);
        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                Message message = new Message();
                message.setReceiver(otherUserId);
                message.setSenderId(currentUserId);
                message.setBody(data);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            refreshMessages();
                        } else {
                            Log.e(TAG, "Failed to save message", e);
                        }
                    }
                });
                etMessage.setText(null);
            }
        });
    }

    void refreshMessages() {
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.setLimit(MAX_MESSAGES_TO_SHOW);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {
                if (e == null ) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged();
                    if (mFirstLoad) {
                        rvMessage.scrollToPosition(0);
                        mFirstLoad = false;
                    } else {
                        Log.e("message", "Error Loading Messages",  e);
                    }
                }
            }
        });
    }

    void startWithCurrentUser() {
        messagePosting();
    }

}