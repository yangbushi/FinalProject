package com.example.finalproject;


import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;




/**
 * @author George Yang
 * @version 1.0.0
 * the fragment rendered after click an article title from the search / saved list.
 */
public class ArticleTextFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long id;
    private String title;
    private String link;
    private String text;
    private String icon;
    private TextView titleView;
    private TextView linkView;
    private ImageView iconView;
    private TextView textView;

    public void setTablet(boolean tablet) { isTablet = tablet; }

    public ArticleTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bitmap articleIcon;

        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_article_text, container, false);

        // get data from the host activity
        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(ArticleActivity.ARTICLE_ID );
        title = dataFromActivity.getString(ArticleActivity.ARTICLE_TITLE);
        link = dataFromActivity.getString(ArticleActivity.ARTICLE_LINK);
        icon = dataFromActivity.getString(ArticleActivity.ARTICLE_ICON);
        text = dataFromActivity.getString(ArticleActivity.ARTICLE_TEXT);

        //show the title
        titleView = (TextView)result.findViewById(R.id.articleTitle);
        titleView.setText(title);

        //show the link:
        linkView = (TextView)result.findViewById(R.id.articleLink);
        linkView.setText(link);

        //show the icon:
        if(!"".equals(icon)) {
            iconView = (ImageView) result.findViewById(R.id.articleIcon);
            iconView.setVisibility(View.VISIBLE);
            FileInputStream fis = null;
            try {
                fis = getContext().openFileInput(icon);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            articleIcon = BitmapFactory.decodeStream(fis);
            iconView.setImageBitmap(articleIcon);
        }

        //show the text:
        textView = (TextView)result.findViewById(R.id.articleText);
        textView.setText(text);

        // get the delete button, and add a click listener:
        String type = dataFromActivity.getString(ArticleActivity.ARTICLE_LIST_TYPE);
        if(type.equals("search")) {
            Button saveButton = (Button)result.findViewById(R.id.articleSaveButton);
            saveButton.setVisibility(View.VISIBLE);

            saveButton.setOnClickListener( clk -> {

                if(isTablet) { //both the list and details are on the screen:
                    ArticleActivity parent = (ArticleActivity)getActivity();
                    parent.saveArticle((int)id, title, link, icon, text); //this saves the article in the saved list

                    //now remove the fragment
                    // this is the object to be removed, so remove(this):
                    parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
                }
                //for Phone:
                else //You are only looking at the details, you need to go back to the previous list page
                {
                    ArticleTextActivity parent = (ArticleTextActivity) getActivity();
                    Intent backToArticleList = new Intent();
                    backToArticleList.putExtra(ArticleActivity.ARTICLE_ID, id);
                    backToArticleList.putExtra(ArticleActivity.ARTICLE_TITLE, title);
                    backToArticleList.putExtra(ArticleActivity.ARTICLE_LINK, link);
                    backToArticleList.putExtra(ArticleActivity.ARTICLE_ICON, icon);
                    backToArticleList.putExtra(ArticleActivity.ARTICLE_TEXT, text);

                    parent.setResult(Activity.RESULT_OK, backToArticleList); //send data back to ArticleActivity in onActivityResult()
                    parent.finish(); //go back
                }
            });
        }

        return result;
    }

}
