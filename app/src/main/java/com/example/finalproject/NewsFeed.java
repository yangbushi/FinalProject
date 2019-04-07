package com.example.finalproject;



import java.util.ArrayList;

public class NewsFeed {
    private int id;
    private String title;
    private String url;

    private ArrayList<String> Content;
    private String searchWord;
  //  private String price;

    public NewsFeed(){
        this.Content = new ArrayList<>();
    }

    public NewsFeed(String title, String url) {
        super();

        this.title = title;
        this.url = url;
    }



    public NewsFeed(int id, String title, ArrayList<String> Content, String url) {
        super();
        this.id = id;
        this.title = title;
        this.Content = Content;
        this.url = url;
    }
    //getters & setters
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

    public String getUrl() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public  ArrayList<String> getContent() {
        return Content;
    }

    public void setContent(ArrayList<String> Content) {
        this.Content = Content;
    }

    public void addContent(String a){
        this.Content.add(a);
    }
    public String searchWord() {
        return searchWord;
    }

    public void setKeyword(String searchWord) {
        this.searchWord = searchWord;
    }
    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + title + ", url=" + url
                + "]";
    }
}