package com.youniform.android.Model;

public class WishListModel {
    int id;
    String Product;

    public WishListModel(int id, String product) {
        this.id = id;
        Product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }
}
