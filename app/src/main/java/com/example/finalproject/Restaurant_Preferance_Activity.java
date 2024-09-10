package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Restaurant_Preferance_Activity extends AppCompatActivity {

    private ImageView backButton;
    private EditText city;
    private RelativeLayout[] typeRestaurantLayout;
    private View[] typeRestaurantViews;
    private TextView[] typeRestaurantTextViews;
    private String[] typeRestaurant = {"Asian", "American", "Italian", "French", "Mexican", "Mediterranean"};
    private boolean[] selectedIndexRestaurants = new boolean[] {true, false, false, false, false, false}; // Store the default index of the selected option
    private RelativeLayout[] numStarsLayout;
    private View[] numStarsView;
    private TextView[] numStarsTextViews;
    private int selectedIndexNumStars = 3; // Store the default index of the selected option
    EditText maxBudgetEditText;
    private Button next_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_preferance_page);

        // Taking care of back button
        // Set onClickListener for the back button to go to Flights_Activity
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        // Taking care of city preferance
        city = findViewById(R.id.city);

        // Taking care of distance from the city
        // Initialize views
        typeRestaurantLayout = new RelativeLayout[] {
                findViewById(R.id.asian1),
                findViewById(R.id.american1),
                findViewById(R.id.italian1),
                findViewById(R.id.french1),
                findViewById(R.id.mexican1),
                findViewById(R.id.mediterranean1)
        };

        typeRestaurantViews = new View[] {
                findViewById(R.id.asian2),
                findViewById(R.id.american2),
                findViewById(R.id.italian2),
                findViewById(R.id.french2),
                findViewById(R.id.mexican2),
                findViewById(R.id.mediterranean2)
        };

        typeRestaurantTextViews = new TextView[] {
                ((TextView) findViewById(R.id.asian3)),
                ((TextView) findViewById(R.id.american3)),
                ((TextView) findViewById(R.id.italian3)),
                ((TextView) findViewById(R.id.french3)),
                ((TextView) findViewById(R.id.mexican3)),
                ((TextView) findViewById(R.id.mediterranean3)),
        };

        // Set OnClickListener for each RelativeLayout
        for (int i = 0; i < typeRestaurantLayout.length; i++) {
            final int index = i;
            typeRestaurantLayout[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateRestaurantStyles(index);
                }
            });
        }

        // Taking care of number of stars
        // Initialize views
        numStarsLayout = new RelativeLayout[] {
                findViewById(R.id.one_star1),
                findViewById(R.id.two_star1),
                findViewById(R.id.three_star1),
                findViewById(R.id.four_star1),
                findViewById(R.id.five_star1)
        };

        numStarsView = new View[] {
                findViewById(R.id.one_star2),
                findViewById(R.id.two_star2),
                findViewById(R.id.three_star2),
                findViewById(R.id.four_star2),
                findViewById(R.id.five_star2)
        };

        numStarsTextViews = new TextView[] {
                ((TextView) findViewById(R.id.one_star3)),
                ((TextView) findViewById(R.id.two_star3)),
                ((TextView) findViewById(R.id.three_star3)),
                ((TextView) findViewById(R.id.four_star3)),
                ((TextView) findViewById(R.id.five_star3))
        };

        // Set OnClickListener for each RelativeLayout
        for (int i = 0; i < numStarsLayout.length; i++) {
            final int index = i;
            numStarsLayout[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateStyles(numStarsView, numStarsTextViews, index);
                    selectedIndexNumStars = index;
                }
            });
        }

        // Taking care of average cost per person
        maxBudgetEditText = findViewById(R.id.max_budget);

        // Taking care of selected flights (back and forth) and hotel
        Intent intent = getIntent();
        String selectedFlight = intent.getStringExtra("selectedFlight");
        String selectedReturnedFlight = intent.getStringExtra("selectedReturnedFlight");
        String selectedHotel = intent.getStringExtra("selectedHotel");


        // Taking care of clicking on next
        next_button = findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected location from the AutoCompleteTextView
                String location = city.getText().toString();
                String cuisine = getSelectedRestaurants();
                String rating = String.valueOf(selectedIndexNumStars + 1);
                String averageCost = maxBudgetEditText.getText().toString();

                // Logging to check the values in the console
                Log.d("FlightInfo", "Location: " + location);
                Log.d("FlightInfo", "rating: " + rating);
                Log.d("FlightInfo", "average Cost: " + averageCost);
                Log.d("FlightInfo", "selected Flight: " + selectedFlight);
                Log.d("FlightInfo", "selected Returned Flight: " + selectedReturnedFlight);
                Log.d("FlightInfo", "selected Hotel: " + selectedHotel);
                Log.d("Selected Restaurants", cuisine); // Example of logging the result


                // Pass data to FlightsActivity using intent
                Intent intent = new Intent(Restaurant_Preferance_Activity.this, RestaurantsResult.class);
                intent.putExtra("location", location);
                intent.putExtra("cuisine", cuisine);
                intent.putExtra("rating", rating);
                intent.putExtra("averageCost", averageCost);
                intent.putExtra("selectedFlight", selectedFlight);
                intent.putExtra("selectedReturnedFlight", selectedReturnedFlight);
                intent.putExtra("selectedHotel", selectedHotel);

                startActivity(intent);
            }
        });
    }

    // Function to toggle selection and update styles for multiple selections
    private void updateRestaurantStyles(int selectedIndex) {
        // Toggle the selected state for the clicked restaurant type
        selectedIndexRestaurants[selectedIndex] = !selectedIndexRestaurants[selectedIndex];

        // Iterate through all restaurant types and update styles
        for (int i = 0; i < typeRestaurantViews.length; i++) {
            if (selectedIndexRestaurants[i]) {
                // If the type is selected, highlight it
                typeRestaurantViews[i].setBackgroundResource(R.drawable.rectangle_21_shape);
                typeRestaurantTextViews[i].setTextColor(Color.WHITE);
            } else {
                // If not selected, revert to default styles
                typeRestaurantViews[i].setBackgroundResource(R.drawable.rectangle_24_shape);
                typeRestaurantTextViews[i].setTextColor(Color.BLACK);
            }
        }
    }

    private void updateStyles(View[] views, TextView[] textViews, int selectedIndex) {
        // Update the selected index
        for (int i = 0; i < views.length; i++) {
            if (i == selectedIndex) {
                views[i].setBackgroundResource(R.drawable.rectangle_21_shape);
                textViews[i].setTextColor(Color.WHITE);
            } else {
                views[i].setBackgroundResource(R.drawable.rectangle_24_shape);
                textViews[i].setTextColor(Color.BLACK);
            }
        }
    }

    private String getSelectedRestaurants() {
        StringBuilder selectedRestaurants = new StringBuilder();

        // Loop through the restaurant types and check if they are selected
        for (int i = 0; i < selectedIndexRestaurants.length; i++) {
            if (selectedIndexRestaurants[i]) {
                if (selectedRestaurants.length() > 0) {
                    selectedRestaurants.append(", "); // Add a comma and space before each new restaurant (except the first one)
                }
                selectedRestaurants.append(typeRestaurant[i]); // Append the selected restaurant type
            }
        }

        return selectedRestaurants.toString(); // Convert the StringBuilder to a String
    }

}