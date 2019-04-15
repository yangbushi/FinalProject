package com.example.finalproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FlightFragment extends Fragment {

    private Bundle flightData;
    private long id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        flightData = getArguments();

        //Layout inflater
        View result = inflater.inflate(R.layout.frag_flight, container, false);

        //all the flight data to the textviews using the bundle
        TextView info = (TextView)result.findViewById(R.id.frag_info);
        info.setText("Current flight code: " + flightData.getString("info"));

        TextView departure = (TextView)result.findViewById(R.id.frag_departure);
        departure.setText(flightData.getString("departure"));

        TextView arrival = (TextView)result.findViewById(R.id.frag_arrival);
        arrival.setText(flightData.getString("arrival"));

        TextView speed = (TextView)result.findViewById(R.id.frag_speed);
        speed.setText(flightData.getString("speed"));

        TextView altitude = (TextView)result.findViewById(R.id.frag_altitude);
        altitude.setText(flightData.getString("altitude"));

        TextView status = (TextView)result.findViewById(R.id.frag_status);
        status.setText(flightData.getString("status"));

        return result;
    }
}
