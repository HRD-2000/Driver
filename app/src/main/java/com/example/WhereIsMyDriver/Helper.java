package com.example.WhereIsMyDriver;

public class Helper {

    private double Longitude;
    private double Latitude ;

    public Helper(double longitude, double latitude) {
        Longitude = longitude;
        Latitude = latitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

}