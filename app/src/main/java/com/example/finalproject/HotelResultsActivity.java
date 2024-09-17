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

import com.example.finalproject.adapters.FlightListAdapter;
import com.example.finalproject.adapters.HotelListAdapter;
import com.example.finalproject.api.HotelAPI;
import com.example.finalproject.items.Flight;
import com.example.finalproject.items.Hotel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelResultsActivity extends AppCompatActivity {

    private TextView title;
    private ImageView backButton;
    private TextView instructions;
    private RecyclerView hotelsRecyclerView;
    private List<Hotel> hotelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_outbound_page);

        // Set title
        title = findViewById(R.id.title);
        title.setText("Accommodations:");

        // Taking care of back button
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        hotelsRecyclerView = findViewById(R.id.lstFlights);
        hotelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve user data from HotelPreferenceActivity
        String maxPriceForNight = getIntent().getStringExtra("maxPriceForNight");
        String location = getIntent().getStringExtra("location");
        String rating = getIntent().getStringExtra("rating");
        String kmFromCity = getIntent().getStringExtra("kmFromCity");
        String hotelOrCabbin = getIntent().getStringExtra("hotelOrCabbin");
        String peopleNumber = getIntent().getStringExtra("peopleNumber");

        Flight selectedFlight = (Flight) getIntent().getSerializableExtra("selectedFlight");
        Flight selectedReturnedFlight = (Flight) getIntent().getSerializableExtra("selectedReturnedFlight");


        // Create filter request
        HotelFilterRequest filterRequest = new HotelFilterRequest(
                Double.parseDouble(maxPriceForNight),
                location,
                Double.parseDouble(rating),
                Double.parseDouble(kmFromCity),
                hotelOrCabbin
        );

        // Fetch hotels
        fetchHotels(filterRequest, selectedFlight, selectedReturnedFlight, peopleNumber);

        // Taking care of instructions text
        instructions = findViewById(R.id.instructions);
        if (hotelList == null && hotelList.isEmpty()){
            instructions.setText("No hotels where found, please go back and change your preferences");
        } else {
            instructions.setText("Please pick a hotel");
        }
    }

    private void fetchHotels(HotelFilterRequest request, Flight selectedFlight, Flight selectedReturnedFlight, String peopleNumber) {
        HotelAPI hotelAPI = RetrofitClient.getClient("http://192.168.1.41:5000").create(HotelAPI.class);

        Call<List<Hotel>> call = hotelAPI.filterHotels(request);
        call.enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                if (!response.isSuccessful()) {
                    Log.e("HotelsActivity", "Response error: " + response.code());
                    return;
                }

                hotelList = response.body();
                if (hotelList != null) {
                    HotelListAdapter adapter = new HotelListAdapter(HotelResultsActivity.this, peopleNumber, hotelList, selectedFlight, selectedReturnedFlight, true);
                    hotelsRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable t) {
                Log.e("HotelsActivity", "Request failed: " + t.getMessage());
            }
        });
    }
}
