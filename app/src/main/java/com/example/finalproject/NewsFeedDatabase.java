package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.LinkedList;
import java.util.List;

     /**
     *realize database design
     */
public class NewsFeedDatabase extends SQLiteOpenHelper {
    /**
     * define Database Version
     */
    private static final int DATABASE_VERSION = 2;
    /**
     * define Database Name
     */
    private static final String DATABASE_NAME = "NewsFeedDB";

         public NewsFeedDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create th NewsFeed table
        String CREATE_NEWS_TABLE = "CREATE TABLE NewsFeed ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, "+
                 "content TEXT,"+"url TEXT)";
        db.execSQL(CREATE_NEWS_TABLE);
    }

    @Override
    /**
     *  Drop older NewsFeedS table and fresh
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older NewsFeedS table
        db.execSQL("DROP TABLE IF EXISTS NewsFeed");
        this.onCreate(db);
    }

    /**
     * define table name
     */
    private static final String TABLE_NewsFeedS = "NewsFeed";
    /**
     * define Columns-names of the NewsFeedS Table
     */

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_content = "content";
    private static final String KEY_url = "url";

    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE,KEY_content,KEY_url};

    public void addNewsFeed(NewsFeed NewsFeed){
        Log.d("addNewsFeed", NewsFeed.toString());
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, NewsFeed.getTitle());
        values.put(KEY_url, NewsFeed.getUrl());
        db.insert(TABLE_NewsFeedS,
                null,
                values);
        db.close();
    }

    public NewsFeed getNewsFeed(int id){

        //get reference to database
        SQLiteDatabase db = this.getReadableDatabase();

        // query
        Cursor cursor =
                db.query(TABLE_NewsFeedS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        //get the first record
        if (cursor != null)
            cursor.moveToFirst();

        NewsFeed NewsFeed = new NewsFeed();
        NewsFeed.setId(Integer.parseInt(cursor.getString(0)));
        NewsFeed.setTitle(cursor.getString(1));
        NewsFeed.addContent(cursor.getString(2));
        NewsFeed.setURL(cursor.getString(3));
        Log.d("getNewsFeed("+id+")", NewsFeed.toString());
        return NewsFeed;
    }

    // Get All NewsFeeds
    public List<NewsFeed> getAllNewsFeeds() {
        List<NewsFeed> AllNews = new LinkedList<NewsFeed>();
/**
 * ger the query
 */
        String query = "SELECT  * FROM " + TABLE_NewsFeedS;

        //get reference to DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //go over each row, build NewsFeed and add it to list
        NewsFeed NewsFeed = null;
        if (cursor.moveToFirst()) {
            do {
                NewsFeed = new NewsFeed();
                NewsFeed.setId(Integer.parseInt(cursor.getString(0)));
                NewsFeed.setTitle(cursor.getString(1));
                NewsFeed.addContent(cursor.getString(2));
                NewsFeed.setURL(cursor.getString(3));

                // Add NewsFeed to NewsFeedS
                AllNews.add(NewsFeed);
            } while (cursor.moveToNext());
        }

        return AllNews;
    }

    // Updating single NewsFeed
    public int updateNewsFeed(NewsFeed NewsFeed) {

        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("KEY_TITLE", NewsFeed.getTitle()); // get title
        values.put("KEY_content", NewsFeed.getContent().get(0)); // get Content
        values.put("KEY_url", NewsFeed.getUrl()); // get url

        //  updating row
        int i = db.update(TABLE_NewsFeedS,
                values, // column/value
                KEY_url+" = ?",
                new String[] { String.valueOf(NewsFeed.getId()) });
        db.close();
        return i;

    }

    // Deleting single NewsFeed
    public void deleteNewsFeed(NewsFeed NewsFeed) {

        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        //delete
        db.delete(TABLE_NewsFeedS,
                KEY_url+" = ?",
                new String[] { String.valueOf(NewsFeed.getId()) });
        db.close();
        Log.d("deleteNewsFeed", NewsFeed.toString());

    }
}