package com.youniform.android.Model.PlacesModels;

import androidx.annotation.Keep;

@Keep
public class Viewport {
    private Northeast northeast;

    private Southwest southwest;

    public Northeast getNortheast() {
        return this.northeast;
    }

    public void setNortheast(Northeast northeast) {
        this.northeast = northeast;
    }

    public Southwest getSouthwest() {
        return this.southwest;
    }

    public void setSouthwest(Southwest southwest) {
        this.southwest = southwest;
    }
}
