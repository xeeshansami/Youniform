package com.youniform.android.Model.MakeBookings;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class VehicleModel implements Serializable {
    private String categoryId;
    private String currentPricePerHour;
    private String currentPriceAddHour;
    private String currentPricePerKM;
    private String peakRate;
    private String discountRate;
    private String categoryName;
    private String categoryImage;
    private String categoryDescription;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCurrentPricePerHour() {
        return currentPricePerHour;
    }

    public void setCurrentPricePerHour(String currentPricePerHour) {
        this.currentPricePerHour = currentPricePerHour;
    }

    public String getCurrentPriceAddHour() {
        return currentPriceAddHour;
    }

    public void setCurrentPriceAddHour(String currentPriceAddHour) {
        this.currentPriceAddHour = currentPriceAddHour;
    }

    public String getCurrentPricePerKM() {
        return currentPricePerKM;
    }

    public void setCurrentPricePerKM(String currentPricePerKM) {
        this.currentPricePerKM = currentPricePerKM;
    }

    public String getPeakRate() {
        return peakRate;
    }

    public void setPeakRate(String peakRate) {
        this.peakRate = peakRate;
    }

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }
}
