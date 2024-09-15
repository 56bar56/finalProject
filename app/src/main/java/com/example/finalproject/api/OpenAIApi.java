package com.example.finalproject.api;

import com.example.finalproject.items.OpenAIRequest;
import com.example.finalproject.items.OpenAIResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenAIApi {

    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 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"  // Replace with your OpenAI API key
    })
    @POST("v1/chat/completions")
    Call<OpenAIResponse> getAIResponse(@Body OpenAIRequest request);
}
