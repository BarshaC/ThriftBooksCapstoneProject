package com.example.thriftbooks.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("MessageThread")
public class MessageThread extends ParseObject {
    public static final String TAG = "MessageThread class";
    public static final String KEY_THREAD_ID = "objectId";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_THREAD_UPDATED_AT = "updatedAt";
    public static final String KEY_THREAD_POST_ID = "PostId";
    public static final String KEY_THREAD_BUYER_ID = "BuyerId";
    public static final String KEY_THREAD_SELLER_ID = "SellerId";
    public static final String KEY_MESSAGE_STARTER = "starterMessage";

    public MessageThread() {}

    public User getBuyerId() {
        return (User) getParseUser(KEY_THREAD_BUYER_ID);
    }

    public void setBuyerId(User buyerId) {
        put(KEY_THREAD_BUYER_ID, buyerId);
    }

    public User getSellerId() {
        return (User) getParseUser(KEY_THREAD_SELLER_ID);
    }

    public void setSellerId(User sellerId) {
        put(KEY_THREAD_SELLER_ID, sellerId);
    }

    public Post getPostId() {
        return (Post) getParseObject(KEY_THREAD_POST_ID);
    }

    public void setPostId(Post postId) {
        put(KEY_THREAD_POST_ID, postId);
    }

    public String getMessageStarter() { return getString(KEY_MESSAGE_STARTER); }

    public void setMessageStarter(String messageStarter) { put(KEY_MESSAGE_STARTER, messageStarter); }

}
