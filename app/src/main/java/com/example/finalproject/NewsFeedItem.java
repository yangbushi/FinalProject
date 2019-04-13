package com.example.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * appear the fragment and pass data to the the fragment
 */
public class NewsFeedItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_feed_detail);
        Bundle dataToPass = getIntent().getExtras();
        NewFeedDetail dFragment = new NewFeedDetail();
        dFragment.setArguments( dataToPass );
        dFragment.setTablet(false);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentLocation, dFragment)
                .addToBackStack("AnyName")
                .commit();
    }
}
