package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The main entry of 'New York Times Article Search'
 * @author George Yang
 * @version 1.0.0
 * @
 */
public class ArticleSavedActivity extends AppCompatActivity {

    private List<Article> searchArticles; // the article list returned by searching
    private List<Article> savedArticles; // the saved article list from database

    private Toolbar toolbar;
    private ProgressBar progressBar;
    private SearchView sView;
    private Button deleteButton;
    private ListView articleList;

    MyDatabaseOpenHelper dbOpener;
    SQLiteDatabase db;

    private ListAdapter adapter;

    private String toastMessage = getString(R.string.article_toast);

    public static final String ARTICLE_ID = "ID";
    public static final String ARTICLE_TITLE = "TITLE";
    public static final String ARTICLE_LINK = "LINK";
    public static final String ARTICLE_ICON = "ICON";
    public static final String ARTICLE_TEXT = "TEXT";
    public static final int ARTICLE_TEXT_ACTIVITY = 345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_saved);

        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        //get a database:
        dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();
        loadArticlesFromDB(db);

        // set the adapter for the search listView
        savedArticles = new ArrayList<>(20);
        adapter = new MyArrayAdapter<Article>(savedArticles);
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
                Intent nextActivity = new Intent(ArticleSavedActivity.this, ArticleTextActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, ARTICLE_TEXT_ACTIVITY); //make the transition
            }
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
        int iconIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ARTICLE_ICON);
        int textIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ARTICLE_TEXT);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            int id = results.getInt(idIndex);
            String title = results.getString(titleColIndex);
            String link = results.getString(linkIndex);
            String icon = results.getString(iconIndex);
            String text = results.getString(textIndex);

            //add the new article to the array list:
            savedArticles.add(new Article(id, title, link, icon, text));
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
                Intent articleIntent = new Intent(this, ArticleSavedActivity.class);
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
                root = inflater.inflate(R.layout.list_row_article_saved, parent, false);

            TextView articleTitle = (TextView)root.findViewById(R.id.articleRow);
            articleTitle.setText(((Article)getItem(position)).getTitle());

            // delete an article from the list
            deleteButton = (Button)findViewById(R.id.article_delete);
            deleteButton.setOnClickListener(v->{
                Snackbar sb = Snackbar.make((Toolbar)findViewById(R.id.main_toolbar), getString(R.string.article_delete_remind), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.confirm), e -> deleteMessageId((int)(getItemId(position))));
                sb.show();
            });

            return root;
        }

        public void deleteMessageId(int id)
        {
            Log.i("Delete this article:" , " id="+id);

            // delete the message from database
            db.delete(MyDatabaseOpenHelper.TABLE_ARTICLE, MyDatabaseOpenHelper.COL_ARTICLE_ID + "= ?", new String[] {Long.toString(id)});

            // delete the article from adapter
            for(E article: articles) {
                if(((Article)article).getId() == id) {
                    articles.remove(article);
                    break;
                }
            }

          //  adapter.notifyDataSetChanged();
        }
    }

}