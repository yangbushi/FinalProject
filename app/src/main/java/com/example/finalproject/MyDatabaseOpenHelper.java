package com.example.finalproject;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * The main entry of the database
 * @author George Yang
 * @version 1.0.0
 * refer from the professor's week5 demo
 */
public class MyDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDatabaseFile";
    public static final int VERSION_NUM = 2;

    // Saved articles table
    public static final String TABLE_ARTICLE = "articles";
    public static final String COL_ARTICLE_ID = "_id";
    public static final String COL_ARTICLE_TITLE = "TITLE";
    public static final String COL_ARTICLE_LINK = "LINK";
    public static final String COL_ARTICLE_ICON = "ICON";
    public static final String COL_ARTICLE_TEXT = "TEXT";

    public MyDatabaseOpenHelper(Activity ctx){
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create saved articles table
        db.execSQL("CREATE TABLE " + TABLE_ARTICLE + "( "
                + COL_ARTICLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_ARTICLE_LINK + " TEXT, "
                + COL_ARTICLE_ICON + " TEXT,"
                + COL_ARTICLE_TEXT + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old saved articles table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);

        //Create new tables:
        onCreate(db);
    }
}