package com.example.finalproject;


public class FlightFilterRequest {
    private double maxPrice;
    private String departureLocation;
    private String arrivalLocation;
    private String departureDate;
    private String arrivalDate;

    // Constructors, getters, and setters
    public FlightFilterRequest(double maxPrice, String departureLocation, String arrivalLocation, String departureDate, String arrivalDate) {
        this.maxPrice = maxPrice;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
    }

    public double getMaxPrice() { return maxPrice; }
    public String getDepartureLocation() { return departureLocation; }
    public String getArrivalLocation() { return arrivalLocation; }
    public String getDepartureDate() { return departureDate; }
    public String getArrivalDate() { return arrivalDate; }
}
