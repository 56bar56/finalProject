package com.example.finalproject;
import com.example.finalproject.api.OpenAIApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.openai.com/")  // Base URL for OpenAI API
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static OpenAIApi getOpenAIApi() {
        return getRetrofit().create(OpenAIApi.class);
    }
}
