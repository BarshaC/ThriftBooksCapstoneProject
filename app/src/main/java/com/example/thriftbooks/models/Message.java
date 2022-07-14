package com.example.thriftbooks.models;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String MESSAGE_SENDER_ID_KEY = "senderId";
    public static final String MESSAGE_RECEIVER_ID_KEY = "receiverId";
    public static final String MESSAGE_THREAD_ID_KEY = "threadId";
    public static final String POST_ID_KEY = "postId";
    public static final String BODY_MESSAGE_KEY = "body";

    public User getSenderId() {
        return (User) getParseUser(MESSAGE_SENDER_ID_KEY);
    }

    public void setSenderId(User senderId) {
        put(MESSAGE_SENDER_ID_KEY, senderId);
    }

    public User getReceiverId() {
        return (User) getParseUser(MESSAGE_RECEIVER_ID_KEY);
    }

    public void setReceiverId(User receiverId) {
        put(MESSAGE_RECEIVER_ID_KEY, receiverId); }


    public MessageThread getThreadId() { return (MessageThread) getParseObject(MESSAGE_THREAD_ID_KEY); }

    public void setThreadId(MessageThread threadId) { put(MESSAGE_THREAD_ID_KEY, threadId); }

    public Post getPostId() {
         return (Post) getParseObject(POST_ID_KEY);
    }

    public void setPostId(Post postId) {
        put(POST_ID_KEY, postId);
    }

    public String getBody() {
        return getString(BODY_MESSAGE_KEY);
    }

    public void setBody(String body) {
        put(BODY_MESSAGE_KEY, body);
    }
}
