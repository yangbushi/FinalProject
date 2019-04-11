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

public class NewFeedDetail extends Fragment {


    private boolean isTablet;
    private Bundle dataFromActivity;
    private int position;

    public void setTablet(boolean tablet) { isTablet = tablet; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        position = dataFromActivity.getInt(NewsFeedMain.ITEM_POSITION );

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.news_feed_dialog, container, false);

        //show the message
        TextView messageTitle = (TextView)result.findViewById(R.id.message_title);
        messageTitle.setText("Title : " + dataFromActivity.getString(NewsFeedMain.ITEM_TITLE));

        TextView messageUrl = (TextView)result.findViewById(R.id.message_url);
        messageUrl.setText("Url : " + dataFromActivity.getStringArrayList(NewsFeedMain.ITEM_URL));

        TextView messageContent = (TextView)result.findViewById(R.id.message_content);
        messageContent.setText("Content : " + dataFromActivity.getStringArrayList(NewsFeedMain.ITEM_Content));

        // get the delete button, and add a click listener:
        Button deleteButton = (Button)result.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener( clk -> {

            if(isTablet) { //both the list and details are on the screen:
                NewsFeedMain parent = (NewsFeedMain)getActivity();
                parent.deleteListMessage(position); //this deletes the item and updates the list


                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                NewsFeedItem parent = (NewsFeedItem) getActivity();
                Intent backToFragmentExample = new Intent();

                backToFragmentExample.putExtra(NewsFeedMain.ITEM_POSITION,
                        dataFromActivity.getInt(NewsFeedMain.ITEM_POSITION ));

                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });
        return result;
    }

}
