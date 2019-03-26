package com.example.finalproject;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.design.widget.Snackbar;


public class NewsFeed extends AppCompatActivity {

    private Toolbar tBar;
    private String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        tBar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(tBar);
        message = "This is the initial message";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);


	    /* slide 15 material:
	    MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView sView = (SearchView)searchItem.getActionView();
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }  });

	    */

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.menu_dic:
                //Show the toast immediately:
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                break;
            case R.id.menu_news:
                //Show the toast immediately:
                //Toast.makeText(this, "Welcome to Menu Example", Toast.LENGTH_LONG).show();
                alertExample();
                break;
            case R.id.menu_flight:
                //Show the toast immediately:
                //Toast.makeText(this, "Welcome to Menu Example", Toast.LENGTH_LONG).show();
                Snackbar sb = Snackbar.make(tBar, "Go Back?", Snackbar.LENGTH_LONG)
                        .setAction("GoBack", e ->{
                            Log.e("Menu Example", "Clicked Undo");
                            finish();
                        });
                sb.show();

                break;
            case R.id.menu_article:
                //Show the toast immediately:
                Toast.makeText(this, "You clicked on the overflow menu", Toast.LENGTH_LONG).show();

                break;
        }
        return true;
    }
    public void alertExample()
    {
        View middle = getLayoutInflater().inflate(R.layout.news_feed_dialog, null);

        EditText et = (EditText)middle.findViewById(R.id.view_edit_text);
        //btn.setOnClickListener( clk -> {et.setText("You clicked my button!");});

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The Message")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Accept
                        message = et.getText().toString();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();
    }
}
