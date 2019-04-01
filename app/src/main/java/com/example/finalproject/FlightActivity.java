package com.example.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 'Flight status tracker'
 * @author Dustin Horricks
 * @version 1.0.0
 */

public class FlightActivity extends AppCompatActivity {

    private String arrivalAPI = "http://aviation-edge.com/v2/public/flights?key=e66fe0-74b486&arrIata=YOW";
    private String departAPI = "http://aviation-edge.com/v2/public/flights?key=e66fe0-74b486&depIata=YOW";
    private ListView fListView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);

        Toolbar toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        FlightAPI netWorkThread = new FlightAPI();
        netWorkThread.execute(arrivalAPI);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu_bar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_article:
                Intent articleIntent = new Intent(this, ArticleActivity.class);
                startActivity(articleIntent);
                break;
            case R.id.menu_dic:
                break;
            case R.id.menu_flight:
                Intent flightIntent = new Intent(this, FlightActivity.class);
                startActivity(flightIntent);
                break;
            case R.id.menu_news:
                break;
        }

        return true;
    }
}

    class FlightAPI extends AsyncTask<String, Integer, String> {

    String speed, altitude, status;

        @Override
        protected String doInBackground(String... strings) {

            String myUrl = strings[0];

            try {

                //this creates the connection to the network
                URL url = new URL(myUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();

                //create JSON obj from the response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String result = sb.toString();

                //now to create a JSON table:
                JSONObject jsonObject = new JSONObject(result);
                speed = jsonObject.getString("speed");
                Log.i("Speed is: ", "" + speed);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "Finished..";
        }
    }
