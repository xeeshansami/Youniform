package com.youniform.android.Model.ShiipingAddress;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetShippingAddressModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("shippingaddress")
    @Expose
    private List<Shippingaddress> shippingaddress = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Shippingaddress> getShippingaddress() {
        return shippingaddress;
    }

    public void setShippingaddress(List<Shippingaddress> shippingaddress) {
        this.shippingaddress = shippingaddress;
    }
}
