package com.example.finalproject;

import java.util.List;

public class AttractionFilterRequest {
    private List<String> location;
    private List<String> attraction;
    private int averageCost;
    private float rating;
    private String kidFriendly;

    public AttractionFilterRequest(List<String> location, List<String> type, int averageCost, float rating, String kidFriendly) {
        this.location = location;
        this.attraction = type;
        this.averageCost = averageCost;
        this.rating = rating;
        this.kidFriendly = kidFriendly;
    }

    // Getters and Setters
    public List<String> getLocation() { return location; }
    public void setLocation(List<String> location) { this.location = location; }

    public List<String> getAttraction() { return attraction; }
    public void setAttraction(List<String> type) { this.attraction = type; }

    public int getAverageCost() { return averageCost; }
    public void setAverageCost(int averageCost) { this.averageCost = averageCost; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public String getKidFriendly() { return kidFriendly; }
    public void setKidFriendly(String kidFriendly) { this.kidFriendly = kidFriendly; }
}

