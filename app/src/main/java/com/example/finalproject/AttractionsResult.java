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

public class AttractionsResult extends AppCompatActivity {
    private ListView attractionsListView;
    private List<Attraction> attractionList;
    private List<Attraction> selectedAttractions = new ArrayList<>(); // To store selected attractions
    private Button doneChoosingButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attractions_result);

        attractionsListView = findViewById(R.id.attractionsListView);
        doneChoosingButton = findViewById(R.id.doneChoosingButton);

        // Retrieve user data from AttractionPreferencesActivity
        String averageCost = getIntent().getStringExtra("maxPriceForNight");
        String location = getIntent().getStringExtra("location");
        String rating = getIntent().getStringExtra("rating");
        String attractionType = getIntent().getStringExtra("attractions");
        String kidFriendly = getIntent().getStringExtra("kidFriendly");
        Flight selectedFlight = (Flight) getIntent().getSerializableExtra("selectedFlight");
        Flight selectedReturnedFlight = (Flight) getIntent().getSerializableExtra("selectedReturnedFlight");
        Hotel selectedHotel = (Hotel) getIntent().getSerializableExtra("selectedHotel");
        Restaurant returnedRestaurants = (Restaurant) getIntent().getSerializableExtra("selectedRestaurants");


        // Parse multiple attraction types from comma-separated string
        List<String> attractionTypeList = Arrays.asList(attractionType.split(",\\s*"));
        List<String> locationList = Arrays.asList(location.split(",\\s*"));

        // Create filter request
        AttractionFilterRequest filterRequest = new AttractionFilterRequest(
                locationList,
                attractionTypeList,  // Send list of attraction types
                Integer.parseInt(averageCost),
                Float.parseFloat(rating),
                kidFriendly
        );

        // Fetch attractions
        fetchAttractions(filterRequest, selectedFlight, selectedReturnedFlight, selectedHotel, returnedRestaurants);

        // Set "Done Choosing" button click listener
        doneChoosingButton.setOnClickListener(v -> {
            // Pass selected attractions to another activity (e.g., SummaryActivity)
            Intent intent = new Intent(AttractionsResult.this, MyTripsActivity.class);
            intent.putExtra("selectedAttractions", new ArrayList<>(selectedAttractions));
            intent.putExtra("selectedHotel", selectedHotel);
            intent.putExtra("selectedFlight", selectedFlight);
            intent.putExtra("selectedReturnedFlight", selectedReturnedFlight);
            intent.putExtra("selectedRestaurants", returnedRestaurants);
            startActivity(intent);
        });
    }

    private void fetchAttractions(AttractionFilterRequest request, Flight selectedFlight, Flight selectedReturnedFlight, Hotel selectedHotel, Restaurant returnedRestaurants) {
        AttractionAPI attractionAPI = RetrofitClient.getClient("http://10.0.2.2:5000").create(AttractionAPI.class);

        Call<List<Attraction>> call = attractionAPI.getFilteredAttractions(request);
        call.enqueue(new Callback<List<Attraction>>() {
            @Override
            public void onResponse(Call<List<Attraction>> call, Response<List<Attraction>> response) {
                if (!response.isSuccessful()) {
                    Log.e("AttractionsActivity", "Response error: " + response.code());
                    return;
                }

                attractionList = response.body();
                if (attractionList != null) {
                    ArrayList<String> attractionDetails = new ArrayList<>();
                    for (Attraction attraction : attractionList) {
                        attractionDetails.add(
                                "Attraction : " + attraction.getAttraction() + "\n" +
                                        "Location: " + attraction.getLocation() + "\n" +
                                        "Average Cost: $" + attraction.getAverageCost() + "\n" +
                                        "Rating: " + attraction.getRating() + " stars");
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AttractionsResult.this,
                            android.R.layout.simple_list_item_multiple_choice, attractionDetails) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView textView = (TextView) view.findViewById(android.R.id.text1);
                            textView.setTextSize(16); // Increase text size
                            return view;
                        }
                    };
                    attractionsListView.setAdapter(adapter);

                    attractionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Attraction selectedAttraction = attractionList.get(position);
                            if (attractionsListView.isItemChecked(position)) {
                                selectedAttractions.add(selectedAttraction); // Add selected attraction
                            } else {
                                selectedAttractions.remove(selectedAttraction); // Remove deselected attraction
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Attraction>> call, Throwable t) {
                Log.e("AttractionsActivity", "Request failed: " + t.getMessage());
            }
        });
    }
}
