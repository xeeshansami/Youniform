package com.youniform.android.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.youniform.android.Model.ProfileModel.Shipping;
import com.youniform.android.Model.UserModel.Billing;

import java.util.List;

public class PlaceOrder {

    @SerializedName("customerid")
    @Expose
    private int customerid;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("payment_method_title")
    @Expose
    private String paymentMethodTitle;
    @SerializedName("set_paid")
    @Expose
    private Boolean setPaid;
    @SerializedName("billing")
    @Expose
    private Billing billing;
    @SerializedName("shipping")
    @Expose
    private Shipping shipping;
    @SerializedName("line_items")
    @Expose
    private List<LineItem> lineItems = null;
    @SerializedName("shipping_lines")
    @Expose
    private List<ShippingLine> shippingLines = null;

    public PlaceOrder(int customerid, String paymentMethod, String paymentMethodTitle, Boolean setPaid, Billing billing, Shipping shipping, List<LineItem> lineItems, List<ShippingLine> shippingLines) {
        this.customerid = customerid;
        this.paymentMethod = paymentMethod;
        this.paymentMethodTitle = paymentMethodTitle;
        this.setPaid = setPaid;
        this.billing = billing;
        this.shipping = shipping;
        this.lineItems = lineItems;
        this.shippingLines = shippingLines;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethodTitle() {
        return paymentMethodTitle;
    }

    public void setPaymentMethodTitle(String paymentMethodTitle) {
        this.paymentMethodTitle = paymentMethodTitle;
    }

    public Boolean getSetPaid() {
        return setPaid;
    }

    public void setSetPaid(Boolean setPaid) {
        this.setPaid = setPaid;
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

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public List<ShippingLine> getShippingLines() {
        return shippingLines;
    }

    public void setShippingLines(List<ShippingLine> shippingLines) {
        this.shippingLines = shippingLines;
    }
}
