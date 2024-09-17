package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.adapters.AttractionListAdapter;
import com.example.finalproject.api.AttractionAPI;
import com.example.finalproject.api.TripAPI;
import com.example.finalproject.items.Attraction;
import com.example.finalproject.items.Flight;
import com.example.finalproject.items.Hotel;
import com.example.finalproject.items.Restaurant;
import com.example.finalproject.items.Trip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttractionsResultActivity extends AppCompatActivity {


    private ImageView backButton;
    private TextView title;
    private TextView instructions;
    private RecyclerView attractionRecyclerView;
    private ArrayList<Attraction> attractionList;
    private List<Attraction> selectedAttractions = new ArrayList<>(); // To store selected attractions
    private AttractionListAdapter adapter;
    private Button nextButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_results_page); // same xml for attractions and restaurants

        // Taking care of back button
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        // Set title
        title = findViewById(R.id.title);
        title.setText("Attractions");

        // Set RecyclerView
        attractionRecyclerView = findViewById(R.id.list);
        attractionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set next button
        nextButton = findViewById(R.id.next_button);
        nextButton.setText("Finish");

        // Retrieve user data from AttractionPreferencesActivity
        String averageCost = getIntent().getStringExtra("avgPrice");
        String location = getIntent().getStringExtra("location");
        String rating = getIntent().getStringExtra("rating");
        String attractionType = getIntent().getStringExtra("attractions");
        String kidFriendly = getIntent().getStringExtra("kidFriendly");
        String peopleNumber = getIntent().getStringExtra("peopleNumber");
        Flight selectedFlight = (Flight) getIntent().getSerializableExtra("selectedFlight");
        Flight selectedReturnedFlight = (Flight) getIntent().getSerializableExtra("selectedReturnedFlight");
        Hotel selectedHotel = (Hotel) getIntent().getSerializableExtra("selectedHotel");
        ArrayList<Restaurant> returnedRestaurants = (ArrayList<Restaurant>) getIntent().getSerializableExtra("selectedRestaurants");


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
        fetchAttractions(filterRequest);



        // Set "Done Choosing" button click listener
        nextButton.setOnClickListener(v -> {
            // Pass selected attractions to another activity (e.g., SummaryActivity)
            selectedAttractions = adapter.getSelectedAttractions();
            Intent intent = new Intent(AttractionsResultActivity.this, FinishTripActivity.class);
            intent.putExtra("selectedAttractions", new ArrayList<>(selectedAttractions));
            intent.putExtra("selectedRestaurants", returnedRestaurants);
            intent.putExtra("selectedHotel", selectedHotel);
            intent.putExtra("selectedReturnedFlight", selectedReturnedFlight);
            intent.putExtra("selectedFlight", selectedFlight);
            intent.putExtra("peopleNumber", peopleNumber);
            startActivity(intent);
        });
    }

    private void fetchAttractions(AttractionFilterRequest request) {
        AttractionAPI attractionAPI = RetrofitClient.getClient("http://192.168.1.41:5000").create(AttractionAPI.class);

        Call<List<Attraction>> call = attractionAPI.getFilteredAttractions(request);
        call.enqueue(new Callback<List<Attraction>>() {
            @Override
            public void onResponse(Call<List<Attraction>> call, Response<List<Attraction>> response) {
                if (!response.isSuccessful()) {
                    Log.e("AttractionsActivity", "Response error: " + response.code());
                    return;
                }

                attractionList = (ArrayList<Attraction>) response.body();
                instructions = findViewById(R.id.instructions);
                if (attractionList != null) {
                    adapter = new AttractionListAdapter(AttractionsResultActivity.this, attractionList, true);
                    attractionRecyclerView.setAdapter(adapter);
                    // Taking care of instructions text
                    instructions.setText("Please pick attractions");
                }
                else {
                    instructions.setText("No attractions where found, please click next or go back and change your preferences");
                }
            }

            @Override
            public void onFailure(Call<List<Attraction>> call, Throwable t) {
                Log.e("AttractionsActivity", "Request failed: " + t.getMessage());
            }
        });
    }
}
