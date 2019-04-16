package com.example.finalproject;

import android.content.Intent;
import android.database.Cursor;
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

                db.insertFlight(dataToPass.getString("departure"),
                        dataToPass.getString("arrival"),
                        dataToPass.getString("speed"),
                        dataToPass.getString("altitude"),
                        dataToPass.getString("status"));

                flights.add(flight);
                fragList.setAdapter(fragAdapter);
                fragAdapter.notifyDataSetChanged();
            }
        });

        db.open();
        Cursor c = db.getFlights();

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

//            Flight dataFlight = new Flight();
//
//            dataFlight.setDeparture(c.getString(c.getColumnIndex(db.KEY_ROW_DEP)));
//            dataFlight.setArrival(c.getString(c.getColumnIndex(db.KEY_ROW_ARR)));
//            dataFlight.setSpeed(c.getString(c.getColumnIndex(db.KEY_ROW_SPEED)));
//            dataFlight.setAltitude(c.getString(c.getColumnIndex(db.KEY_ROW_ALT)));
//            dataFlight.setStatus(c.getString(c.getColumnIndex(db.KEY_ROW_STATUS)));
//
//            flights.add(dataFlight);

            flights.add(new Flight(c.getString(c.getColumnIndex(db.KEY_ROW_DEP)),
                    c.getString(c.getColumnIndex(db.KEY_ROW_ARR)),
                    c.getString(c.getColumnIndex(db.KEY_ROW_SPEED)),
                    c.getString(c.getColumnIndex(db.KEY_ROW_ALT)),
                    c.getString(c.getColumnIndex(db.KEY_ROW_STATUS))));
        }
        db.close();

        fragAdapter = new FlightActivity.FlightFragListAdapter(this, flights);
        fragList.setAdapter(fragAdapter);
        fragAdapter.notifyDataSetChanged();

        delButton.setOnClickListener(new View.OnClickListener() {   //Delete Button-----------
            @Override
            public void onClick(View v) {

            }
        });
    }
}