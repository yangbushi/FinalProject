package com.example.finalproject;

/**
 * Flight object class
 * @author Dustin Horricks
 * @version 1.0.0
 */

public class Flight {

    private int id;
    private String info;
    private String flight_number;

    public Flight(){

        id = 0;
        info = null;
        flight_number = null;
    }

    public Flight(int id, String info, String flight_number){

        this.id = id;
        this.info = info;
        this.flight_number = flight_number;
    }

    public void setId(int id){

        this.id = id;
    }

    public int getId(){

        return id;
    }

    public void setInfo(String info){

        this.info = info;
    }

    public String getInfo(){

        return info;
    }

    public void setFlight_number(String flight_number){

        this.flight_number = flight_number;
    }

    public String getFlight_number(){

        return flight_number;
    }
}
