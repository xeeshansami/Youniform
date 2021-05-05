package com.youniform.android.Interface;


import com.youniform.android.Model.Searched.PlaceAutocomplete;

import java.util.ArrayList;

public interface PlaceAutoCompleteInterface {
    void onPlaceClick(ArrayList<PlaceAutocomplete> mResultList, int position);

}
