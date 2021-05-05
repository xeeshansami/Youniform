package com.youniform.android.Model.Searched;

import androidx.annotation.Keep;

import java.util.List;

@Keep

public class RootSearched {


    private List<Predictions> predictions;

    private String status;

    public List<Predictions> getPredictions() {
        return this.predictions;
    }

    public void setPredictions(List<Predictions> predictions) {
        this.predictions = predictions;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
