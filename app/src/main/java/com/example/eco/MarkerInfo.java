package com.example.eco;

import com.google.android.gms.maps.model.LatLng;

public class MarkerInfo {
    private Double lat;
    private Double lon;
    private String user;
    private int rating;
    private int type;


    public MarkerInfo(Double lat, Double lon, String user, int rating, int type) {
        this.lat = lat;
        this.lon = lon;
        this.user = user;
        this.rating = rating;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MarkerInfo(){

    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
