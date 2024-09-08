package com.example.finalproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FlightAPI {
    @GET("/api/Flights")
    Call<List<Flight>> getFlights();
    @POST("/api/Flights/filter")
    Call<List<Flight>> filterFlights(@Body FlightFilterRequest request);
}