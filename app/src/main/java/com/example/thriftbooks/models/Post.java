package com.example.thriftbooks.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;


@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_BOOK_IMAGE = "bookImage";
    public static final String KEY_BOOK_TITLE = "titleBook";
    public static final String KEY_BOOK_AUTHOR = "authorBook";
    public static final String KEY_BOOK_DESCRIPTION = "description";
    public static final String KEY_BOOK_TYPE = "bookType";
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

    public User getUser() {
        return (User) getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }


}