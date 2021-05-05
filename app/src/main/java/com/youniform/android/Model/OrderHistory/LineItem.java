package com.youniform.android.Model.OrderHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LineItem {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("variation_id")
    @Expose
    private Integer variationId;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("tax_class")
    @Expose
    private String taxClass;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("subtotal_tax")
    @Expose
    private String subtotalTax;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("total_tax")
    @Expose
    private String totalTax;
    @SerializedName("price")
    @Expose
    private Integer price;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getVariationId() {
        return variationId;
    }

    public void setVariationId(Integer variationId) {
        this.variationId = variationId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getTaxClass() {
        return taxClass;
    }

    public void setTaxClass(String taxClass) {
        this.taxClass = taxClass;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getSubtotalTax() {
        return subtotalTax;
    }

    public void setSubtotalTax(String subtotalTax) {
        this.subtotalTax = subtotalTax;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(String totalTax) {
        this.totalTax = totalTax;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
