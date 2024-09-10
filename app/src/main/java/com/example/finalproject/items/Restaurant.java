package com.example.finalproject.items;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private String name;
    private String location;
    private String cuisine;
    private int averageCost;
    private float rating;

    public Restaurant(String name, String location, String cuisine, int averageCost, float rating) {
        this.name = name;
        this.location = location;
        this.cuisine = cuisine;
        this.averageCost = averageCost;
        this.rating = rating;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public int getAverageCost() { return averageCost; }
    public void setAverageCost(int averageCost) { this.averageCost = averageCost; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
}

