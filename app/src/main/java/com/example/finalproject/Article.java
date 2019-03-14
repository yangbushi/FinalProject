package com.example.finalproject;

/**
 * The structure of searched out article list
 * @author George Yang
 * @version 1.0.0
 */
public class Article {
    private int id;
    private String title;
    private String link;

    public Article() {
        this(0, "", "");
    }

    public Article(int id, String title, String link)
    {
        this.id = id;
        this.title = title;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
