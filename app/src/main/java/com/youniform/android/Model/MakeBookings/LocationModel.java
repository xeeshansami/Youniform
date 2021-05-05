package com.youniform.android.Model.MakeBookings;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class LocationModel implements Serializable {


    private String address;
    private String placeName;
    private double lat;
    private double lng;
    private double latitude;
    private double longitude;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String knownName;

    public LocationModel() {
    }

    public LocationModel(double latitude, double longitude) {
        this.lat = latitude;
        this.lat = longitude;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getKnownName() {
        return knownName;
    }

    public void setKnownName(String knownName) {
        this.knownName = knownName;
    }
}
