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

    /**
     * define the output content of the fragment
     */
    /**define a boolean parameter to judge if the device is a tablet
     *
     */
    private boolean isTablet;
    /**
     * define a bundle
     */
    private Bundle dataFromActivity;
    /**
     * define a position
     */
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

        TextView messageContent = (TextView)result.findViewById(R.id.message_content);
        messageContent.setText("Content : " + dataFromActivity.get(NewsFeedMain.ITEM_Content));

        // define a save button and add a click listener:
        Button saveButton = (Button)result.findViewById(R.id.saveButton);
        saveButton.setOnClickListener( clk -> {
            // for a tablet
            if(isTablet) {
                NewsFeedMain parent = (NewsFeedMain)getActivity();
                parent.saveListMessage(position);
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            //for Phone:
            else
            {
                NewsFeedItem parent = (NewsFeedItem) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra(NewsFeedMain.ITEM_POSITION,
                        dataFromActivity.getInt(NewsFeedMain.ITEM_POSITION ));
                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish();
            }
        });
        return result;
    }

}
