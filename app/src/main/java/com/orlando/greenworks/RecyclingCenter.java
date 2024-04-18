package com.orlando.greenworks;

import com.google.android.gms.maps.model.LatLng;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

public class RecyclingCenter {
    private String name;
    private String address;
    private String hours;
    private String phone;
    private double latitude;
    private double longitude;

    private LatLng latLng;

    public RecyclingCenter(String name, String address, String hours, String phone) {
        this.name = name;
        this.address = address;
        this.hours = hours;
        this.phone = phone;
    }

    public RecyclingCenter(String name, String address, String hours, String phone, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.hours = hours;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }
}
