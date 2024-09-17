package com.example.finalproject;

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

import com.example.finalproject.adapters.FlightListAdapter;
import com.example.finalproject.api.FlightAPI;
import com.example.finalproject.items.Flight;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class ReturnFlightsActivity extends AppCompatActivity {

    private TextView title;
    private ImageView backButton;
    private RecyclerView flightsRecyclerView;
    private List<Flight> flightList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_outbound_page);

        // Set title
        title = findViewById(R.id.title);
        title.setText("Outbound Flight");

        // Taking care of back button
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        flightsRecyclerView = findViewById(R.id.lstFlights);
        flightsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve selected flight and user's return trip information
        Flight selectedFlight = (Flight) getIntent().getSerializableExtra("selectedFlight");
        String returnDepartureLocation = selectedFlight.getArrival();
        String returnArrivalLocation = selectedFlight.getDeparture();
        String days = getIntent().getStringExtra("tripDays");
        String daysMin = getIntent().getStringExtra("tripDaysMin");
        String maxPrice = getIntent().getStringExtra("maxPrice");
        String peopleNumber = getIntent().getStringExtra("peopleNumber");

        String returnDate = calculateReturnDate(selectedFlight.getTakeoff(), days);  // Calculate return date by adding the user's stay duration
        String returnDateMin = calculateReturnDate(selectedFlight.getTakeoff(), daysMin);  // Calculate return date by adding the user's stay duration

        // Create filter request for return flights
        FlightFilterRequest filterRequest = new FlightFilterRequest(
                Double.parseDouble(maxPrice),  // Max price is not relevant for return flight
                returnDepartureLocation,
                returnArrivalLocation,
                returnDateMin,
                returnDate
        );

        fetchReturnFlights(filterRequest, selectedFlight, peopleNumber);
    }

    private String calculateReturnDate(String departureDate, String days) {
        // Define the date format used in the input and output
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Parse the departure date string to a Date object
            Date date = sdf.parse(departureDate);

            // Create a Calendar object and set it to the parsed date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Add the number of days to the calendar
            calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(days));

            // Get the new date and format it back to a string
            Date returnDate = calendar.getTime();
            return sdf.format(returnDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void fetchReturnFlights(FlightFilterRequest request, Flight selectedFlight, String peopleNumber) {
        FlightAPI flightAPI = RetrofitClient.getClient("http://192.168.1.41:5000").create(FlightAPI.class);

        Call<List<Flight>> call = flightAPI.filterFlights(request);
        call.enqueue(new Callback<List<Flight>>() {
            @Override
            public void onResponse(Call<List<Flight>> call, Response<List<Flight>> response) {
                if (!response.isSuccessful()) {
                    Log.e("ReturnFlightsActivity", "Response error: " + response.code());
                    return;
                }

                flightList = response.body();
                if (flightList != null) {
                    FlightListAdapter adapter = new FlightListAdapter(ReturnFlightsActivity.this, flightList, "none", "none", "none", selectedFlight, true, true, peopleNumber);
                    flightsRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Flight>> call, Throwable t) {
                Log.e("ReturnFlightsActivity", "Request failed: " + t.getMessage());
            }
        });
    }
}
