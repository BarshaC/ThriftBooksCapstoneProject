package com.example.thriftbooks.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Book")
public class Book extends ParseObject {
    public static final String DEFAULT_BOOK_DESCRIPTION = "description";
    public static final String DEFAULT_BOOK_TITLE = "defaultTitle";
    public static final String DEFAULT_BOOK_AUTHOR = "defaultAuthor";


}
