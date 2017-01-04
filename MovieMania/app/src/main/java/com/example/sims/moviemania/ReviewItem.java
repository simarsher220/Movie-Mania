package com.example.sims.moviemania;

/**
 * Created by sims on 28/12/16.
 */

public class ReviewItem {

    private String author;
    private String content;

    public ReviewItem(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
