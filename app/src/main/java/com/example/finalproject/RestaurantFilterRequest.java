package com.example.finalproject;

import java.util.List;

public class RestaurantFilterRequest {
    private List<String> location;
    private List<String> cuisine;
    private int averageCost;
    private float rating;

    public RestaurantFilterRequest(List<String> location, List<String> cuisine, int averageCost, float rating) {
        this.location = location;
        this.cuisine = cuisine;
        this.averageCost = averageCost;
        this.rating = rating;
    }

    // Getters and Setters
    public List<String> getLocation() { return location; }
    public void setLocation(List<String> location) { this.location = location; }

    public List<String> getCuisine() { return cuisine; }
    public void setCuisine(List<String> cuisine) { this.cuisine = cuisine; }

    public int getAverageCost() { return averageCost; }
    public void setAverageCost(int averageCost) { this.averageCost = averageCost; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
}
