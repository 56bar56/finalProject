package com.example.finalproject.api;

import com.example.finalproject.items.GeoNamesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


import java.util.ArrayList;
import java.util.List;

// Define your Retrofit interface
public interface GeoNamesApi {
    @GET("searchJSON")
    Call<GeoNamesResponse> getCities(
            @Query("name_startsWith") String prefix,
            @Query("country") String countryCode,
            @Query("featureClass") String featureClass,
            @Query("maxRows") int maxRows,
            @Query("username") String username
    );
}