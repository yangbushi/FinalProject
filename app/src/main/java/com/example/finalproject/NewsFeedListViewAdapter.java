package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**define adapter
 *
 */

public class NewsFeedListViewAdapter extends ArrayAdapter<NewsFeed> {
    /**
     * define a ArrayList of NewsFeed
     */
    private ArrayList<NewsFeed> newsFeedList = new ArrayList<>();
    /**
     * define reference of NewsFeedMain
     */
    private NewsFeedMain newsFeedExamplethis = null;

    public NewsFeedListViewAdapter(Context ctx) {

        super(ctx, 0);
    }

    public void setNewsFeedList(ArrayList<NewsFeed> NewsFeedList){
        this.newsFeedList = NewsFeedList;
    }

    public void setNewsFeedExample(NewsFeedMain b){
        this.newsFeedExamplethis = b;
    }

    public int getCount() {

        return newsFeedList.size();
    }

    public long getItemId(int position) {
        return position;

    }

    public NewsFeed getItem(int position) {

        return newsFeedList.get(position);
    }

    /**
     * define the output content on the page
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = newsFeedExamplethis.getLayoutInflater();
        View result = null;
        NewsFeed current = (NewsFeed)getItem(position);
        result = inflater.inflate(R.layout.activity_news_feed_item, null);
        TextView newsFeedTitle = (TextView) result.findViewById(R.id.NewsFeedTitle);
        newsFeedTitle.setText(current.getTitle());

        TextView newsFeedUrl = (TextView) result.findViewById(R.id.NewsFeedUrl);
        newsFeedUrl.setText(current.getUrl());
        return result;
    }

}