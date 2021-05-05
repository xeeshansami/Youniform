package com.youniform.android.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LineItem {

    @SerializedName("product_id")
    @Expose
    private Integer productId;

    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    @SerializedName("variation_id")
    @Expose
    private Integer variationId;

    public LineItem(Integer productId, Integer quantity, Integer variationId) {
        this.productId = productId;
        this.quantity = quantity;
        this.variationId = variationId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getVariationId() {
        return variationId;
    }

    public void setVariationId(Integer variationId) {
        this.variationId = variationId;
    }
}
