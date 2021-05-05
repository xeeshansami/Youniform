package com.youniform.android.Model.Searched;

import androidx.annotation.Keep;

@Keep
public class PlaceAutocomplete {

    public CharSequence placename;
    public CharSequence description;
    public CharSequence placeid;


    public PlaceAutocomplete(CharSequence placeid, CharSequence placename, CharSequence description) {
        this.placename = placename;
        this.description = description;
        this.placeid = placeid;
    }

    @Override
    public String toString() {
        return description.toString();
    }
}