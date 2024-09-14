package com.example.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.RetrofitClient;
import com.example.finalproject.adapters.HotelListAdapter;
import com.example.finalproject.adapters.TripListAdapter;
import com.example.finalproject.api.TripAPI;
import com.example.finalproject.items.Hotel;
import com.example.finalproject.items.Trip;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTripsActivity extends AppCompatActivity {
    private ImageView backButton;
    private RecyclerView tripsRecyclerView;
    private TripListAdapter adapter;
    private List<Trip> tripList;
    private EditText searchEditText;
    private ImageView searchIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_trips_page);

        // Taking care of back button
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        tripsRecyclerView = findViewById(R.id.list_trips);
        tripsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Send request to the server to get user's trips
        getUserTrips();

        // Taking care of the searching bar
        searchEditText = findViewById(R.id.search_message);
        searchIcon = findViewById(R.id.search_icon);

        // Set up search icon click listener
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchEditText.getText().toString();
                adapter.filter(query);  // Call the filter method
            }
        });
    }

    private void getUserTrips() {
        String username = globalVars.username;

        TripAPI tripAPI = RetrofitClient.getClient("http://10.0.2.2:5000").create(TripAPI.class);
        Call<List<Trip>> call = tripAPI.getUserTrips(username);

        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MyTripsActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                tripList = response.body();
                if (tripList != null) {
                    adapter = new TripListAdapter(MyTripsActivity.this, tripList);
                    tripsRecyclerView.setAdapter(adapter);
                } else {
                    // Handle the case when tripList is null or empty
                    tripList = new ArrayList<>();
                    adapter = new TripListAdapter(MyTripsActivity.this, tripList);
                    tripsRecyclerView.setAdapter(adapter);
                    Toast.makeText(MyTripsActivity.this, "No trips found", Toast.LENGTH_SHORT).show();
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
