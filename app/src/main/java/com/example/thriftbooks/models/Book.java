package com.example.thriftbooks.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Book")
public class Book extends ParseObject {
    public static final String KEY_BOOK_CREATED_AT = "createdAt";
    public static final String KEY_BOOK_TITLE = "titleBook";
    public static final String KEY_BOOK_SUBTITLE = "titleBook";
    public static final String KEY_BOOK_AUTHOR = "authors";
    public static final String KEY_BOOK_DATE_PUBLISHED = "datePublished";
    public static final String KEY_BOOK_PUBLISHER = "publisher";
    public static final String KEY_BOOK_DESCRIPTION = "description";
    public static final String KEY_BOOK_PAGE_COUNT = "pageCount";

    public Book() {
    }

    public Date getCreatedAt() {
        return getDate(KEY_BOOK_CREATED_AT);
    }

    public String getSearchTitle() {
        return getString(KEY_BOOK_TITLE);
    }

    public void setSearchTitle(String title) {
        put(KEY_BOOK_TITLE, title);
    }

    public String getSearchSubtitle() {
        return getString(KEY_BOOK_SUBTITLE);
    }

    public void setSearchSubtitle(String subtitle) {
        put(KEY_BOOK_SUBTITLE, subtitle);
    }

    public ArrayList<String> getSearchAuthors() {
        List<String> authors = getList(KEY_BOOK_AUTHOR);

        return new ArrayList<>(authors);

    }

    public void setSearchAuthors(ArrayList<String> authors) {
        put(KEY_BOOK_AUTHOR, authors);
    }

    public String getSearchPublisher() {
        return getString(KEY_BOOK_PUBLISHER);
    }

    public void setSearchPublisher(String publisher) {
        put(KEY_BOOK_PUBLISHER, publisher);
    }

    public String getPublishedDate() {
        return getString(KEY_BOOK_DATE_PUBLISHED);
    }

    public void setPublisherDate(String publishedDate) {
        put(KEY_BOOK_DATE_PUBLISHED, publishedDate);
    }

    public String getAboutVolume() {
        return getString(KEY_BOOK_DESCRIPTION);
    }

    public void setAboutVolume(String aboutVolume) {
        put(KEY_BOOK_DESCRIPTION, aboutVolume);
    }

    public int getPageCount() {
        return getInt(KEY_BOOK_PAGE_COUNT);
    }

    public void setPageCount(int pageCount) {
        put(KEY_BOOK_PAGE_COUNT, pageCount);
    }

}
