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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotelResultsActivity extends AppCompatActivity {

    private ListView hotelsListView;
    private List<Hotel> hotelList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_results);

        hotelsListView = findViewById(R.id.hotelsListView);

        // Retrieve user data from HotelPreferenceActivity
        String maxPriceForNight = getIntent().getStringExtra("maxPriceForNight");
        String location = getIntent().getStringExtra("location");
        String rating = getIntent().getStringExtra("rating");
        String kmFromCity = getIntent().getStringExtra("kmFromCity");
        String hotelOrCabbin = getIntent().getStringExtra("hotelOrCabbin");
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
        fetchHotels(filterRequest, selectedFlight, selectedReturnedFlight);
    }

    private void fetchHotels(HotelFilterRequest request, Flight selectedFlight,Flight selectedReturnedFlight) {
        HotelAPI hotelAPI = RetrofitClient.getClient("http://10.0.2.2:5000").create(HotelAPI.class);

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
                    ArrayList<String> hotelDetails = new ArrayList<>();
                    for (Hotel hotel : hotelList) {
                        hotelDetails.add("Hotel: " + hotel.getName() + "\n" +
                                "Location: " + hotel.getLocation() + "\n" +
                                "Price Per Night: $" + hotel.getPricePerNight() + "\n" +
                                "Rating: " + hotel.getRating() + " stars\n" +
                                "Distance from City: " + hotel.getDistanceFromCenterKm() + " km\n" +
                                "Type: " + hotel.getType());
                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<>(HotelResultsActivity.this,
                            android.R.layout.simple_list_item_1, hotelDetails);
                    hotelsListView.setAdapter(adapter);

                    hotelsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // Pass selected hotel to another activity (e.g., HotelDetailsActivity)
                            Hotel selectedHotel = hotelList.get(position);
                            Intent intent = new Intent(HotelResultsActivity.this, Restaurant_Preferance_Activity.class);
                            intent.putExtra("selectedHotel", selectedHotel);

                            intent.putExtra("selectedFlight", selectedFlight);
                            intent.putExtra("selectedReturnedFlight", selectedReturnedFlight);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable t) {
                Log.e("HotelsActivity", "Request failed: " + t.getMessage());
            }
        });
    }
}
