package com.example.finalproject.api;

import com.example.finalproject.items.Trip;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TripAPI {
    // Endpoint to create a new trip
    @POST("/api/trips/createTrip")
    Call<Trip> createTrip(@Body Trip trip);
}
