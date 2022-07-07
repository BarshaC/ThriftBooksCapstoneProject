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
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity";
    private EditText etMessage;
    private ImageButton ibSend;
    private MessageAdapter mAdapter;
    private RecyclerView rvMessage;
    private ArrayList<Message mMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


    }
    void messagePosting() {
        etMessage = (EditText) findViewById(R.id.etMessage);
        rvMessage = (RecyclerView) findViewById(R.id.rvChat);
        mMessages = new ArrayList<>();
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
                message.setUserId(ParseUser.getCurrentUser().getObjectId());
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

}