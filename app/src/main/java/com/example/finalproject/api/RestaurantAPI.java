package com.example.finalproject.api;

import com.example.finalproject.RestaurantFilterRequest;
import com.example.finalproject.items.Restaurant;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestaurantAPI {
    @POST("/api/Restaurants/filter")
    Call<List<Restaurant>> getFilteredRestaurants(@Body RestaurantFilterRequest request);
}
