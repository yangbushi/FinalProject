package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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

    // test

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

        sView = (SearchView)findViewById(R.id.search_article);
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

    /**
     * load saved articles from database
     * copy mostly from professor's week5
     */
    private void loadArticlesFromDB(SQLiteDatabase db) {

        //query all the results from the database:
        String [] columns = {MyDatabaseOpenHelper.COL_ARTICLE_ID, MyDatabaseOpenHelper.COL_ARTICLE_TITLE, MyDatabaseOpenHelper.COL_ARTICLE_LINK};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_ARTICLE, columns, null, null, null, null, null, null);

        //find the column indices:
        int linkIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ARTICLE_LINK);
        int titleColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ARTICLE_TITLE);
        int idIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ARTICLE_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            int id = results.getInt(idIndex);
            String title = results.getString(titleColIndex);
            String link = results.getString(linkIndex);

            //add the new article to the array list:
            savedArticles.add(new Article(id, title, link));
        }
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

                String title, link;
                int articleNumber = jsonArray.length();
                for(int i = 0; i < articleNumber; i++) { // loop the array to get article items
                    title = ((JSONObject)(jsonArray.get(i))).getJSONObject("headline").getString("main");
                    link = ((JSONObject)(jsonArray.get(i))).getString("web_url");
                    Article article = new Article(i, title, link);
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
}