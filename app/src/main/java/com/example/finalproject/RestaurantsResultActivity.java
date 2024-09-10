package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.adapters.HotelListAdapter;
import com.example.finalproject.adapters.RestaurantListAdapter;
import com.example.finalproject.api.RestaurantAPI;
import com.example.finalproject.items.Flight;
import com.example.finalproject.items.Hotel;
import com.example.finalproject.items.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsResultActivity extends AppCompatActivity {
    private ImageView backButton;
    private RecyclerView restaurantRecyclerView;
    private List<Restaurant> restaurantList;
    private List<Restaurant> selectedRestaurants = new ArrayList<>(); // To store selected restaurants
    private RestaurantListAdapter adapter;
    private Button nextButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_results_page);

        // Taking care of back button
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        restaurantRecyclerView = findViewById(R.id.list);
        restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        nextButton = findViewById(R.id.next_button);

        // Retrieve user data from RestaurantPreferencesActivity
        String averageCost = getIntent().getStringExtra("averageCost");
        String location = getIntent().getStringExtra("location");
        String rating = getIntent().getStringExtra("rating");
        String cuisine = getIntent().getStringExtra("cuisine");
        Flight selectedFlight = (Flight) getIntent().getSerializableExtra("selectedFlight");
        Flight selectedReturnedFlight = (Flight) getIntent().getSerializableExtra("selectedReturnedFlight");
        Hotel selectedHotel = (Hotel) getIntent().getSerializableExtra("selectedHotel");

        // Parse multiple cuisines from comma-separated string
        List<String> cuisineList = Arrays.asList(cuisine.split(",\\s*"));
        List<String> locationList = Arrays.asList(location.split(",\\s*"));


        // Create filter request
        RestaurantFilterRequest filterRequest = new RestaurantFilterRequest(
                locationList,
                cuisineList,  // Send list of cuisines
                Integer.parseInt(averageCost),
                Float.parseFloat(rating)
        );

        // Fetch restaurants
        fetchRestaurants(filterRequest, selectedFlight, selectedReturnedFlight, selectedHotel);

        // Set "Done Choosing" button click listener
        nextButton.setOnClickListener(v -> {
            // Pass selected restaurants to another activity (e.g., RestaurantDetailsActivity)
            selectedRestaurants = adapter.getSelectedRestaurants();
            Intent intent = new Intent(RestaurantsResultActivity.this, Attraction_Preferance_Activity.class);
            intent.putExtra("selectedRestaurants", new ArrayList<>(selectedRestaurants));
            intent.putExtra("selectedHotel", selectedHotel);
            intent.putExtra("selectedFlight", selectedFlight);
            intent.putExtra("selectedReturnedFlight", selectedReturnedFlight);
            startActivity(intent);
        });
    }

    private void fetchRestaurants(RestaurantFilterRequest request, Flight selectedFlight, Flight selectedReturnedFlight, Hotel selectedHotel) {
        RestaurantAPI restaurantAPI = RetrofitClient.getClient("http://10.0.2.2:5000").create(RestaurantAPI.class);

        Call<List<Restaurant>> call = restaurantAPI.getFilteredRestaurants(request);
        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                if (!response.isSuccessful()) {
                    Log.e("RestaurantsActivity", "Response error: " + response.code());
                    return;
                }

                restaurantList = response.body();
                if (restaurantList != null) {
                    adapter = new RestaurantListAdapter(RestaurantsResultActivity.this, restaurantList);
                    restaurantRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.e("RestaurantsActivity", "Request failed: " + t.getMessage());
            }
        });
    }
}
