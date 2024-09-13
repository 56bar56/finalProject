package com.example.finalproject.items;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trip implements Serializable {
    private Flight selectedFlight;
    private Flight selectedReturnedFlight;
    private Hotel selectedHotel;
    private ArrayList<Restaurant> selectedRestaurants;
    private List<Attraction> selectedAttractions;
    private String username;  // Added username

    // Constructor with username and password
    public Trip(Flight selectedFlight, Flight selectedReturnedFlight, Hotel selectedHotel,
                ArrayList<Restaurant> selectedRestaurants, List<Attraction> selectedAttractions,
                String username) {
        this.selectedFlight = selectedFlight;
        this.selectedReturnedFlight = selectedReturnedFlight;
        this.selectedHotel = selectedHotel;
        this.selectedRestaurants = selectedRestaurants;
        this.selectedAttractions = selectedAttractions;
        this.username = username;
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


    public int getPriceForTrip() {
        double totalPrice = 0;
        for(Restaurant r: selectedRestaurants){
            totalPrice += r.getAverageCost();
        }
        for(Attraction a: selectedAttractions){
            totalPrice += a.getAverageCost();
        }
        int numberOfDays = 30;
        if(this.selectedReturnedFlight != null) {
            numberOfDays = (int) calculateDaysBetween(this.selectedFlight.getDeparture(), this.selectedReturnedFlight.getArrival()); //something
        }
        totalPrice += numberOfDays * this.selectedHotel.getPricePerNight();
        totalPrice += this.selectedFlight.getPrice();
        if(this.selectedReturnedFlight != null){
            totalPrice += this.selectedReturnedFlight.getPrice();
        }
        return (int)totalPrice;
    }

    // Function gets 2 dates and return the number of days in between.
    public static long calculateDaysBetween(String string1, String string2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Extract only the date part from the strings
            String datePart1 = string1.substring(0, 10);
            String datePart2 = string2.substring(0, 10);

            // Parse the date strings
            Date date1 = sdf.parse(datePart1);
            Date date2 = sdf.parse(datePart2);

            // Calculate the difference in milliseconds
            long diffInMillies = date2.getTime() - date1.getTime();

            // Convert milliseconds to days
            long daysBetween = diffInMillies / (1000 * 60 * 60 * 24);

            return daysBetween;

        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
