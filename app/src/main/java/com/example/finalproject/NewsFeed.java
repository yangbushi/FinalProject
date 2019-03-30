package com.example.finalproject;



import java.util.ArrayList;

public class NewsFeed {
    private int id;
    private String title;
    private ArrayList<String> author;

    private String year;
    private String price;

    public NewsFeed(){
        this.author = new ArrayList<>();
    }

    public NewsFeed(String title, ArrayList<String> author) {
        super();

        this.title = title;
        this.author = author;
    }

    public NewsFeed(String title, String a) {
        super();

        this.title = title;
        this.author = new ArrayList<>();
        author.add(a);
    }

    public NewsFeed(String title, ArrayList<String> author, String year, String price) {
        super();

        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
    }

    public NewsFeed(int id, String title, ArrayList<String> author) {
        super();
        this.id = id;
        this.title = title;
        this.author = author;
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

    public ArrayList<String> getAuthor() {
        return author;
    }

    public void setAuthor(ArrayList<String> author) {
        this.author = author;
    }
    public void setAuthor(String author) {
        this.author.add(author);
    }
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void addAuthor(String a){
        if(author == null)
            this.author = new ArrayList<>();
        this.author.add(a);
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + title + ", author=" + author
                + "]";
    }
}