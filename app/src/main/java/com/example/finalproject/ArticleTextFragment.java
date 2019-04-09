package com.example.finalproject;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * the fragment rendered after click an article title from the search list.
 */
public class ArticleTextFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long id;
    private String title;
    private String link;
    private String text;

    public void setTablet(boolean tablet) { isTablet = tablet; }

    public ArticleTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_article_text, container, false);

        // get data from the host activity
        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(ArticleActivity.ARTICLE_ID );
        title = dataFromActivity.getString(ArticleActivity.ARTICLE_TITLE);
        link = dataFromActivity.getString(ArticleActivity.ARTICLE_LINK);

        //show the title
        TextView titleView = (TextView)result.findViewById(R.id.articleTitle);
        titleView.setText(title);

        //show the link:
        TextView linkView = (TextView)result.findViewById(R.id.articleLink);
        linkView.setText(link);

        //show the text:
        TextView textView = (TextView)result.findViewById(R.id.articleText);
        textView.setText(text);

        // get the delete button, and add a click listener:
        Button saveButton = (Button)result.findViewById(R.id.articleSaveButton);
        saveButton.setOnClickListener( clk -> {

            if(isTablet) { //both the list and details are on the screen:
                ArticleActivity parent = (ArticleActivity)getActivity();
                parent.saveArticle((int)id, title, link); //this saves the article in the saved list

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

                parent.setResult(Activity.RESULT_OK, backToArticleList); //send data back to ArticleActivity in onActivityResult()
                parent.finish(); //go back
            }
        });

        return result;
    }

}
