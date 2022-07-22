package com.example.thriftbooks.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;


@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String TAG = "Post.java";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_POST_IMAGE = "bookImage";
    public static final String KEY_POST_BOOK_TITLE = "titleBook";
    public static final String KEY_POST_BOOK_AUTHOR = "authorBook";
    public static final String KEY_POST_BOOK_DESCRIPTION = "description";
    public static final String KEY_POST_BOOK_TYPE = "bookType";
    public static final String KEY_POST_BOOK_CONDITION = "bookCondition";
    public static final String KEY_POST_BOOK_COMMENT = "postComment";

    public Post() {
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

    public String getDescription() {
        return getString(KEY_POST_BOOK_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_POST_BOOK_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_POST_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_POST_IMAGE, parseFile);
    }

    public String getBookTitle() {
        return getString(KEY_POST_BOOK_TITLE);
    }

    public void setBookTitle(String titleBook) {
        put(KEY_POST_BOOK_TITLE, titleBook);
    }

    public String getBookAuthor() {
        return getString(KEY_POST_BOOK_AUTHOR);
    }

    public void setBookAuthor(String authorBook) {
        put(KEY_POST_BOOK_AUTHOR, authorBook);
    }

    public String getBookType() {
        return getString(KEY_POST_BOOK_TYPE);
    }

    public void setBookType(String bookType) {
        put(KEY_POST_BOOK_TYPE, bookType);
    }

    public String getBookCondition() {
        return getString(KEY_POST_BOOK_CONDITION);
    }


    public void setBookCondition(String bookCondition) {
        put(KEY_POST_BOOK_CONDITION, bookCondition);
    }

    public Comment getPostComment() {
        return (Comment) getParseObject(KEY_POST_BOOK_COMMENT);
    }

    public void setPostComment(Comment postComment) {
        put(KEY_POST_BOOK_COMMENT, postComment);
    }
    //Might use this code later if I use relation to the comment not the pointer
    //Using pointer for now
//    public boolean getPostComment() {
//        ParseQuery<ParseObject> commentQuery = this.getRelation(KEY_POST_BOOK_COMMENT).getQuery();
//        commentQuery.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> comments, ParseException e) {
//            }
//        });
//        return true;
//    }

    public User getUser() {
        return (User) getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }



}