package com.youniform.android.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoriesModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("parent")
    @Expose
    private Integer parent;
    @SerializedName("image")
    @Expose
    private Image image;


    @SerializedName("hassubcat")
    @Expose
    private Boolean hassubcat;

    public Boolean getHassubcat() {
        return hassubcat;
    }

    public void setHassubcat(Boolean hassubcat) {
        this.hassubcat = hassubcat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}
