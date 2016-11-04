/*
 * Copyright 2016 Instil Software.
 */
package com.deloitte.shivnotes;

import java.util.Date;

public class Note {

    private final String title;
    private final String text;
    private final String date;

    public Note(String title, String text) {
        this(title, text, new Date().toString());
    }

    public Note(String title, String text, String date) {
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("Title: %s\nText: %s\nDate: %s\n", title, text, date);
    }

}
