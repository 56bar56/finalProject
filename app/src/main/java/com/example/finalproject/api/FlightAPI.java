package com.example.finalproject.api;

import com.example.finalproject.FlightFilterRequest;
import com.example.finalproject.items.Flight;

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