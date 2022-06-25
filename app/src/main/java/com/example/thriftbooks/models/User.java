package com.example.thriftbooks.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String KEY_IMAGE_PROFILE = "profilePicture";

    public User() {
    }
    public ParseFile getProfileImage() {
        return getParseFile(KEY_IMAGE_PROFILE);
    }
    public void setProfileImage(ParseFile parseFile) {
        put(KEY_IMAGE_PROFILE, parseFile);
    }

}
