package com.example.finalproject;



import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;
        import android.content.ContentValues;
import android.database.Cursor;

import java.util.LinkedList;
        import java.util.List;

public class NewsFeedDatabase extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "NewsFeedDB";

    public NewsFeedDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_BOOK_TABLE = "CREATE TABLE NewsFeed ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, "+
                 "content TEXT,"+"url TEXT)";

        // create books table
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS NewsFeed");

        // create fresh books table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */

    // Books table name
    private static final String TABLE_NewsFeedS = "NewsFeed";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_content = "content";
    private static final String KEY_url = "url";

    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE,KEY_content,KEY_url};

    public void addNewsFeed(NewsFeed NewsFeed){
        Log.d("addNewsFeed", NewsFeed.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, NewsFeed.getTitle()); // get title
        values.put(KEY_content, NewsFeed.getContent().get(0));
        values.put(KEY_url, NewsFeed.getUrl()); // get author


        // 3. insert
        db.insert(TABLE_NewsFeedS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public NewsFeed getNewsFeed(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_NewsFeedS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build NewsFeed object
        NewsFeed NewsFeed = new NewsFeed();
        NewsFeed.setId(Integer.parseInt(cursor.getString(0)));
        NewsFeed.setTitle(cursor.getString(1));
        NewsFeed.addContent(cursor.getString(2));
        NewsFeed.setURL(cursor.getString(3));
        Log.d("getNewsFeed("+id+")", NewsFeed.toString());

        // 5. return NewsFeed
        return NewsFeed;
    }

    // Get All NewsFeeds
    public List<NewsFeed> getAllNewsFeeds() {
        List<NewsFeed> AllNews = new LinkedList<NewsFeed>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NewsFeedS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        NewsFeed NewsFeed = null;
        if (cursor.moveToFirst()) {
            do {
                NewsFeed = new NewsFeed();
                NewsFeed.setId(Integer.parseInt(cursor.getString(0)));
                NewsFeed.setTitle(cursor.getString(1));
                NewsFeed.addContent(cursor.getString(2));
                NewsFeed.setURL(cursor.getString(3));

          //      NewsFeed.setPrice(cursor.getString(4));

                // Add book to books
                AllNews.add(NewsFeed);
            } while (cursor.moveToNext());
        }

        //Log.d("getAllNewsFeeds()", NewsFeed.toString());

        // return books
        return AllNews;
    }

    // Updating single book
    public int updateNewsFeed(NewsFeed NewsFeed) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("KEY_TITLE", NewsFeed.getTitle()); // get title
        values.put("KEY_content", NewsFeed.getContent().get(0)); // get Content
        values.put("KEY_url", NewsFeed.getUrl()); // get url



        // 3. updating row
        int i = db.update(TABLE_NewsFeedS, //table
                values, // column/value
                KEY_url+" = ?", // selections
                new String[] { String.valueOf(NewsFeed.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single NewsFeed
    public void deleteNewsFeed(NewsFeed NewsFeed) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_NewsFeedS,
                KEY_url+" = ?",
                new String[] { String.valueOf(NewsFeed.getId()) });

        // 3. close
        db.close();

        Log.d("deleteNewsFeed", NewsFeed.toString());

    }
}