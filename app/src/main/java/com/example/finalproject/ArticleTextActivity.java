package com.example.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * The temporary activity to render the detail article fragment
 * @author George Yang
 * @version 1.0.0
 */
public class ArticleTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_text);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from ChatRoomActivity

        ArticleTextFragment fragment = new ArticleTextFragment();
        fragment.setArguments( dataToPass ); //pass data to the the fragment
        fragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.articleTextFrag, fragment)
                .addToBackStack("AnyName")
                .commit();
    }
}
