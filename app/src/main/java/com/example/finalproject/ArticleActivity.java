package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The main entry of 'New York Times Article Search'
 * @author George Yang
 * @version 1.0.0
 * @
 */
public class ArticleActivity extends AppCompatActivity {

    private List<Article> searchArticles; // the article list returned by searching
    private List<Article> savedArticles; // the saved article list from database

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private SearchView sView;
    private Button searchButton;
    private ListView articleList;

    MyDatabaseOpenHelper dbOpener;
    SQLiteDatabase db;

    private ListAdapter adapter;

    private String toastMessage;

    public static final String ARTICLE_ID = "ID";
    public static final String ARTICLE_TITLE = "TITLE";
    public static final String ARTICLE_LINK = "LINK";
    public static final String ARTICLE_ICON = "ICON";
    public static final String ARTICLE_TEXT = "TEXT";
    public static final int ARTICLE_TEXT_ACTIVITY = 345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        //get a database:
        dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();
        //loadArticlesFromDB(db);

        // set the adapter for the search listView
        searchArticles = new ArrayList<>(20);
        adapter = new MyArrayAdapter<Article>(searchArticles);
        articleList = (ListView)findViewById(R.id.articleList);
        articleList.setAdapter(adapter);

        // turn to article detail fragment
        boolean isTablet = findViewById(R.id.articleTextFrag) != null;
        articleList.setOnItemClickListener((list, item, position, id) -> { // refer to prof's week8
            Bundle dataToPass = new Bundle();
            Article article = searchArticles.get(position);
            dataToPass.putString(ARTICLE_LINK, article.getLink() );
            dataToPass.putString(ARTICLE_TITLE, article.getTitle());
            dataToPass.putString(ARTICLE_ICON, article.getIconName());
            dataToPass.putString(ARTICLE_TEXT, article.getText());
            dataToPass.putLong(ARTICLE_ID, id);

            if(isTablet) // show fragment directly
            {
                ArticleTextFragment fragment = new ArticleTextFragment(); //add a DetailFragment
                fragment.setArguments( dataToPass ); //pass it a bundle for information
                fragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.articleTextFrag, fragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            }
            else //isPhone, start empty activity which then calls fragment
            {
                Intent nextActivity = new Intent(ArticleActivity.this, ArticleTextActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, ARTICLE_TEXT_ACTIVITY); //make the transition
            }
        });


        // saved query
        SharedPreferences prefs = getSharedPreferences("shared.txt", Context.MODE_PRIVATE);
        String preQuery = prefs.getString("query", "");

        // search articles by typing key words or saved query
        sView = (SearchView)findViewById(R.id.search_article);
        sView.setQuery(preQuery, false);

        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                DataFetcher networkThread = new DataFetcher();
                networkThread.execute( "http://api.nytimes.com/svc/search/v2/articlesearch.json?&api-key=S14OrOeGC2U7vSABHtPnpN6oZD5ysWud&query=" + sView.getQuery() ); //this starts doInBackground on other thread

