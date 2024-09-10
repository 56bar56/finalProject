package com.example.finalproject;


import java.io.Serializable;

public class Hotel implements Serializable {
    private String name;
    private String location;
    private double pricePerNight;
    private double rating;
    private double distanceFromCenterKm;
    private String type; // Either "Hotel" or "Cabbin"

    public Hotel(String name, String location, double pricePerNight, double rating, double kmFromCity, String type) {
        this.name = name;
        this.location = location;
        this.pricePerNight = pricePerNight;
        this.rating = rating;
        this.distanceFromCenterKm = kmFromCity;
        this.type = type;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public double getDistanceFromCenterKm() { return distanceFromCenterKm; }
    public void setDistanceFromCenterKm(double distanceFromCenterKm) { this.distanceFromCenterKm = distanceFromCenterKm; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
