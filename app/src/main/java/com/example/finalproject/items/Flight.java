package com.example.finalproject.items;

import java.io.Serializable;

public class Flight implements Serializable {
    private String flightNumber;
    private String departure;
    private String arrival;
    private double price;
    private String takeoff;
    private String landing;
    private String company;

    // Constructor
    public Flight(String flightNumber, String departure, String arrival, double price, String takeoff, String landing, String company) {
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.arrival = arrival;
        this.price = price;
        this.takeoff = takeoff;
        this.landing = landing;
        this.company = company;
    }

    // Getters
    public String getFlightNumber() { return flightNumber; }
    public String getDeparture() { return departure; }
    public String getArrival() { return arrival; }
    public double getPrice() { return price; }
    public String getTakeoff() { return takeoff; }
    public String getLanding() { return landing; }
    public String getCompany() { return company; }

    // Setters (optional)
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }
    public void setDeparture(String departure) { this.departure = departure; }
    public void setArrival(String arrival) { this.arrival = arrival; }
    public void setPrice(double price) { this.price = price; }
    public void setTakeoff(String takeoff) { this.takeoff = takeoff; }
    public void setLanding(String landing) { this.landing = landing; }
    public void setCompany(String company) { this.company = company; }
}
