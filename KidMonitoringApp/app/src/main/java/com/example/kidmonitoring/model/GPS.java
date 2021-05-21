package com.example.kidmonitoring.model;

public class GPS {
    private String Email, Address;
    private double Latitude, Longitude;

    public GPS(String email, String address, double latitude, double longitude) {
        Email = email;
        Address = address;
        Latitude = latitude;
        Longitude = longitude;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
