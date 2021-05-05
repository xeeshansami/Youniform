package com.youniform.android.Model.Filter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FilterModel {

    @SerializedName("attribute")
    @Expose
    private List<Attribute> attribute = null;

    public List<Attribute> getAttribute() {
        return attribute;
    }

    public void setAttribute(List<Attribute> attribute) {
        this.attribute = attribute;
    }
}
