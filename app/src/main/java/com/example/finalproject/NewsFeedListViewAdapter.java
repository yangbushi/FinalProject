package com.example.finalproject;


import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

        import java.util.ArrayList;

import static com.example.finalproject.R.id.NewsFeedList;

public class NewsFeedListViewAdapter extends ArrayAdapter<NewsFeed> {

    private ArrayList<NewsFeed> NewsFeedList = new ArrayList<>();
    private NewsFeedMain NewsFeedExamplethis = null;

    public NewsFeedListViewAdapter(Context ctx) {

        super(ctx, 0);
    }

    public void setNewsFeedList(ArrayList<NewsFeed> NewsFeedList){
        this.NewsFeedList = NewsFeedList;
    }

    public void setNewsFeedExample(NewsFeedMain b){
        this.NewsFeedExamplethis = b;
    }

    public int getCount() {

        return NewsFeedList.size();
    }

    public long getItemId(int position) {
        return position;

    }

    public NewsFeed getItem(int position) {

        return NewsFeedList.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = NewsFeedExamplethis.getLayoutInflater();
        View result = null;


        result = inflater.inflate(R.layout.activity_news_feed_item, null);

        TextView NewsFeedID = (TextView) result.findViewById(R.id.NewsFeedID);
        NewsFeedID.setText(Integer.toString(getItem(position).getId()));

        TextView NewsFeedTitle = (TextView) result.findViewById(R.id.NewsFeedTitle);
        NewsFeedTitle.setText(getItem(position).getTitle());

        TextView NewsFeedUrl = (TextView) result.findViewById(R.id.NewsFeedUrl);
        NewsFeedUrl.setText(getItem(position).getUrl());

        return result;
    }

}