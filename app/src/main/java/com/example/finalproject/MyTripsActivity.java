package com.example.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.RetrofitClient;
import com.example.finalproject.api.TripAPI;
import com.example.finalproject.items.Trip;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTripsActivity extends AppCompatActivity {
    private ListView tripsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_trips_page);

        tripsListView = findViewById(R.id.tripsListView);

        // Send request to the server to get user's trips
        getUserTrips();
    }

    private void getUserTrips() {
        String username = globalVars.username;
        String password = globalVars.password;

        TripAPI tripAPI = RetrofitClient.getClient("http://10.0.2.2:5000").create(TripAPI.class);
        Call<List<Trip>> call = tripAPI.getUserTrips(username, password);

        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyTripsActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Trip> trips = response.body();
                if (trips != null) {
                    List<String> tripDetails = new ArrayList<>();
                    for (Trip trip : trips) {
                        tripDetails.add("Flight: " + trip.getSelectedFlight().getFlightNumber() + "\n" +
                                "Hotel: " + trip.getSelectedHotel().getName() + "\n" +
                                "Restaurants: " + trip.getSelectedRestaurants().size() + " selected\n" +
                                "Attractions: " + trip.getSelectedAttractions().size() + " selected");
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MyTripsActivity.this,
                            android.R.layout.simple_list_item_1, tripDetails);
                    tripsListView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                Log.e("MyTripsActivity", "Failed to fetch trips: " + t.getMessage());
                Toast.makeText(MyTripsActivity.this, "Request failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
