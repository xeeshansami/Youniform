package com.youniform.android.Model.PlacesModels;

import androidx.annotation.Keep;

@Keep
public class Geometry {
    private Location location;

    private Viewport viewport;

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Viewport getViewport() {
        return this.viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }
}
