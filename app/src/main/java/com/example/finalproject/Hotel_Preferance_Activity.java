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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class Hotel_Preferance_Activity extends AppCompatActivity {

    private ImageView backButton;
    private AutoCompleteTextView cityAutoCompleteTextView;
    private GeoNamesApi geoNamesApi;
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
        String peopleNumber = getIntent().getStringExtra("peopleNumber");
        Flight selectedReturnedFlight = (Flight) getIntent().getSerializableExtra("selectedReturnedFlight");

        // Taking care of back button
        // Set onClickListener for the back button to go to Flights_Activity
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });

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

        // Calculating country code
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
                String location = cityAutoCompleteTextView.getText().toString();
                String kmFromCity = maxDistance[selectedIndexDistance];
                String hotelOrCabbin = typeOfHouse[selectedIndexType];
                String rating = String.valueOf(selectedIndexNumStars + 1);
                String maxBudget = maxBudgetEditText.getText().toString();
                String isStarTrip = starTripCheckBox.isChecked() ? "true" : "false";
                String numStops = starTripCheckBox.isChecked() ? "null" : String.valueOf(selectedIndexNumStop + 2);

                // Input validation
                // Check if the city is empty
                if (location.isEmpty()) {
                    Toast.makeText(Hotel_Preferance_Activity.this, "Please enter the city", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if the budget is empty
                if (maxBudget.isEmpty()) {
                    Toast.makeText(Hotel_Preferance_Activity.this, "Please enter the maximum budget", Toast.LENGTH_SHORT).show();
                    return;
                }
                // TODO check city validation

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
                intent.putExtra("peopleNumber", peopleNumber);
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
                            Hotel_Preferance_Activity.this,
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