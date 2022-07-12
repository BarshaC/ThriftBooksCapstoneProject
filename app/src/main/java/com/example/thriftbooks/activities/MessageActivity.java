package com.example.thriftbooks.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thriftbooks.MessageAdapter;
import com.example.thriftbooks.R;
import com.example.thriftbooks.models.Message;
import com.example.thriftbooks.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity";
    private EditText etMessage;
    private ImageButton ibSend;
    private MessageAdapter mAdapter;
    private RecyclerView rvMessage;
    private ArrayList<Message> mMessages;
    static final int MAX_MESSAGES_TO_SHOW = 50;
    boolean mFirstLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        if (ParseUser.getCurrentUser() != null ) {
            startWithCurrentUser();
        }


    }
    void messagePosting() {
        etMessage = (EditText) findViewById(R.id.etMessage);
        rvMessage = (RecyclerView) findViewById(R.id.rvChat);
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        final String userId = ParseUser.getCurrentUser().getObjectId();
        mAdapter = new MessageAdapter(MessageActivity.this, userId, mMessages);
        rvMessage.setAdapter(mAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessageActivity.this);
        rvMessage.setLayoutManager(linearLayoutManager);
        ibSend = (ImageButton) findViewById(R.id.ibSend);
        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                Message message = new Message();
                message.setSenderId((User) ParseUser.getCurrentUser());
                message.setBody(data);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(MessageActivity.this, "Successfully created message on Parse",
                                    Toast.LENGTH_SHORT).show();
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

    private void queryMessages() {
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.include(Message.MESSAGE_THREAD_ID_KEY);
        query.setLimit(25);
        query.addDescendingOrder(Message.KEY_CREATED_AT);
        //query.whereNotEqualTo(Message.KEY_THREAD_BUYER_ID, (User) ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts on HomePage", e);
                } for (Message message: messages) {
                    Log.i(TAG, "Posts : " + message.getPostId() + ", " );
                }
                messages.addAll(messages);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

}