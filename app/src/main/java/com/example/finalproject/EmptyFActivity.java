package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EmptyFActivity extends AppCompatActivity {  //EMPTY ACTIVITY CLASS---------------------------------------------------------------------------

    Button backButton, saveButton, delButton;
    FlightActivity.DatabaseHelper db;
    private ListView fragList;
    private FlightActivity.FlightFragListAdapter fragAdapter;
    private Flight flight;
    private List<Flight> flights = new ArrayList<>();
    private String departure;
    private String arrival;
    private String altitude;
    private String speed;
    private String status;
    private boolean isSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_flight);

        if(db == null) {

            db = new FlightActivity.DatabaseHelper(this, null, null, 1);
        }

        Bundle dataToPass = getIntent().getExtras();

        FlightFragment fFragment = new FlightFragment();
        fFragment.setArguments(dataToPass);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frag_flightFrame, fFragment)
                .addToBackStack("Flight")
                .commit();

        fragList = (ListView)findViewById(R.id.frag_listView);
        backButton = (Button)findViewById(R.id.frag_backButton);
        saveButton = (Button)findViewById(R.id.frag_buttonSave);
        delButton = (Button)findViewById(R.id.frag_buttonDel);



        backButton.setOnClickListener(new View.OnClickListener() {  //Back Button------------
            @Override
            public void onClick(View v) {

                Intent back = new Intent(EmptyFActivity.this, FlightActivity.class);
                startActivity(back);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {  //Save Button-------------
            @Override
            public void onClick(View v) {

                flight = new Flight();
                flight.setDeparture(dataToPass.getString("departure"));
                flight.setArrival(dataToPass.getString("arrival"));
                flight.setSpeed(dataToPass.getString("speed"));
                flight.setAltitude(dataToPass.getString("altitude"));
                flight.setStatus(dataToPass.getString("status"));

                flights.add(flight);
                fragList.setAdapter(fragAdapter);
                fragAdapter.notifyDataSetChanged();
            }
        });

        fragAdapter = new FlightActivity.FlightFragListAdapter(this, flights);
        fragList.setAdapter(fragAdapter);
        fragAdapter.notifyDataSetChanged();

        Log.e("before adapter", " plerae");


        delButton.setOnClickListener(new View.OnClickListener() {   //Delete Button-----------
            @Override
            public void onClick(View v) {

            }
        });
    }

//    public void flightData(Flight flight){
//
//            flights.add(flight);
//            Log.e("flightData ", "Method has been accessed");
//            Log.e("Testing flightData ", "" + flights.get(0).getSpeed());
//            Log.e("Testin flight ", " " + flight.getDeparture());
//        Log.e("Testin speed ", " " + flight.getSpeed());
//        Log.e("Testin arrival ", " " + flight.getArrival());
//    }
}