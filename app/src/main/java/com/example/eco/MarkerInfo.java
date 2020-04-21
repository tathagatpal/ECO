package com.example.eco;

import com.google.android.gms.maps.model.LatLng;

public class MarkerInfo {
    private LatLng location;
    private String user;
    private int rating;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MarkerInfo(){

    }
    public MarkerInfo(LatLng loc, String user, int rating){
        this.location = loc;
        this.user = user;
        this.rating = rating;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
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
