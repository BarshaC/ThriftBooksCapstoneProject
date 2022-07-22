package com.example.thriftbooks.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String TAG = "Comment Model";
    public static final String KEY_COMMENT_CREATED_AT = "createdAt";
    public static final String KEY_COMMENT_ACTUAL = "actualComment";
    public static final String KEY_COMMENTATOR_POINTER_ID = "authorComment";
    public static final String KEY_POST_ID_COMMENT_REFERENCE = "postIDComment";

    public Comment() {
    }

    public static String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (Exception e) {
            Log.e(TAG, "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }

    public String getActualComment() {
        return getString(KEY_COMMENT_ACTUAL);
    }

    public void setCommentActual(String commentBody) {
        put(KEY_COMMENT_ACTUAL, commentBody);
    }

    public User getCommentator() {
        return (User) getParseUser(KEY_COMMENTATOR_POINTER_ID);
    }

    public void setCommentator(User commentator) {
        put(KEY_COMMENTATOR_POINTER_ID, commentator);
    }

    public Post getPostIDComment() {
        return (Post) getParseObject(KEY_POST_ID_COMMENT_REFERENCE);
    }

    public void setPostIdComment(Post postIdPointer) {
        put(KEY_POST_ID_COMMENT_REFERENCE, postIdPointer);
    }
}
