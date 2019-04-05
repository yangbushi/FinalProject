package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Flight ListView Adapter
 * @author Dustin Horricks
 * @version 1.0.0
 */

public class FlightListAdapter extends ArrayAdapter<Flight> {

    private Context fContext;
    private List<Flight> flightList;

    public FlightListAdapter(Context context, List<Flight> list) {
        super(context, 0, list);

        fContext = context;
        flightList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View listItem = convertView;

        listItem = LayoutInflater.from(fContext).inflate(R.layout.list_row_flight, parent, false);

        Flight currentFlight = getItem(position);

        TextView fDeparture = (TextView)listItem.findViewById(R.id.flightRowDeparture);
        fDeparture.setText(flightList.get(position).getDeparture());

        TextView fSpeed = (TextView)listItem.findViewById(R.id.flightRowSpeed);
        fSpeed.setText(flightList.get(position).getSpeed());

        TextView fAltitude = (TextView)listItem.findViewById(R.id.flightRowAltitude);
        fAltitude.setText(flightList.get(position).getAltitude());

        TextView fStatus = (TextView)listItem.findViewById(R.id.flightRowStatus);
        fStatus.setText(flightList.get(position).getStatus());

        return listItem;
    }
}
