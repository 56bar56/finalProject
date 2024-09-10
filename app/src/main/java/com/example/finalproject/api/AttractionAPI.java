package com.example.finalproject.api;

import com.example.finalproject.AttractionFilterRequest;
import com.example.finalproject.items.Attraction;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AttractionAPI {
    @POST("/api/Attractions/filter")
    Call<List<Attraction>> getFilteredAttractions(@Body AttractionFilterRequest request);
}
