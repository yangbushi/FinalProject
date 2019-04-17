package com.example.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

/**
 * The main entry of the application
 * @author George Yang
 * @version 1.0.0
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // Jump to 'New York Times Article Search' by clicking the relative button
        Button articleButton = (Button)findViewById(R.id.article_button);
        articleButton.setOnClickListener(v->{
            Intent articleIntent = new Intent(this, ArticleActivity.class);
            startActivity(articleIntent);
        });
//go to  news_feed
        Button feedButton = (Button)findViewById(R.id.news_button);
        feedButton.setOnClickListener(v->{
            Intent feedIntent = new Intent(this, NewsFeedMain.class);
            startActivity(feedIntent);
        });

        Button dicButton = (Button)findViewById(R.id.dic_button);
        dicButton.setOnClickListener(v->{
            Intent dictIntent = new Intent(this, DictActivity.class);
            startActivity(dictIntent);
        });

        Button flightButton = (Button)findViewById(R.id.flight_button);
        flightButton.setOnClickListener(v->{
            Intent flightIntent = new Intent(this, FlightActivity.class);
            startActivity(flightIntent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);

        return true;
    }

    /**
     * Jump to different activity / function based on the menu icon clicked
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_article:
                Intent articleIntent = new Intent(this, ArticleActivity.class);
                startActivity(articleIntent);
                break;
            case R.id.menu_dic:
                Intent dictIntent = new Intent(this, DictActivity.class);
                startActivity(dictIntent);
                break;
            case R.id.menu_flight:
                Intent flightIntent = new Intent(this, FlightActivity.class);
                startActivity(flightIntent);
                break;
            case R.id.menu_news:
                Intent feedIntent = new Intent(this, NewsFeedMain.class);
                startActivity(feedIntent);
                break;
        }

        return true;
    }
}
