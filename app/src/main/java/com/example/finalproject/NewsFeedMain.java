package com.example.finalproject;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

public class NewsFeedMain  extends AppCompatActivity {
    ////
    private Toolbar tBar;
    private String message = "Init message";
 //   private EditText keyWord;
  //  String strURL;
    ///
    private ArrayList<NewsFeed> NewsFeedList = new ArrayList<>();
    protected static final String ACTIVITY_NAME = "NewsFeedMain";

    // SQLite database
    protected static NewsFeedDatabase NewsFeedData;
    protected SQLiteDatabase NewsFeedDb;

    Cursor results;
    NewsFeedListViewAdapter NewsFeedListViewAdapter;

    ////
    SharedPreferences NewsFeedSharedPref;
    EditText typeField;
    ////

    ////
    public static final int PROGRESSSPEED = 10;
    private ProgressBar progressBar;
    public NewsFeed NewsFeedFromXML= new NewsFeed();
    ////

    ////
    public static final String ITEM_TITLE = "TITLE";
    public static final String ITEM_URL = "URL";
    public static final String ITEM_Content = "CONTENT";
  //  public static final String ITEM_PRICE = "PRICE";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final int EMPTY_ACTIVITY = 345;
    ////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        ////
        tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);
        ////
        NewsFeedData = new NewsFeedDatabase(this);
        NewsFeedDb = NewsFeedData.getWritableDatabase();

        setSupportActionBar(tBar);
        message = "This is the final project";
      Button SearchButton = findViewById(R.id.SearchButton);
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String INputword = typeField.getText().toString();
                NewsFeedQuery Query = new NewsFeedQuery();
                NewsFeedFromXML.setKeyword(INputword);

                //articleEntity.setLocation(word);
                Query.execute();
            }
        });

      /*  Intent prePage = getIntent();
        String keyWordTyped = prePage.getStringExtra(getString(R.string.preference_keyWord));
        EditText editText = findViewById(R.id.typeField);
        editText.setText(keyWordTyped);


        String strUrlHead = "http://webhose.io/filterWebContent?";
        String token = "cf1375e3-9fc1-4002-b876-806a616c1b28";
        String strUrlTail ="&format=xml&sort=crawled&q=";
        //http://webhose.io/filterWebContent?token=20da7245-5cb3-47f5-a3ed-e08148b81f6d&format=xml&sort=crawled&q=apple;
        strURL = strUrlHead + "token=" + token + strUrlTail + keyWordTyped;
       **/
   //     SearchButton.setOnClickListener(new View.OnClickListener() {
   //         final int REQUEST_IMAGE_CAPTURE = 1;
        /**
         * CRUD Operations
         * */
        // add Books
//        bookData.addBook(new Book("Android Application Development Cookbook", "Wei Meng Lee"));
//        bookData.addBook(new Book("Android Programming: The Big Nerd Ranch Guide", "Bill Phillips and Brian Hardy"));
//        bookData.addBook(new Book("Learn Android App Development", "Wallace Jackson"));

        // get all books
        //////////////////////////////////
       List<NewsFeed> list = NewsFeedData.getAllNewsFeeds();

        // delete one book
//        bookData.deleteBook(list.get(0));

        // get all books
        //NewsFeedData.getAllNewsFeeds();

        NewsFeedList.addAll(list);

        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded
        ///
        ListView NewsFeedListView = (ListView) findViewById(R.id.NewsFeedList);
        NewsFeedListViewAdapter = new NewsFeedListViewAdapter(this);
        NewsFeedListViewAdapter.setNewsFeedList(NewsFeedList);
        NewsFeedListViewAdapter.setNewsFeedExample(this);
        NewsFeedListView.setAdapter(NewsFeedListViewAdapter);

        NewsFeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();

                String NewsFeed = NewsFeedList.get(position).toString();
                Bundle dataToPass = new Bundle();
                dataToPass.putString(ITEM_TITLE, NewsFeedList.get(position).getTitle());
                dataToPass.putString(ITEM_URL, NewsFeedList.get(position).getUrl());
               dataToPass.putStringArrayList(ITEM_Content, NewsFeedList.get(position).getContent());
              //  dataToPass.putString(ITEM_PRICE, bookList.get(position).getPrice());
                dataToPass.putInt(ITEM_POSITION, position);
                dataToPass.putInt(ITEM_ID, NewsFeedList.get(position).getId());

                if(isTablet)
                {
                    NewFeedDetail dFragment = new NewFeedDetail(); //add a BookDetailFrag
                    dFragment.setArguments( dataToPass ); //pass it a bundle for information
                    dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                            //.addToBackStack("AnyName") //make the back button undo the transaction
                            .commit(); //actually load the fragment.
                }
                else //isPhone
                {
                    Intent nextActivity = new Intent(NewsFeedMain.this, NewsFeedItem.class);
                    nextActivity.putExtras(dataToPass); //send data to next activity
                    startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
                }
            }
        });

        ////
        typeField = (EditText)findViewById(R.id.typeField);
        NewsFeedSharedPref = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String resevedWord = NewsFeedSharedPref.getString("reserved", "Default value");
        typeField.setText(resevedWord);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);  //show the progress bar
        NewsFeedGet.setNewsFeedExample(this);

        //NewsFeedQuery NewsFeedQuery = new NewsFeedQuery();
        //NewsFeedQuery.execute();        ////
    }

    @Override
    protected void onPause() {
        super.onPause();

        //get an editor object
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
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                int position = data.getIntExtra(ITEM_POSITION, 0);
                deleteListMessage(position);
            }
        }
    }

    public void deleteListMessage(int position)
    {
        NewsFeedData.deleteNewsFeed(NewsFeedList.get(position));
        // get all books
        NewsFeedData.getAllNewsFeeds();
        NewsFeedList.clear();
        NewsFeedList.addAll(NewsFeedData.getAllNewsFeeds());
        NewsFeedListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);


	    /* slide 15 material:
	    MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView sView = (SearchView)searchItem.getActionView();
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }  });

	    */

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
                //Show the toast immediately:
                Toast.makeText(this, "This is my final project!", Toast.LENGTH_LONG).show();

                //Show the toast immediately:
                //Toast.makeText(this, "Welcome to Menu Example", Toast.LENGTH_LONG).show();
                Snackbar snackbar = Snackbar.make(tBar, "Go Back?", Snackbar.LENGTH_LONG)
                        .setAction("GoBack", e ->{
                            Log.e("Menu Example", "Clicked GoBack");
                                       finish();
                        });
                snackbar.show();
                break;
            //what to do when the menu item is selected:
            case R.id.menu_help:


                //Show the toast immediately:
                HelpExample();

                break;
        }
        return true;
    }

 /*   public void alertExample()
    {
        View middle = getLayoutInflater().inflate(R.layout.view_extra_stuff, null);

        EditText et = (EditText)middle.findViewById(R.id.view_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The Message")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Accept
                        //message = et.getText().toString();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();
    }
**/
    public void HelpExample()
    {
        View middle = getLayoutInflater().inflate(R.layout.view_extra_stuff, null);

        EditText et = (EditText)middle.findViewById(R.id.view_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Author: Tao Shen\nVersion: 1.0")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Accept
                        //message = et.getText().toString();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        // database
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
            // get all books
            //NewsFeedList.clear();
            //NewsFeedList.addAll(NewsFeedData.getAllNewsFeeds());
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