                //messageBox = (EditText)findViewById(R.id.messageBox);
                progressBar = (ProgressBar)findViewById(R.id.article_progress_bar);
                progressBar.setVisibility(View.VISIBLE);  //show the progress bar

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchButton = (Button)findViewById(R.id.article_search_button);
        searchButton.setOnClickListener(v->{
            sView.setQuery(sView.getQuery(), true);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // save the current query into file
        SharedPreferences prefs = getSharedPreferences("shared.txt", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("query", sView.getQuery().toString());
        edit.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);

/*	    MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView sView = (SearchView)searchItem.getActionView();
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/

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
            case R.id.menu_help:
                View middle = getLayoutInflater().inflate(R.layout.dialog_article, null);
                TextView author = (TextView)middle.findViewById(R.id.article_author);
                TextView version = (TextView)middle.findViewById(R.id.article_version);
                TextView instruction = (TextView)middle.findViewById(R.id.article_instruction);

                author.setText("George Yang");
                version.setText("1.0.0");
                instruction.setText("1. input key words to search related articles from New York Times \n"
                        + "2. save articles from the search result list \n"
                        + "3. modify the saved list");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("")
                        .setPositiveButton("Positive", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).setView(middle);

                builder.create().show();
                break;

            case R.id.menu_saved:
                Intent articleSavedIntent = new Intent(this, ArticleSavedActivity.class);
                startActivity(articleSavedIntent);
                break;
        }

        return true;
    }

    /**
     * Refer from Professor's week4 branch
     * @param <E>
     */
    protected class MyArrayAdapter<E> extends BaseAdapter
    {
        private List<E> articles;

        public MyArrayAdapter(List<E> articles) { this.articles = articles; }

        public int getCount() {
            if (articles == null) return 0;
            else return articles.size();
        }

        public E getItem(int position) {
            if (articles == null) return null;
            else return articles.get(position);
        }

        public long getItemId(int position) { return position; }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            //Recycle views if possible:
            View root = old;
            //If there are no spare layouts, load a new one:
            if(old == null)
                root = inflater.inflate(R.layout.list_row_article, parent, false);

            TextView articleTitle = (TextView)root.findViewById(R.id.articleRow);
            articleTitle.setText(((Article)getItem(position)).getTitle());

            return root;
        }
    }

    /**
     * refer to professor's week7
     */
    // a subclass of AsyncTask                  Type1    Type2    Type3
    private class DataFetcher extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String ... params) {
            try {
                //get the string url:
                String myUrl = params[0];

                //create the network connection:
                URL url = new URL(myUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //create a JSON object from the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                // get the JSON array of articles:
                JSONObject jObject = new JSONObject(result);
                JSONArray jsonArray = jObject.getJSONObject("response").getJSONArray("docs");

                String title, link, iconName = "", text;
                int articleNumber = jsonArray.length();
                for(int i = 0; i < articleNumber; i++) { // loop the array to get article items
                    title = ((JSONObject)(jsonArray.get(i))).getJSONObject("headline").getString("main");
                    link = ((JSONObject)(jsonArray.get(i))).getString("web_url");
                    text = ((JSONObject)(jsonArray.get(i))).getString("snippet");
                    JSONArray multimedia = ((JSONObject)(jsonArray.get(i))).getJSONArray("multimedia");
                    if(multimedia.length() != 0) { // there is a picture
                        JSONObject firstMultimedia = (JSONObject) (multimedia.get(0));
                        String iconUrl = firstMultimedia.getString("url");
                        iconName = iconUrl.substring(iconUrl.lastIndexOf('/') + 1,
                                iconUrl.lastIndexOf('.') - 1); // get the picture name excluding path and type
                        iconName += ".png";

                        if(!fileExistance(iconName)) { // the icon file does not exist, download and save
                            Log.d("icon: ", "icon not found, download it");
                            downloadAndSaveIcon(iconUrl, iconName);
                        } else {
                            Log.d("icon: ", "icon found");
                        }
                    }

                    Article article = new Article(i, title, link, iconName, text);
                    searchArticles.add(article);

                    publishProgress((i+1)/articleNumber*100);
                }

                Thread.sleep(1000); //pause for 1000 milliseconds to watch the progress bar spin
            }catch (Exception ex)
            {
                Log.e("Crash!!", ex.getMessage() );
            }

            //return type 3, which is String:
            return "Finished task";
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        public void downloadAndSaveIcon(String iconfile, String iconName) {

            // dowload icon

            Bitmap image = null;
            URL url = null;

            try {
                url = new URL("https://www.nytimes.com/" + iconfile);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                int responseCode = 0;
                responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(connection.getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // save icon to local storage
            FileOutputStream outputStream = null;
            try {
                outputStream = openFileOutput( iconName, Context.MODE_PRIVATE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
            Log.i("AsyncTaskExample", "update:" + values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            // show the list view
            ((MyArrayAdapter) adapter).notifyDataSetChanged();

            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ARTICLE_TEXT_ACTIVITY) // return from article detail page
        {
            if(resultCode == RESULT_OK) //if you hit the save button instead of back button
            {
                long id = data.getLongExtra(ARTICLE_ID, 0);
                String title = data.getStringExtra(ARTICLE_TITLE);
                String link = data.getStringExtra(ARTICLE_LINK);
                String icon = data.getStringExtra(ARTICLE_ICON);
                String text = data.getStringExtra(ARTICLE_TEXT);

                saveArticle((int)id, title, link, icon, text);
            }
        }
    }

    /**
     * save article to saved list
     * @param id
     */
    public void saveArticle(int id, String title, String link, String icon, String text)
    {
        Log.i("save this article:" , " id="+id);

        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();
        newRowValues.put(MyDatabaseOpenHelper.COL_ARTICLE_TITLE, title);
        newRowValues.put(MyDatabaseOpenHelper.COL_ARTICLE_LINK, link);
        newRowValues.put(MyDatabaseOpenHelper.COL_ARTICLE_ICON, icon);
        newRowValues.put(MyDatabaseOpenHelper.COL_ARTICLE_TEXT, text);

        //insert in the database:
        long newId = db.insert(MyDatabaseOpenHelper.TABLE_ARTICLE, null, newRowValues);

        // add the article to adapter
        Article article = new Article((int)newId, title, link, icon, text);
        savedArticles.add(article);

        toastMessage = getString(R.string.article_toast);
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();

    }
}