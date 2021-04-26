package com.example.WhereIsMyDriver;

public class Helper {

    private double Longitude;
    private double Latitude ;
    private float bearing;
    private float bearingAccuracy;
    private float accuracy;

    public Helper(double latitude, double longitude, float Bearing, float BearingAccuracy, float Accuracy) {
        Latitude = latitude;
        Longitude = longitude;
        bearing = Bearing;
        bearingAccuracy = BearingAccuracy;
        accuracy = Accuracy;

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

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public float getBearingAccuracy() {
        return bearingAccuracy;
    }

    public void setBearingAccuracy(float bearingAccuracy) {
        this.bearingAccuracy = bearingAccuracy;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }
}