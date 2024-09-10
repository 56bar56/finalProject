package com.example.finalproject;

public class HotelFilterRequest {
    private double pricePerNight;
    private String location;
    private double rating;
    private double distanceFromCenterKm;
    private String type;

    public HotelFilterRequest(double maxPrice, String location, double rating, double kmFromCity, String type) {
        this.pricePerNight = maxPrice;
        this.location = location;
        this.rating = rating;
        this.distanceFromCenterKm = kmFromCity;
        this.type = type;
    }

    public void setPricePerNight(double maxPrice) {
        this.pricePerNight = maxPrice;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setDistanceFromCenterKm(double distanceFromCenterKm) {
        this.distanceFromCenterKm = distanceFromCenterKm;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public String getLocation() {
        return location;
    }

    public double getRating() {
        return rating;
    }

    public double getDistanceFromCenterKm() {
        return distanceFromCenterKm;
    }

    public String getType() {
        return type;
    }
}
