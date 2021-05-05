package com.youniform.android.Model.GetProduct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Attribute implements Serializable {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("options")
    @Expose
    private List<String> options = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

}
