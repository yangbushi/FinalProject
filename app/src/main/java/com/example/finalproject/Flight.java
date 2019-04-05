package com.example.finalproject;

/**
 * Flight object class
 * @author Dustin Horricks
 * @version 1.0.0
 */

public class Flight {

    private int id;
    private String departure;
    private String arrival;
    private String speed;
    private String altitude;
    private String status;

    //this is for errors
    private String error;

    public Flight(){

        id = -1;
        speed = null;
        altitude = null;
        status = null;
        error = null;
    }

    public Flight(String departure){

        this.departure = departure;
    }

    public Flight(int id, String departure, String speed, String altitude, String status){

        this.id = id;
        this.departure = departure;
        this.speed = speed;
        this.altitude = altitude;
        this.status = status;
    }

    public void setId(int id){

        this.id = id;
    }

    public int getId(){

        return id;
    }

    public void setSpeed(String speed){

        this.speed = speed;
    }

    public String getSpeed(){

        return speed;
    }

    public void setAltitude(String altitude){

        this.altitude = altitude;
    }

    public String getAltitude(){

        return altitude;
    }

    public String getStatus(){

        return status;
    }

    public void setStatus(String status){

        this.status = status;
    }

    public String getDeparture(){

        return departure;
    }

    public void setDeparture(String departure){

        this.departure = departure;
    }

    public String getError(){

        return error;
    }

    public void setError(String error){

        this.error = error;
    }
}
