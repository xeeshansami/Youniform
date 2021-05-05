package com.youniform.android.Model.MakeBookings;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep

public class BookingModel implements Serializable {

    private LocationModel pickUpLatLng;
    private LocationModel dropOffLatLng;

    private String pickTitle;
    private String pickDesc;

    private String dropTitle;
    private String promo_code;
    private String user_id;
    private String dropDesc;

    private VehicleModel selectedVehicle;

    private String stratTime;
    private String endTime;

    private String duration;
    private String time;
    private String distance;
    private Integer vehicle_type;
    private Integer expected_fare;
    private String date;

    private Double pLat;
    private Double pLng;

    private Double dLat;
    private Double dLng;
    private String ride_type;
    private Integer cod_amount;
    private Integer parcel_value;
    private String user_address;
    private String user_name;
    private String user_number;

    public Integer getExpected_fare() {
        return expected_fare;
    }

    public void setExpected_fare(Integer expected_fare) {
        this.expected_fare = expected_fare;
    }

    public String getPromo_code() {
        return promo_code;
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_number() {
        return user_number;
    }

    public void setUser_number(String user_number) {
        this.user_number = user_number;
    }

    public Integer getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(Integer vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public Integer getCod_amount() {
        return cod_amount;
    }

    public void setCod_amount(Integer cod_amount) {
        this.cod_amount = cod_amount;
    }

    public Integer getParcel_value() {
        return parcel_value;
    }

    public void setParcel_value(Integer parcel_value) {
        this.parcel_value = parcel_value;
    }

    public String getRide_type() {
        return ride_type;
    }

    public void setRide_type(String ride_type) {
        this.ride_type = ride_type;
    }


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public LocationModel getPickUpLatLng() {
        return pickUpLatLng;
    }

    public void setPickUpLatLng(LocationModel pickUpLatLng) {
        this.pickUpLatLng = pickUpLatLng;
    }

    public LocationModel getDropOffLatLng() {
        return dropOffLatLng;
    }

    public void setDropOffLatLng(LocationModel dropOffLatLng) {
        this.dropOffLatLng = dropOffLatLng;
    }

    public String getPickTitle() {
        return pickTitle;
    }

    public void setPickTitle(String pickTitle) {
        this.pickTitle = pickTitle;
    }

    public String getPickDesc() {
        return pickDesc;
    }

    public void setPickDesc(String pickDesc) {
        this.pickDesc = pickDesc;
    }

    public String getDropTitle() {
        return dropTitle;
    }

    public void setDropTitle(String dropTitle) {
        this.dropTitle = dropTitle;
    }

    public String getDropDesc() {
        return dropDesc;
    }

    public void setDropDesc(String dropDesc) {
        this.dropDesc = dropDesc;
    }

    public VehicleModel getSelectedVehicle() {
        return selectedVehicle;
    }

    public void setSelectedVehicle(VehicleModel selectedVehicle) {
        this.selectedVehicle = selectedVehicle;
    }

    public String getStratTime() {
        return stratTime;
    }

    public void setStratTime(String stratTime) {
        this.stratTime = stratTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getpLat() {
        return pLat;
    }

    public void setpLat(Double pLat) {
        this.pLat = pLat;
    }

    public Double getpLng() {
        return pLng;
    }

    public void setpLng(Double pLng) {
        this.pLng = pLng;
    }

    public Double getdLat() {
        return dLat;
    }

    public void setdLat(Double dLat) {
        this.dLat = dLat;
    }

    public Double getdLng() {
        return dLng;
    }

    public void setdLng(Double dLng) {
        this.dLng = dLng;
    }
}
