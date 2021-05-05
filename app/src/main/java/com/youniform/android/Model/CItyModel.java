package com.youniform.android.Model;

public class CItyModel {

    String City, Provience;

    public CItyModel() {
    }

    public CItyModel(String city, String provience) {
        City = city;
        Provience = provience;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getProvience() {
        return Provience;
    }

    public void setProvience(String provience) {
        Provience = provience;
    }

    @Override
    public String toString() {
        return "CItyModel{" +
                "City='" + City + '\'' +
                ", Provience='" + Provience + '\'' +
                '}';
    }
}
