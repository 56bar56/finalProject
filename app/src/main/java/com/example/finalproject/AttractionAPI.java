package com.example.finalproject;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AttractionAPI {
    @POST("/api/Attractions/filter")
    Call<List<Attraction>> getFilteredAttractions(@Body AttractionFilterRequest request);
}
