package com.example.thriftbooks.models;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_BOOK_IMAGE = "bookImage";
    public static final String KEY_BOOK_TITLE = "titleBook";
    public static final String KEY_BOOK_AUTHOR = "authorBook";
    public static final String KEY_BOOK_DESCRIPTION = "description";
    public static final String KEY_BOOK_TYPE = "bookType";
    public static final String KEY_BOOK_CONDITION = "bookCondition";
    public static final String KEY_BOOK_COMMENT = "postComment";
    public Post() {}
    public String getDescription() {
        return getString(KEY_BOOK_DESCRIPTION);
    }
    public  void setDescription(String description) {
        put(KEY_BOOK_DESCRIPTION, description);
   }

    public ParseFile getImage() {
        return getParseFile(KEY_BOOK_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_BOOK_IMAGE, parseFile);
    }

    public String getBookTitle() { return getString(KEY_BOOK_TITLE); }

    public void setBookTitle(String titleBook) { put(KEY_BOOK_TITLE,titleBook); }

    public String getBookAuthor() { return getString(KEY_BOOK_AUTHOR);}

    public void setBookAuthor(String authorBook ) { put(KEY_BOOK_AUTHOR, authorBook);}

    public String getBookType() { return getString(KEY_BOOK_TYPE); }

    public void setBookType(String bookType) { put(KEY_BOOK_TYPE,bookType); }

    public String getBookCondition() { return getString(KEY_BOOK_CONDITION); }


    public void setBookCondition(String bookCondition) { put(KEY_BOOK_CONDITION,bookCondition); }

    public boolean getPostComment() {
        ParseQuery<ParseObject> commentQuery =  this.getRelation(KEY_BOOK_COMMENT).getQuery();
        commentQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> comments, ParseException e) {
            }
        });
        return true;
    }

    public User getUser() {
        return (User) getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }


}