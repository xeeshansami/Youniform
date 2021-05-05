package com.youniform.android.Model.OrderHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.youniform.android.Model.ProfileModel.Shipping;
import com.youniform.android.Model.UserModel.Billing;

import java.util.List;

public class GetOrderHistory {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("discount_total")
    @Expose
    private String discountTotal;
    @SerializedName("shipping_total")
    @Expose
    private String shippingTotal;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("billing")
    @Expose
    private Billing billing;
    @SerializedName("shipping")
    @Expose
    private Shipping shipping;
    @SerializedName("payment_method_title")
    @Expose
    private String paymentMethodTitle;
    @SerializedName("line_items")
    @Expose
    private List<LineItem> lineItems = null;
    @SerializedName("currency_symbol")
    @Expose
    private String currencySymbol;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(String discountTotal) {
        this.discountTotal = discountTotal;
    }

    public String getShippingTotal() {
        return shippingTotal;
    }

    public void setShippingTotal(String shippingTotal) {
        this.shippingTotal = shippingTotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public String getPaymentMethodTitle() {
        return paymentMethodTitle;
    }

    public void setPaymentMethodTitle(String paymentMethodTitle) {
        this.paymentMethodTitle = paymentMethodTitle;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}
