package com.example.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 'Flight status tracker'
 * @author Dustin Horricks
 * @version 1.0.0
 */

public class FlightActivity extends AppCompatActivity {

    private String arrivalAPI;
    //private String temp = "http://torunski.ca/flights.json";
    private String departAPI; //= "http://aviation-edge.com/v2/public/flights?key=e66fe0-74b486&depIata=YOW";
    private ListView fListView;
    private ProgressBar progressBar;
    private FlightListAdapter adapter;
    private Button checkButton;
    private EditText flightText;
    private TextView textView;
    List<Flight> flights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        fListView = (ListView)findViewById(R.id.flight_View);
        checkButton = (Button)findViewById(R.id.flight_textButton);
        flightText = (EditText)findViewById(R.id.flight_text);
        textView = (TextView)findViewById(R.id.flight_view);

        flights = new ArrayList<>();

        //FlightAPI netWorkThread = new FlightAPI();

//        adapter = new FlightListAdapter(this, flights);
//        adapter.notifyDataSetChanged();

         checkButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 try {
                     departAPI = "http://aviation-edge.com/v2/public/flights?key=e09fd1-a516a8&depIata=" + flightText.getText().toString();
                 }catch(Exception e){

                     Toast.makeText(getApplicationContext(), "Wrong Code!", Toast.LENGTH_SHORT).show();
                 }

                 if(flightText.getText().toString().matches("")) {
                     Toast.makeText(getApplicationContext(), "Please enter code", Toast.LENGTH_SHORT).show();
                 } else {
                     FlightAPI netWorkThread = new FlightAPI();
                         netWorkThread.execute(departAPI);

                         //shows the user/console the current Flight Code
                     textView.setText("Current Code: " + flightText.getText().toString());
                     Log.e("Flight text info ", "" + flightText.getText().toString());

                     flights.clear();
                 }
             }
         });

            adapter = new FlightListAdapter(this, flights);
            fListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu_bar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_article:
                Intent articleIntent = new Intent(this, ArticleActivity.class);
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
        }

        return true;
    }

    public void addFlight(Flight newFlight){

        flights.add(newFlight);
    }

    public class FlightAPI extends AsyncTask<String, Integer, String> {

        String speed, status, departure, arrival;
        String altitude = "Unknown";
        Flight flight;
        FlightActivity activity = new FlightActivity();

        @Override
        protected String doInBackground(String... strings) {

            String myUrl = strings[0];

            try {

                //this creates the connection to the network
                URL url = new URL(myUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();

                //create JSON obj from the response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {

                    sb.append(line + "\n");
                }
                String result = sb.toString();

                //now to create a JSON table:
                Log.e("Results ", "" + result);
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    JSONObject tempDep = jsonObject.getJSONObject("departure");
                    departure = "Departure: " + tempDep.getString("iataCode");

                    JSONObject tempSpeed = jsonObject.getJSONObject("speed");
                    speed = "Speed: " + tempSpeed.getString("horizontal");

                    JSONObject tempAlt = jsonObject.getJSONObject("geography");
                    altitude = "Altitude: " + tempAlt.getString("altitude");

                    status = "Status: " + jsonObject.optString("status");

                    flight = new Flight(i, departure, speed, altitude, status);
                    addFlight(flight);



                    Log.e("Departure is ", "" + departure);
                    Log.e("Speed is ", "" + speed);
                    Log.e("Altitude is ", "" + altitude);
                    Log.e("Status is ", "" + status);
                    Log.i("---------------", "--------------------");
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "Finished..";
        }
    }
}