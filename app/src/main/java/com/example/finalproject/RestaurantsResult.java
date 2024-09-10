package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsResult extends AppCompatActivity {
    private ListView restaurantsListView;
    private List<Restaurant> restaurantList;
    private List<Restaurant> selectedRestaurants = new ArrayList<>(); // To store selected restaurants
    private Button doneChoosingButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_results);

        restaurantsListView = findViewById(R.id.restaurants_ListView);
        doneChoosingButton = findViewById(R.id.doneChoosingButton);

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
        doneChoosingButton.setOnClickListener(v -> {
            // Pass selected restaurants to another activity (e.g., RestaurantDetailsActivity)
            Intent intent = new Intent(RestaurantsResult.this, Attraction_Preferance_Activity.class);
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
                    ArrayList<String> restaurantDetails = new ArrayList<>();
                    for (Restaurant restaurant : restaurantList) {
                        restaurantDetails.add("Restaurant: " + restaurant.getName() + "\n" +
                                "Cuisine: " + restaurant.getCuisine() + "\n" +
                                "Location: " + restaurant.getLocation() + "\n" +
                                "Average Cost: $" + restaurant.getAverageCost() + "\n" +
                                "Rating: " + restaurant.getRating() + " stars");
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RestaurantsResult.this,
                            android.R.layout.simple_list_item_multiple_choice, restaurantDetails) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView textView = (TextView) view.findViewById(android.R.id.text1);
                            textView.setTextSize(16); // Increase text size
                            return view;
                        }
                    };
                    restaurantsListView.setAdapter(adapter);

                    restaurantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Restaurant selectedRestaurant = restaurantList.get(position);
                            if (restaurantsListView.isItemChecked(position)) {
                                selectedRestaurants.add(selectedRestaurant); // Add selected restaurant
                            } else {
                                selectedRestaurants.remove(selectedRestaurant); // Remove deselected restaurant
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.e("RestaurantsActivity", "Request failed: " + t.getMessage());
            }
        });
    }
}
