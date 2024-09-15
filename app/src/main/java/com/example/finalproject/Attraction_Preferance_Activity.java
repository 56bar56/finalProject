package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.api.GeoNamesApi;
import com.example.finalproject.items.Flight;
import com.example.finalproject.items.GeoNameResult;
import com.example.finalproject.items.GeoNamesResponse;
import com.example.finalproject.items.Hotel;
import com.example.finalproject.items.Restaurant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Attraction_Preferance_Activity extends AppCompatActivity {

    private ImageView backButton;
    //private EditText city;
    private AutoCompleteTextView cityAutoCompleteTextView;
    private GeoNamesApi geoNamesApi;
    private ImageView plusIcon;
    private String cities = "";
    private TextView selectedCities;
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
        //city = findViewById(R.id.city);
        // Taking care of city preferance
        //city = findViewById(R.id.city);
        Map<String, String> countryCodeMap = new HashMap<>();

        // Add countries and their codes to the map
        countryCodeMap.put("United Arab Emirates", "AE");
        countryCodeMap.put("Ethiopia", "ET");
        countryCodeMap.put("Netherlands", "NL");
        countryCodeMap.put("Andorra", "AD");
        countryCodeMap.put("New Zealand", "NZ");
        countryCodeMap.put("Thailand", "TH");
        countryCodeMap.put("Spain", "ES");
        countryCodeMap.put("China", "CN");
        countryCodeMap.put("Germany", "DE");
        countryCodeMap.put("Bhutan", "BT");
        countryCodeMap.put("Colombia", "CO");
        countryCodeMap.put("Australia", "AU");
        countryCodeMap.put("Belgium", "BE");
        countryCodeMap.put("Argentina", "AR");
        countryCodeMap.put("Egypt", "EG");
        countryCodeMap.put("Mexico", "MX");
        countryCodeMap.put("South Africa", "ZA");
        countryCodeMap.put("Venezuela", "VE");
        countryCodeMap.put("Morocco", "MA");
        countryCodeMap.put("USA", "US");
        countryCodeMap.put("Denmark", "DK");
        countryCodeMap.put("Cyprus", "CY");
        countryCodeMap.put("Qatar", "QA");
        countryCodeMap.put("UK", "GB");
        countryCodeMap.put("Fiji", "FJ");
        countryCodeMap.put("Switzerland", "CH");
        countryCodeMap.put("Malaysia", "MY");
        countryCodeMap.put("Nigeria", "NG");
        countryCodeMap.put("Peru", "PE");
        countryCodeMap.put("Portugal", "PT");
        countryCodeMap.put("Italy", "IT");
        countryCodeMap.put("Canada", "CA");
        countryCodeMap.put("Kenya", "KE");
        countryCodeMap.put("India", "IN");
        countryCodeMap.put("France", "FR");
        countryCodeMap.put("Japan", "JP");
        countryCodeMap.put("Norway", "NO");
        countryCodeMap.put("Papua New Guinea", "PG");
        countryCodeMap.put("Brazil", "BR");
        countryCodeMap.put("Chile", "CL");
        countryCodeMap.put("South Korea", "KR");
        countryCodeMap.put("Seychelles", "SC");
        countryCodeMap.put("Sweden", "SE");
        countryCodeMap.put("Maldives", "MV");
        countryCodeMap.put("Malta", "MT");
        countryCodeMap.put("Luxembourg", "LU");
        countryCodeMap.put("San Marino", "SM");
        countryCodeMap.put("Sri Lanka", "LK");
        countryCodeMap.put("Monaco", "MC");
        countryCodeMap.put("Mauritius", "MU");
        countryCodeMap.put("Iceland", "IS");
        countryCodeMap.put("Russia", "RU");
        countryCodeMap.put("Greece", "GR");
        countryCodeMap.put("Turkey", "TR");
        countryCodeMap.put("Philippines", "PH");
        countryCodeMap.put("Vietnam", "VN");
        countryCodeMap.put("Indonesia", "ID");
        countryCodeMap.put("Saudi Arabia", "SA");
        countryCodeMap.put("Israel", "IL");
        countryCodeMap.put("Finland", "FI");
        countryCodeMap.put("Ireland", "IE");
        countryCodeMap.put("Mexico", "MX");
        countryCodeMap.put("Poland", "PL");

        // Taking care of city preferance
        // Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.geonames.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        geoNamesApi = retrofit.create(GeoNamesApi.class);

        //city = findViewById(R.id.city);
        cityAutoCompleteTextView = findViewById(R.id.city);
        selectedCities = findViewById(R.id.selected_cities);

        // Calculating country code
        Flight selectedFlight = (Flight) getIntent().getSerializableExtra("selectedFlight");
        String airport =  selectedFlight.getArrival();
        String country = getCountryFromAirportString(airport);
        String code = countryCodeMap.get(country);
        cityAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String prefix = s.toString();
                if (!prefix.isEmpty()) {
                    fetchCities(code, prefix); // Replace with actual country code
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Taking care of plus icon for cities
        plusIcon = findViewById(R.id.plus_icon);

        plusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCity = cityAutoCompleteTextView.getText().toString().trim();

                if (selectedCity.isEmpty()) {
                    Toast.makeText(Attraction_Preferance_Activity.this, "Please select a city", Toast.LENGTH_SHORT).show();
                } else if (cities.contains(selectedCity)) {
                    Toast.makeText(Attraction_Preferance_Activity.this, selectedCity + " is already added", Toast.LENGTH_SHORT).show();
                } else {
                    // Add the city to the cities string
                    if (!cities.isEmpty()) {
                        cities += ", ";
                    }
                    cities += selectedCity;

                    // Clear the AutoCompleteTextView and update selected cities
                    cityAutoCompleteTextView.setText("");
                    selectedCities.setText("Selected cities: " + cities);

                    // Show success toast
                    Toast.makeText(Attraction_Preferance_Activity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        //Flight selectedFlight = (Flight) getIntent().getSerializableExtra("selectedFlight");
        Flight selectedReturnedFlight = (Flight) getIntent().getSerializableExtra("selectedReturnedFlight");
        Hotel selectedHotel = (Hotel) getIntent().getSerializableExtra("selectedHotel");
        String peopleNumber = getIntent().getStringExtra("peopleNumber");
        ArrayList<Restaurant> selectedRestaurants = (ArrayList<Restaurant>) getIntent().getSerializableExtra("selectedRestaurants");


        // Taking care of clicking on next
        next_button = findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected location from the AutoCompleteTextView
                String location = cities;
                String attractions = getSelectedAttractions();
                String rating = String.valueOf(selectedIndexNumStars + 1);
                String averageCost = maxBudgetEditText.getText().toString();
                String kidFriendly = kidFriendlyCheckBox.isChecked() ? "yes" : "no";

                // Input validation
                // Check if the city is empty
                if (location.isEmpty()) {
                    Toast.makeText(Attraction_Preferance_Activity.this, "Please enter the city", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if the budget is empty
                if (averageCost.isEmpty()) {
                    Toast.makeText(Attraction_Preferance_Activity.this, "Please enter the maximum budget", Toast.LENGTH_SHORT).show();
                    return;
                }
                // TODO check city validation

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
                intent.putExtra("peopleNumber", peopleNumber);

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

    // Function gets an airport string and return only the country
    public String getCountryFromAirportString(String airport) {
        // Split the string by commas
        String[] parts = airport.split(", ");

        // Return the last part, which should be the country
        if (parts.length >= 3) {
            return parts[2];
        } else {
            // Return empty or some default value if the format is incorrect
            return "";
        }
    }

    // Fetch cities method
    private void fetchCities(String countryCode, String prefix) {
        Call<GeoNamesResponse> call = geoNamesApi.getCities(
                prefix,
                countryCode,
                "P", // featureClass for populated places
                10,  // maxRows
                "Ariel0208" // replace with your GeoNames username
        );

        call.enqueue(new Callback<GeoNamesResponse>() {
            @Override
            public void onResponse(Call<GeoNamesResponse> call, Response<GeoNamesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> cityNames = new ArrayList<>();
                    for (GeoNameResult result : response.body().getGeonames()) {
                        cityNames.add(result.getName());
                        Log.d("City: ", result.getName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            Attraction_Preferance_Activity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            cityNames
                    );
                    cityAutoCompleteTextView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<GeoNamesResponse> call, Throwable t) {
                // Handle failure
                Log.e("API_ERROR", "Error fetching cities", t);
            }
        });
    }

}