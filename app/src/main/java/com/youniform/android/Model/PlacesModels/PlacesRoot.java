package com.youniform.android.Model.PlacesModels;

import androidx.annotation.Keep;

@Keep
public class PlacesRoot {

    private ResultPlaces result;

    private String status;

    public ResultPlaces getResult() {
        return this.result;
    }

    public void setResult(ResultPlaces result) {
        this.result = result;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
