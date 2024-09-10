package com.example.finalproject;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RestaurantAPI {
    @POST("/api/Restaurants/filter")
    Call<List<Restaurant>> getFilteredRestaurants(@Body RestaurantFilterRequest request);
}
