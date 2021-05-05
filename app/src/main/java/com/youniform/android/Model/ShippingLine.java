package com.youniform.android.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShippingLine {
    @SerializedName("method_id")
    @Expose
    private String methodId;
    @SerializedName("method_title")
    @Expose
    private String methodTitle;
    @SerializedName("total")
    @Expose
    private String total;

    public ShippingLine(String methodId, String methodTitle, String total) {
        this.methodId = methodId;
        this.methodTitle = methodTitle;
        this.total = total;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public String getMethodTitle() {
        return methodTitle;
    }

    public void setMethodTitle(String methodTitle) {
        this.methodTitle = methodTitle;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
