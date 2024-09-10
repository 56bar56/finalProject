package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Attraction_Preferance_Activity extends AppCompatActivity {

    private ImageView backButton;
    private EditText city;
    private RelativeLayout[] typeAttractionLayout;
    private View[] typeAttractionViews;
    private TextView[] typeAttractionTextViews;
    private String[] typeAttractions = {"historical", "museum", "park", "zoo", "nature", "theater", "beach", "shopping"};
    private boolean[] selectedIndexAttractions = new boolean[] {true, false, false, false, false, false, false, false}; // Store the default index of the selected option
    private RelativeLayout[] numStarsLayout;
    private View[] numStarsView;
    private TextView[] numStarsTextViews;
    private int selectedIndexNumStars = 3; // Store the default index of the selected option
    EditText maxBudgetEditText;
    CheckBox kidFriendlyCheckBox;
    private Button next_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attraction_preferance_page);


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
        typeAttractionLayout = new RelativeLayout[] {
                findViewById(R.id.historical1),
                findViewById(R.id.museum1),
                findViewById(R.id.park1),
                findViewById(R.id.zoo1),
                findViewById(R.id.nature1),
                findViewById(R.id.theater1),
                findViewById(R.id.beach1),
                findViewById(R.id.shopping1)
        };

        typeAttractionViews = new View[] {
                findViewById(R.id.historical2),
                findViewById(R.id.museum2),
                findViewById(R.id.park2),
                findViewById(R.id.zoo2),
                findViewById(R.id.nature2),
                findViewById(R.id.theater2),
                findViewById(R.id.beach2),
                findViewById(R.id.shopping2)
        };

        typeAttractionTextViews = new TextView[] {
                ((TextView) findViewById(R.id.historical3)),
                ((TextView) findViewById(R.id.museum3)),
                ((TextView) findViewById(R.id.park3)),
                ((TextView) findViewById(R.id.zoo3)),
                ((TextView) findViewById(R.id.nature3)),
                ((TextView) findViewById(R.id.theater3)),
                ((TextView) findViewById(R.id.beach3)),
                ((TextView) findViewById(R.id.shopping3))
        };

        // Set OnClickListener for each RelativeLayout
        for (int i = 0; i < typeAttractionLayout.length; i++) {
            final int index = i;
            typeAttractionLayout[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateAttractionStyles(index);
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

        // Taking care of budget and kid friendly
        maxBudgetEditText = findViewById(R.id.max_budget);
        kidFriendlyCheckBox = findViewById(R.id.kid_friendly);

        // Taking care of selected flights (back and forth) and hotel and restaurants
        Intent intent = getIntent();
        String selectedFlight = intent.getStringExtra("selectedFlight");
        String selectedReturnedFlight = intent.getStringExtra("selectedReturnedFlight");
        String selectedHotel = intent.getStringExtra("selectedHotel");
        String selectedRestaurants = intent.getStringExtra("selectedRestaurants");


        // Taking care of clicking on next
        next_button = findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected location from the AutoCompleteTextView
                String location = city.getText().toString();
                String attractions = getSelectedAttractions();
                String rating = String.valueOf(selectedIndexNumStars + 1);
                String averageCost = maxBudgetEditText.getText().toString();
                String kidFriendly = kidFriendlyCheckBox.isChecked() ? "yes" : "no";

                // Logging to check the values in the console
                Log.d("FlightInfo", "Location: " + location);
                Log.d("FlightInfo","Selected attractions" + attractions); // Example of logging the result
                Log.d("FlightInfo", "rating: " + rating);
                Log.d("FlightInfo", "average Cost: " + averageCost);
                Log.d("FlightInfo", "kid Friendly: " + kidFriendly);
                Log.d("FlightInfo", "selected Flight: " + selectedFlight);
                Log.d("FlightInfo", "selected ReturnedFlight: " + selectedReturnedFlight);
                Log.d("FlightInfo", "selected Hotel: " + selectedHotel);
                Log.d("FlightInfo", "selected Restaurants: " + selectedRestaurants);


                // Pass data to FlightsActivity using intent
                Intent intent = new Intent(Attraction_Preferance_Activity.this, AttractionsResultActivity.class);
                intent.putExtra("location", location);
                intent.putExtra("attractions", attractions);
                intent.putExtra("rating", rating);
                intent.putExtra("avgPrice", averageCost);
                intent.putExtra("kidFriendly", kidFriendly);
                intent.putExtra("selectedFlight", selectedFlight);
                intent.putExtra("selectedReturnedFlight", selectedReturnedFlight);
                intent.putExtra("selectedHotel", selectedHotel);
                intent.putExtra("selectedRestaurants", selectedRestaurants);

                startActivity(intent);
            }
        });
    }

    // Function to toggle selection and update styles for multiple selections
    private void updateAttractionStyles(int selectedIndex) {
        // Toggle the selected state for the clicked restaurant type
        selectedIndexAttractions[selectedIndex] = !selectedIndexAttractions[selectedIndex];

        // Iterate through all restaurant types and update styles
        for (int i = 0; i < typeAttractionViews.length; i++) {
            if (selectedIndexAttractions[i]) {
                // If the type is selected, highlight it
                typeAttractionViews[i].setBackgroundResource(R.drawable.rectangle_21_shape);
                typeAttractionTextViews[i].setTextColor(Color.WHITE);
            } else {
                // If not selected, revert to default styles
                typeAttractionViews[i].setBackgroundResource(R.drawable.rectangle_24_shape);
                typeAttractionTextViews[i].setTextColor(Color.BLACK);
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

    private String getSelectedAttractions() {
        StringBuilder selectedAttractions = new StringBuilder();

        // Loop through the restaurant types and check if they are selected
        for (int i = 0; i < selectedIndexAttractions.length; i++) {
            if (selectedIndexAttractions[i]) {
                if (selectedAttractions.length() > 0) {
                    selectedAttractions.append(", "); // Add a comma and space before each new restaurant (except the first one)
                }
                selectedAttractions.append(typeAttractions[i]); // Append the selected restaurant type
            }
        }

        return selectedAttractions.toString(); // Convert the StringBuilder to a String
    }

}