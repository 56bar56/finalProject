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

import com.example.finalproject.items.Flight;

public class Hotel_Preferance_Activity extends AppCompatActivity {

    private ImageView backButton;
    private EditText city;
    private RelativeLayout[] distanceLayout;
    private View[] distanceViews;
    private TextView[] distanceTextViews;
    private String[] maxDistance = {"5", "10", "20"};
    private int selectedIndexDistance = 0; // Store the default index of the selected option
    private RelativeLayout[] typeHouseLayout;
    private View[] typeHouseViews;
    private TextView[] typeHouseTextViews;
    private String[] typeOfHouse = {"hotel", "cabbin", "airbnb", "warehouse"};
    private int selectedIndexType = 0; // Store the default index of the selected option
    private RelativeLayout[] numStarsLayout;
    private View[] numStarsView;
    private TextView[] numStarsTextViews;
    private int selectedIndexNumStars = 3; // Store the default index of the selected option
    EditText maxBudgetEditText;
    CheckBox starTripCheckBox;
    private RelativeLayout numberOfStopsLayout;
    private RelativeLayout[] numStopLayout;
    private View[] numStopViews;
    private TextView[] numStopTextViews;
    private int selectedIndexNumStop = 1; // Store the default index of the selected option
    private Button next_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_preferance_page);


        Flight selectedFlight = (Flight) getIntent().getSerializableExtra("selectedFlight");
        Flight selectedReturnedFlight = (Flight) getIntent().getSerializableExtra("selectedReturnedFlight");

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
        distanceLayout = new RelativeLayout[] {
                findViewById(R.id.center1),
                findViewById(R.id.suburb1),
                findViewById(R.id.outside1)
        };

        distanceViews = new View[] {
                findViewById(R.id.center2),
                findViewById(R.id.suburb2),
                findViewById(R.id.outside2)
        };

        distanceTextViews = new TextView[] {
                ((TextView) findViewById(R.id.center3)),
                ((TextView) findViewById(R.id.suburb3)),
                ((TextView) findViewById(R.id.outside3))
        };

        // Set OnClickListener for each RelativeLayout
        for (int i = 0; i < distanceLayout.length; i++) {
            final int index = i;
            distanceLayout[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateStyles(distanceViews, distanceTextViews, index);
                    selectedIndexDistance = index;
                }
            });
        }

        // Taking care of type of accommodation
        // Initialize views
        typeHouseLayout = new RelativeLayout[] {
                findViewById(R.id.hotel1),
                findViewById(R.id.Cabbin1),
                findViewById(R.id.airbnb1),
                findViewById(R.id.warehouse1)
        };

        typeHouseViews = new View[] {
                findViewById(R.id.hotel2),
                findViewById(R.id.Cabbin2),
                findViewById(R.id.airbnb2),
                findViewById(R.id.warehouse2)
        };

        typeHouseTextViews = new TextView[] {
                ((TextView) findViewById(R.id.hotel3)),
                ((TextView) findViewById(R.id.Cabbin3)),
                ((TextView) findViewById(R.id.airbnb3)),
                ((TextView) findViewById(R.id.warehouse3))
        };

        // Set OnClickListener for each RelativeLayout
        for (int i = 0; i < typeHouseLayout.length; i++) {
            final int index = i;
            typeHouseLayout[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateStyles(typeHouseViews, typeHouseTextViews, index);
                    selectedIndexType = index;
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

        // Taking care of budget and star trip
        maxBudgetEditText = findViewById(R.id.max_budget);
        starTripCheckBox = findViewById(R.id.star_trip);

        // Taking care of number of stops
        // Taking care of disappearing the layout of number of stops
        numberOfStopsLayout = findViewById(R.id.number_of_stops);
        starTripCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                numberOfStopsLayout.setVisibility(View.GONE);
            } else {
                numberOfStopsLayout.setVisibility(View.VISIBLE);
            }
        });

        // Initialize views
        numStopLayout = new RelativeLayout[] {
                findViewById(R.id.two_stop1),
                findViewById(R.id.three_stop1),
                findViewById(R.id.four_stop1),
                findViewById(R.id.five_stop1),
                findViewById(R.id.six_stop1)
        };

        numStopViews = new View[] {
                findViewById(R.id.two_stop2),
                findViewById(R.id.three_stop2),
                findViewById(R.id.four_stop2),
                findViewById(R.id.five_stop2),
                findViewById(R.id.six_stop2)
        };

        numStopTextViews = new TextView[] {
                ((TextView) findViewById(R.id.two_stop3)),
                ((TextView) findViewById(R.id.three_stop3)),
                ((TextView) findViewById(R.id.four_stop3)),
                ((TextView) findViewById(R.id.five_stop3)),
                ((TextView) findViewById(R.id.six_stop3))
        };

        // Set OnClickListener for each RelativeLayout
        for (int i = 0; i < numStopLayout.length; i++) {
            final int index = i;
            numStopLayout[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateStyles(numStopViews, numStopTextViews, index);
                    selectedIndexNumStop = index;
                }
            });
        }

        // Taking care of clicking on next
        next_button = findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected location from the AutoCompleteTextView
                String location = city.getText().toString();
                String kmFromCity = maxDistance[selectedIndexDistance];
                String hotelOrCabbin = typeOfHouse[selectedIndexType];
                String rating = String.valueOf(selectedIndexNumStars + 1);
                String maxBudget = maxBudgetEditText.getText().toString();
                String isStarTrip = starTripCheckBox.isChecked() ? "true" : "false";
                String numStops = starTripCheckBox.isChecked() ? "null" : String.valueOf(selectedIndexNumStop + 2);

                // Logging to check the values in the console
                Log.d("FlightInfo", "Location: " + location);
                Log.d("FlightInfo", "max Km from city: " + kmFromCity);
                Log.d("FlightInfo", "type of accommodation: " + hotelOrCabbin);
                Log.d("FlightInfo", "rating: " + rating);
                Log.d("FlightInfo", "maxBudget: " + maxBudget);
                Log.d("FlightInfo", "Is a star trip: " + isStarTrip);
                Log.d("FlightInfo", "Number of stops: " + numStops);

                // Pass data to FlightsActivity using intent
                Intent intent = new Intent(Hotel_Preferance_Activity.this, HotelResultsActivity.class);
                intent.putExtra("location", location);
                intent.putExtra("kmFromCity", kmFromCity);
                intent.putExtra("hotelOrCabbin", hotelOrCabbin);
                intent.putExtra("rating", rating);
                intent.putExtra("maxPriceForNight", maxBudget);
                intent.putExtra("isStarTrip", isStarTrip);
                intent.putExtra("numStops", numStops);


                intent.putExtra("selectedFlight", selectedFlight);
                intent.putExtra("selectedReturnedFlight", selectedReturnedFlight);

                startActivity(intent);
            }
        });
    }

    private void updateStyles(View[] views, TextView[] textViews, int selectedIndex) {
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

}