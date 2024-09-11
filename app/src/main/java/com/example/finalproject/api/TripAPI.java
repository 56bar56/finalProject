package com.example.finalproject.api;

import com.example.finalproject.items.Trip;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TripAPI {
    // Endpoint to create a new trip
    @POST("/api/trips/createTrip")
    Call<Trip> createTrip(@Body Trip trip);
    @GET("/api/trips/getUserTrips")
    Call<List<Trip>> getUserTrips(@Query("username") String username, @Query("password") String password);
}
