package com.youniform.android.Model.PlacesModels;

import androidx.annotation.Keep;

@Keep
public class Northeast {
    private double lat;

    private double lng;

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return this.lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
