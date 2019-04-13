package com.example.finalproject;
import java.util.ArrayList;

public class NewsFeed {

    /**
     * define a int variable:id
     */
    private int id;
    /**
     * define a String variable:title
     */
    private String title;
    /**
     * define a String variable:url
     */
    private String url;
    /**
     * define a ArrayListof:url
     */
    private ArrayList<String> content;
    /**
     * define a String variable:searchWord
     */
    private String searchWord;


    public NewsFeed(){
        this.content = new ArrayList<>();
    }

    public NewsFeed(String title, String url) {
        super();

        this.title = title;
        this.url = url;
    }


    //define all getters or setters
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
        return content;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }

    public void addContent(String a){
        this.content.add(a);
    }
    public String searchWord() {
        return searchWord;
    }

    public void setKeyword(String searchWord) {
        this.searchWord = searchWord;
    }
    @Override
    public String toString() {
        return "NewsFeed [title=" + title + ", url=" + url
                + "]";
    }
}