package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.Flight;
import com.example.finalproject.FlightAPI;
import com.example.finalproject.FlightFilterRequest;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightsActivity extends AppCompatActivity {

    private ListView flightsListView;
    private List<Flight> flightList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights);

        flightsListView = findViewById(R.id.flightsListView);

        // Retrieve user data from MainActivity
        String maxPrice = getIntent().getStringExtra("maxPrice");
        String departureLocation = getIntent().getStringExtra("departureLocation");
        String arrivalLocation = getIntent().getStringExtra("arrivalLocation");
        String departureDate = getIntent().getStringExtra("departureDate");
        String arrivalDate = getIntent().getStringExtra("arrivalDate");
        String days = getIntent().getStringExtra("maxDay");
        String daysMin = getIntent().getStringExtra("minDay");


        // Create filter request
        FlightFilterRequest filterRequest = new FlightFilterRequest(
                Double.parseDouble(maxPrice),
                departureLocation,
                arrivalLocation,
                departureDate,
                arrivalDate
        );

        // Fetch flights
        fetchFlights(filterRequest, days, daysMin, maxPrice);
    }

    private void fetchFlights(FlightFilterRequest request, String days, String daysMin, String maxPrice) {
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
                    ArrayList<String> flightDetails = new ArrayList<>();
                    for (Flight flight : flightList) {
                        flightDetails.add("Flight: " + flight.getFlightNumber() + "\n" +
                                "Departure: " + flight.getDeparture() + "\n" +
                                "Arrival: " + flight.getArrival() + "\n" +
                                "Price: $" + flight.getPrice() + "\n" +
                                "Company: " + flight.getCompany());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(FlightsActivity.this,
                            android.R.layout.simple_list_item_1, flightDetails);
                    flightsListView.setAdapter(adapter);

                    flightsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // Pass selected flight to ReturnFlightsActivity
                            Flight selectedFlight = flightList.get(position);
                            Intent intent = new Intent(FlightsActivity.this, ReturnFlightsActivity.class);
                            intent.putExtra("selectedFlight", selectedFlight);
                            intent.putExtra("tripDays", days);
                            intent.putExtra("tripDaysMin", daysMin);
                            intent.putExtra("maxPrice", maxPrice);

                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Flight>> call, Throwable t) {
                Log.e("FlightsActivity", "Request failed: " + t.getMessage());
            }
        });
    }
}
