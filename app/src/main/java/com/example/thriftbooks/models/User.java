package com.example.thriftbooks.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String KEY_IMAGE_PROFILE = "profilePicture";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_USER_FIRST_NAME = "firstName";
    public static final String KEY_USER_LAST_NAME = "lastName";
    public static final String KEY_EMAIL_VERIFIED = "emailVerified"; //Will use later when email verification is set up
    public static final String KEY_USER_ACCOUNT_CREATED_AT = "createdAt";
    public static final String KEY_DATE_OF_BIRTH = "dateOfBirth";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_FAV_GENRE = "favGenreBook";

    public User() {
    }

    public String getUserEmail() {
        return getString(KEY_USER_EMAIL);
    }

    public void setEmail(String email) {
        put(KEY_USER_EMAIL, email);
    }

    public String getUserFirstName() {
        return getString(KEY_USER_FIRST_NAME);
    }

    public void setUserFirstName(String firstName) {
        put(KEY_USER_FIRST_NAME, firstName);
    }

    public String getUserLastName() {
        return getString(KEY_USER_LAST_NAME);
    }

    public void setUserLastName(String lastName) {
        put(KEY_USER_LAST_NAME, lastName);
    }

    public String getDateOfBirth() {
        return getString(KEY_DATE_OF_BIRTH);
    }

    public void setDateOfBirth(String DateOfBirth) {
        put(KEY_DATE_OF_BIRTH, DateOfBirth);
    }

    public String getFavGenre() {
        return getString(KEY_USER_FAV_GENRE);
    }

    public void setFavGenre(String favGenreUser) {
        put(KEY_USER_FAV_GENRE, favGenreUser);
    }

    public ParseFile getProfileImage() {
        return getParseFile(KEY_IMAGE_PROFILE);
    }

    public void setProfileImage(ParseFile parseFile) {
        put(KEY_IMAGE_PROFILE, parseFile);
    }

}
