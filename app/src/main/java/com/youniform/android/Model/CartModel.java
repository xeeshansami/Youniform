package com.youniform.android.Model;

import java.io.Serializable;

public class CartModel implements Serializable {
    int _id, Producdid, Quantity, CatId;
    String Name, Price, Image, Color, Size;

 /*   public CartModel(int _id, int producdid, int quantity, String name, String price, String image) {
        this._id = _id;
        Producdid = producdid;
        Quantity = quantity;
        Name = name;
        Price = price;
        Image = image;
    }*/


    public CartModel(int _id, int producdid, int catId, int quantity, String name, String price, String image, String color, String size) {
        this._id = _id;
        Producdid = producdid;
        CatId = catId;
        Quantity = quantity;
        Name = name;
        Price = price;
        Image = image;
        Color = color;
        Size = size;
    }

    public int getCatId() {
        return CatId;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getProducdid() {
        return Producdid;
    }

    public void setProducdid(int producdid) {
        Producdid = producdid;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }
}
