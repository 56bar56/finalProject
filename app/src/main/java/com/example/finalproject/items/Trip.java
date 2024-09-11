package com.example.finalproject.items;

import java.util.ArrayList;
import java.util.List;

public class Trip {
    private Flight selectedFlight;
    private Flight selectedReturnedFlight;
    private Hotel selectedHotel;
    private ArrayList<Restaurant> selectedRestaurants;
    private List<Attraction> selectedAttractions;
    private String username;  // Added username
    private String password;  // Added password

    // Constructor with username and password
    public Trip(Flight selectedFlight, Flight selectedReturnedFlight, Hotel selectedHotel,
                ArrayList<Restaurant> selectedRestaurants, List<Attraction> selectedAttractions,
                String username, String password) {
        this.selectedFlight = selectedFlight;
        this.selectedReturnedFlight = selectedReturnedFlight;
        this.selectedHotel = selectedHotel;
        this.selectedRestaurants = selectedRestaurants;
        this.selectedAttractions = selectedAttractions;
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public Flight getSelectedFlight() {
        return selectedFlight;
    }

    public void setSelectedFlight(Flight selectedFlight) {
        this.selectedFlight = selectedFlight;
    }

    public Flight getSelectedReturnedFlight() {
        return selectedReturnedFlight;
    }

    public void setSelectedReturnedFlight(Flight selectedReturnedFlight) {
        this.selectedReturnedFlight = selectedReturnedFlight;
    }

    public Hotel getSelectedHotel() {
        return selectedHotel;
    }

    public void setSelectedHotel(Hotel selectedHotel) {
        this.selectedHotel = selectedHotel;
    }

    public ArrayList<Restaurant> getSelectedRestaurants() {
        return selectedRestaurants;
    }

    public void setSelectedRestaurants(ArrayList<Restaurant> selectedRestaurants) {
        this.selectedRestaurants = selectedRestaurants;
    }

    public List<Attraction> getSelectedAttractions() {
        return selectedAttractions;
    }

    public void setSelectedAttractions(ArrayList<Attraction> selectedAttractions) {
        this.selectedAttractions = selectedAttractions;
    }

    // Getters and setters for username and password
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
