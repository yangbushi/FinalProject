package com.example.finalproject;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
*main function of my interface
 */
public class NewsFeedMain  extends AppCompatActivity {
    /**
     * define a toolbar
     */
    private Toolbar tBar;
    /**
     * define a message of String
     */
    private String message = "Init message";
    /**
     * define a newsfeed's arraylist
     */
    private ArrayList<NewsFeed> NewsFeedList = new ArrayList<>();
    /**
     * define a ACTIVITY_NAME
     */
    protected static final String ACTIVITY_NAME = "NewsFeedMain";
    /**
     * define a instance of the NewsFeedDatabase
     */
    protected static NewsFeedDatabase NewsFeedData;
    /**
     * define a instance of the SQLiteDatabase
     */
    protected SQLiteDatabase NewsFeedDb;
    /**
     * define a adapter of the NewsFeed
     */
    NewsFeedListViewAdapter NewsFeedListViewAdapter;

    /**
     * define a SharedPreferences of the NewsFeed
     */
    SharedPreferences NewsFeedSharedPref;
    /**
     * define a EditText
     */
    EditText typeField;
    /**
     * define a PROGRESSSPEED
     */
    public static final int PROGRESSSPEED = 10;
    /**
     * define a ProgressBar
     */
    private ProgressBar progressBar;
    /**
     * define and initiaize a instance of NewsFeed
     */
    public NewsFeed NewsFeedFromXML= new NewsFeed();
    /**
     * define a String named title
     */
    public static final String ITEM_TITLE = "TITLE";
    /**
     * define a String named url
     */
    public static final String ITEM_URL = "URL";
    /**
     * define a String named content
     */
    public static final String ITEM_Content = "CONTENT";
    /**
     * define a String named position
     */
    public static final String ITEM_POSITION = "POSITION";
    /**
     * define a String named id
     */
    public static final String ITEM_ID = "ID";
    /**
     * define a int variable  named EMPTY_ACTIVITY
     */
    public static final int EMPTY_ACTIVITY = 345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);
        NewsFeedData = new NewsFeedDatabase(this);
        NewsFeedDb = NewsFeedData.getWritableDatabase();
        setSupportActionBar(tBar);
        message = "This is the final project";
       //get the list of the before
        List<NewsFeed> list = NewsFeedData.getAllNewsFeeds();
        NewsFeedList.addAll(list);
        boolean isTablet = findViewById(R.id.fragmentLocation) != null;
        ListView NewsFeedListView = (ListView) findViewById(R.id.NewsFeedList);
        NewsFeedListViewAdapter = new NewsFeedListViewAdapter(this);
        NewsFeedListViewAdapter.setNewsFeedList(NewsFeedList);
        NewsFeedListViewAdapter.setNewsFeedExample(this);
        NewsFeedListView.setAdapter(NewsFeedListViewAdapter);

        NewsFeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle dataToPass = new Bundle();
                dataToPass.putString(ITEM_TITLE, NewsFeedList.get(position).getTitle());
                dataToPass.putString(ITEM_URL, NewsFeedList.get(position).getUrl());
                dataToPass.putStringArrayList(ITEM_Content, NewsFeedList.get(position).getContent());
                dataToPass.putInt(ITEM_POSITION, position);
                dataToPass.putInt(ITEM_ID, NewsFeedList.get(position).getId());
                //appear the different effect according the different devices
                if(isTablet)
                {
                    NewFeedDetail dFragment = new NewFeedDetail();
                    dFragment.setArguments( dataToPass );
                    dFragment.setTablet(true);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragmentLocation, dFragment)
                            .commit();
                }
                else
                {
                    Intent nextActivity = new Intent(NewsFeedMain.this, NewsFeedItem.class);
                    nextActivity.putExtras(dataToPass);
                    startActivityForResult(nextActivity, EMPTY_ACTIVITY);
                }
            }
        });

        //reserve the history of search
        typeField = (EditText)findViewById(R.id.typeField);
        NewsFeedSharedPref = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String resevedWord = NewsFeedSharedPref.getString("reserved", "Default value");
        typeField.setText(resevedWord);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        NewsFeedGet.setNewsFeedExample(this);
        //delete the reserved content before
        Button favorite = (Button)findViewById(R.id.favorite);
        favorite.setOnClickListener(v->{
            Intent favoriteIntent = new Intent(this, view_favorite.class);
            startActivity(favoriteIntent);
        });
        //search news from internet
        Button SearchButton = findViewById(R.id.SearchButton);
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String INputword = typeField.getText().toString();
                NewsFeedQuery Query = new NewsFeedQuery();
                NewsFeedFromXML.setKeyword(INputword);
                Query.execute();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        //get an editor object of SharedPreferences
        SharedPreferences.Editor editor = NewsFeedSharedPref.edit();

        //save what was typed under the name "ReserveName"
        String TypedWord = typeField.getText().toString();
        editor.putString("reserved", TypedWord);

        //write it to disk:
        editor.commit();
    }

    public void onActivityResult(int requestCode,int resultCode, Intent data) {
        if(requestCode == EMPTY_ACTIVITY)
        {
            if(resultCode == RESULT_OK) //if you hit the save button instead of back button
            {
                int position = data.getIntExtra(ITEM_POSITION, 0);
                saveListMessage(position);
            }
        }
    }

    public void saveListMessage(int position)//save the results to database
    {
        NewsFeedData.addNewsFeed(NewsFeedList.get(position));

    }

    @Override
    /**
     * Inflate the menu
      */

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
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
                Intent feedIntent = new Intent(this, NewsFeedMain.class);
                startActivity(feedIntent);
                break;
            case R.id.menu_about:
                //Show the toast
                Toast.makeText(this, "This is my final project!", Toast.LENGTH_LONG).show();
                //show the snackbar
                Snackbar snackbar = Snackbar.make(tBar, "Go Back?", Snackbar.LENGTH_LONG)
                        .setAction("GoBack", e ->{
                            Log.e("Menu Example", "Clicked GoBack");
                                       finish();
                        });
                snackbar.show();
                break;
            case R.id.menu_help:
                HelpExample();
                break;
        }
        return true;
    }

    public void HelpExample()
    {
        View middle = getLayoutInflater().inflate(R.layout.view_extra_stuff, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Author: Tao Shen\nVersion: 1.0")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).setView(middle);

        builder.create().show();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        // close the database
        NewsFeedData.close();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }


    private class NewsFeedQuery extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            NewsFeedGet.setNewsFeedFromXML(NewsFeedFromXML);

            try {

                if(NewsFeedGet.openStream(typeField.getText().toString())){
                    NewsFeedList = new ArrayList<>();

                    publishProgress(25);
                    Thread.sleep(2000 / PROGRESSSPEED);
                    NewsFeedList = NewsFeedGet.readNewsFeedTag();

                    publishProgress(50);
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return NewsFeedGet.FINISH;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i(ACTIVITY_NAME, "update:" + values[0]);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            NewsFeedListViewAdapter.setNewsFeedList(NewsFeedList);
            NewsFeedListViewAdapter.notifyDataSetChanged();
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
