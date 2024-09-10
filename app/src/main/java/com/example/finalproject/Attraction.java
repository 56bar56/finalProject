package com.example.finalproject;

import java.io.Serializable;

public class Attraction implements Serializable {
    private String name;
    private String location;
    private String attraction;
    private double averageCost;
    private float rating;
    private String kidFriendly;

    public Attraction(String name, String location, String type, double averageCost, float rating, String kidFriendly) {
        this.name = name;
        this.location = location;
        this.attraction = type;
        this.averageCost = averageCost;
        this.rating = rating;
        this.kidFriendly = kidFriendly;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getAttraction() { return attraction; }
    public void setAttraction(String attraction) { this.attraction = attraction; }

    public double getAverageCost() { return averageCost; }
    public void setAverageCost(double averageCost) { this.averageCost = averageCost; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public String getKidFriendly() { return kidFriendly; }
    public void setKidFriendly(String kidFriendly) { this.kidFriendly = kidFriendly; }
}
