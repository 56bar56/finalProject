package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.api.FlightAPI;
import com.example.finalproject.items.Flight;
import com.example.finalproject.FlightFilterRequest;
import com.example.finalproject.adapters.FlightListAdapter;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightsActivity extends AppCompatActivity {
    private ImageView backButton;
    private RecyclerView flightsRecyclerView;
    private List<Flight> flightList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_outbound_page);

        // Taking care of back button
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        flightsRecyclerView = findViewById(R.id.lstFlights);
        flightsRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        // Retrieve user data from MainActivity
        String maxPrice = getIntent().getStringExtra("maxPrice");
        String departureLocation = getIntent().getStringExtra("departureLocation");
        String arrivalLocation = getIntent().getStringExtra("arrivalLocation");
        String departureDate = getIntent().getStringExtra("departureDate");
        String arrivalDate = getIntent().getStringExtra("arrivalDate");
        String days = getIntent().getStringExtra("maxDay");
        String daysMin = getIntent().getStringExtra("minDay");
        String isRoundedTrip  = getIntent().getStringExtra("ROUNDED_TRIP");
        String peopleNumber = getIntent().getStringExtra("peopleNumber");



        // Create filter request
        FlightFilterRequest filterRequest = new FlightFilterRequest(
                Double.parseDouble(maxPrice),
                departureLocation,
                arrivalLocation,
                departureDate,
                arrivalDate
        );

        // Fetch flights
        fetchFlights(filterRequest, days, daysMin, maxPrice, isRoundedTrip, peopleNumber);
    }

    private void fetchFlights(FlightFilterRequest request, String days, String daysMin, String maxPrice, String isRoundedTrip, String peopleNumber) {
        FlightAPI flightAPI = RetrofitClient.getClient("http://10.0.2.2:5000").create(FlightAPI.class);

        Call<List<Flight>> call = flightAPI.filterFlights(request);
        call.enqueue(new Callback<List<Flight>>() {
            @Override
            public void onResponse(Call<List<Flight>> call, Response<List<Flight>> response) {
                if (!response.isSuccessful()) {
                    Log.e("FlightsActivity", "Response error: " + response.code());
                    return;
                }

                flightList = response.body();
                if (flightList != null) {
                    if (flightList != null && flightList.size() > 3) {
                        sendFlightsToChatGPT(flightList, days, daysMin, maxPrice, isRoundedTrip, peopleNumber);
                    } else {
                        // Show flights directly if there are 3 or fewer flights
                        displayFlights(flightList, days, daysMin, maxPrice, isRoundedTrip, peopleNumber);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Flight>> call, Throwable t) {
                Log.e("FlightsActivity", "Request failed: " + t.getMessage());
            }
        });
    }
    private void sendFlightsToChatGPT(List<Flight> flightList, String days, String daysMin, String maxPrice, String isRoundedTrip, String peopleNumber) {
        // Assuming you have a backend endpoint to process this
        FlightAPI flightAPI = RetrofitClient.getClient("http://10.0.2.2:5000").create(FlightAPI.class);

        Call<List<Flight>> call = flightAPI.sortFlights(flightList);
        call.enqueue(new Callback<List<Flight>>() {
            @Override
            public void onResponse(Call<List<Flight>> call, Response<List<Flight>> response) {
                if (!response.isSuccessful()) {
                    Log.e("FlightsActivity", "Sorting API error: " + response.code());
                    return;
                }

                // Get sorted flights from ChatGPT response
                List<Flight> sortedFlights = response.body();
                if (sortedFlights != null) {
                    displayFlights(sortedFlights, days, daysMin, maxPrice, isRoundedTrip, peopleNumber);
                }
            }

            @Override
            public void onFailure(Call<List<Flight>> call, Throwable t) {
                Log.e("FlightsActivity", "Request failed: " + t.getMessage());
            }
        });
    }
    private void displayFlights(List<Flight> flights, String days, String daysMin, String maxPrice, String isRoundedTrip, String peopleNumber) {
        FlightListAdapter adapter = new FlightListAdapter(FlightsActivity.this, flights, days, daysMin, maxPrice, null, Boolean.parseBoolean(isRoundedTrip), true, peopleNumber);
        flightsRecyclerView.setAdapter(adapter);
    }
}
