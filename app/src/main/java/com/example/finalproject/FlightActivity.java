package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    public static final int REQUEST_CODE = 6;
    private String API;
    private ListView fListView;
    ProgressBar progressBar;
    private FlightListAdapter adapter;
    private Button checkButton;
    private EditText flightText;
    private TextView textView;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Flight flight;
    private List<Flight> flights;
    private Bundle dataToPass;
    private Toolbar toolbar;
    EmptyFActivity emptyFActivity = new EmptyFActivity();
    private String departure, arrival, speed, altitude, status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        fListView = (ListView)findViewById(R.id.flight_View);
        checkButton = (Button)findViewById(R.id.flight_textButton);
        flightText = (EditText)findViewById(R.id.flight_text);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        progressBar = (ProgressBar)findViewById(R.id.flight_progressbar);

        flights = new ArrayList<>();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio_dep:
                        API = "http://aviation-edge.com/v2/public/flights?key=8e0a99-d48b74&depIata=" + flightText.getText().toString().toUpperCase();
                        break;

                    case R.id.radio_arr:
                        API = "http://aviation-edge.com/v2/public/flights?key=8e0a99-d48b74&arrIata=" + flightText.getText().toString().toUpperCase();
                        break;
                }
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flightText.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Please enter code", Toast.LENGTH_SHORT).show();
                } else if(radioGroup.getCheckedRadioButtonId() == -1) {

                    Toast.makeText(getApplicationContext(), "Please select Departure or Arrival", Toast.LENGTH_LONG).show();
                }else {

                    FlightAPI netWorkThread = new FlightAPI();
                    netWorkThread.execute(API);

                    //shows the console the current Flight Code
                    Log.e("Flight text info ", "" + flightText.getText().toString());

                    flights.clear();

                    //this closes the textbox once the user pressed the CHECK button
                    flightText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    fListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged(); // update adapter with changed data
                }
            }
        });

        //create adapter and send it the flights data
        adapter = new FlightListAdapter(this, flights);
        fListView.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // update adapter with changed data

        fListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dataToPass = new Bundle();

                //all the data we are passing to our fragment
                dataToPass.putString("info", flightText.getText().toString());
                dataToPass.putString("departure", flights.get(position).getDeparture());
                dataToPass.putString("arrival", flights.get(position).getArrival());
                dataToPass.putString("speed", flights.get(position).getSpeed());
                dataToPass.putString("altitude", flights.get(position).getAltitude());
                dataToPass.putString("status", flights.get(position).getStatus());

                flight = new Flight(departure, arrival, speed, altitude, status);

                //Jump to the fragment
                Intent nextActivity = new Intent(FlightActivity.this, EmptyFActivity.class); //create and empty class??---------------------------
                nextActivity.putExtras(dataToPass); //sending the data to the fragment
                startActivityForResult(nextActivity, REQUEST_CODE); //used this to return data back from the fragment to here using request code
            }
        });

        //need to create the OnActivityResult() to request data ex: delete from database------------------------------------------------------
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
            case R.id.menu_saved:
                Intent listIntent = new Intent(this, EmptyFActivity.class);
                startActivity(listIntent);
                break;
            case R.id.menu_help:
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
        private FlightActivity activity = new FlightActivity();

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

                double temp = jsonArray.length();

                for (int i = 1; i <= jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i - 1);

                    JSONObject tempDep = jsonObject.getJSONObject("departure");
                    departure = "Departure: " + tempDep.getString("iataCode");

                    JSONObject tempArr = jsonObject.getJSONObject("arrival");
                    arrival = "Arrival: " + tempArr.getString("iataCode");

                    JSONObject tempSpeed = jsonObject.getJSONObject("speed");
                    speed = "Speed: " + tempSpeed.getString("horizontal");

                    JSONObject tempAlt = jsonObject.getJSONObject("geography");
                    altitude = "Altitude: " + tempAlt.getString("altitude");

                    status = "Status: " + jsonObject.optString("status");

                    flight = new Flight(i - 1, departure, arrival, speed, altitude, status);
                    addFlight(flight);

                    Log.e("Departure is ", "" + departure);
                    Log.e("Arrival is ", "" + arrival);
                    Log.e("Speed is ", "" + speed);
                    Log.e("Altitude is ", "" + altitude);
                    Log.e("Status is ", "" + status);
                    Log.i("---------------", "--------------------");

                    publishProgress((int) ((i / temp) * 100));
                    Thread.sleep(100);
                }

                Thread.sleep(300);
                publishProgress(100);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



            return "Finished.";
        }

        @Override
        protected void onProgressUpdate(Integer ...values){

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s){

            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

        }
    }



    public static class DatabaseHelper extends SQLiteOpenHelper {  //DATABASE CLASS---------------------------------------------------------------------------------------

        public static final int VERSION_NUMBER = 1;
        public static final String DATABASE_NAME = "Flights.db";
        public static final String DATABASE_TABLE = "flights_table";
        public static final String KEY_ROW_ID = "_id";
        public static final String KEY_ROW_DEP = "departure";
        public static final String KEY_ROW_ARR = "arrival";
        public static final String KEY_ROW_SPEED = "speed";
        public static final String KEY_ROW_ALT = "altitude";
        public static final String KEY_ROW_STATUS = "status";
        SQLiteDatabase db;

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, DATABASE_NAME, null, VERSION_NUMBER);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String query = "CREATE TABLE "
                    + DATABASE_TABLE + "("
                    + KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_ROW_DEP + " TEXT, "
                    + KEY_ROW_ARR + " TEXT, "
                    + KEY_ROW_SPEED + " TEXT, "
                    + KEY_ROW_ALT + " TEXT, "
                    + KEY_ROW_STATUS + " TEXT"
                    + ");";

            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }

        public DatabaseHelper open(){

            db = getWritableDatabase();
            return this;
        }

        public void close(){

            db.close();
        }

        public long insertFlight(String departure, String arrival, String speed, String altitude, String status){

            open();
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_ROW_DEP, departure);
            Log.e("departure insertFlight ", "" + departure);
            initialValues.put(KEY_ROW_ARR, arrival);
            Log.e("Arrvial insertFlight ", "" + arrival);
            initialValues.put(KEY_ROW_SPEED, speed);
            Log.e("speed insertFlight ", "" + speed);
            initialValues.put(KEY_ROW_ALT, altitude);
            Log.e("alitude insertFlight ", "" + altitude);
            initialValues.put(KEY_ROW_STATUS, status);
            Log.e("status insertFlight ", "" + status);

            return db.insert(DATABASE_TABLE, null, initialValues);
        }

        public Cursor getFlights(){

            return db.query(DATABASE_TABLE, new String[] {KEY_ROW_ID, KEY_ROW_DEP, KEY_ROW_ARR, KEY_ROW_SPEED, KEY_ROW_ALT, KEY_ROW_STATUS},
                    null, null, null, null, null);
        }

        public void deleteID(int id){

            db.delete(DATABASE_TABLE, KEY_ROW_ID + " = " + id, null);
        }


    }

    public static class FlightFragListAdapter extends ArrayAdapter<Flight> { // FlightListFrag-------------------------------------------------------------------------------

        private Context fContext;
        private List<Flight> flightList;

        public FlightFragListAdapter(Context context, List<Flight> list) {
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

            TextView fArrival = (TextView)listItem.findViewById(R.id.flightRowArrival);
            fArrival.setText(flightList.get(position).getArrival());

            TextView fSpeed = (TextView)listItem.findViewById(R.id.flightRowSpeed);
            fSpeed.setText(flightList.get(position).getSpeed());

            TextView fAltitude = (TextView)listItem.findViewById(R.id.flightRowAltitude);
            fAltitude.setText(flightList.get(position).getAltitude());

            TextView fStatus = (TextView)listItem.findViewById(R.id.flightRowStatus);
            fStatus.setText(flightList.get(position).getStatus());

            return listItem;
        }

    }
}