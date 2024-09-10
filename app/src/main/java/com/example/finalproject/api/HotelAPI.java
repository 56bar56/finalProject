package com.example.finalproject.api;

import com.example.finalproject.HotelFilterRequest;
import com.example.finalproject.items.Hotel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HotelAPI {
    @POST("/api/Hotels/filter")
    Call<List<Hotel>> filterHotels(@Body HotelFilterRequest filterRequest);
}