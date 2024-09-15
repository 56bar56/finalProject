package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.adapters.AttractionListAdapter;
import com.example.finalproject.adapters.FlightListAdapter;
import com.example.finalproject.adapters.HotelListAdapter;
import com.example.finalproject.adapters.ImagePagerAdapter;
import com.example.finalproject.adapters.RestaurantListAdapter;
import com.example.finalproject.api.OpenAIApi;
import com.example.finalproject.items.Attraction;
import com.example.finalproject.items.Flight;
import com.example.finalproject.items.Hotel;
import com.example.finalproject.items.Message;
import com.example.finalproject.items.OpenAIRequest;
import com.example.finalproject.items.OpenAIResponse;
import com.example.finalproject.items.Restaurant;
import com.example.finalproject.items.Trip;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class TourDetailsActivity extends AppCompatActivity {

    private ImageView backButton;
    private Trip selectedTrip;
    private TextView title;
    private List<Attraction> attractions;
    private ViewPager2 imageViewPager;
    private View[] dots;
    private List<Integer> imageList;
    private int previousPosition = -1; // Track the current image

    private RecyclerView recyclerView;
    private FlightListAdapter flightAdapter;
    private HotelListAdapter hotelAdapter;
    private RestaurantListAdapter restaurantAdapter;
    private AttractionListAdapter attractionAdapter;
    private List<Flight> flightList;
    private List<Hotel> hotelList;
    private List<Restaurant> restaurantList;
    private List<Attraction> attractionList;
    private ImageView askAIButton;


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour_details_page);

        // Taking care of back button
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });
        askAIButton = findViewById(R.id.weather_icon);  // Make sure you add this button in XML

        askAIButton.setOnClickListener(v -> {
            askOpenAI();
        });

        selectedTrip = (Trip) getIntent().getSerializableExtra("selectedTrip");

        // Set title
        title = findViewById(R.id.title_destination);
        title.setText(extractCityCountry(selectedTrip.getSelectedFlight().getArrival()));

        // Take care image_info RelativeLayout
        //attractions = selectedTrip.getSelectedAttractions();


        // Initialize images
        imageList = Arrays.asList(R.drawable.top3number2, R.drawable.top3number3, R.drawable.ashim_d_silva_ihjohaud8ry_unsplash_1_ek1,
                R.drawable.ashim_d_silva_ihjohaud8ry_unsplash_1, R.drawable.top3number3);

        imageViewPager = findViewById(R.id.image_view_pager);

        ImagePagerAdapter adapter = new ImagePagerAdapter(this, imageList);
        imageViewPager.setAdapter(adapter);
        //imageViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        // Initialize dots
        dots = new View[] {
                findViewById(R.id.dot1), findViewById(R.id.dot2),
                findViewById(R.id.dot3), findViewById(R.id.dot4), findViewById(R.id.dot5)
        };

        // Set a PageTransformer to handle page change updates
        imageViewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                int currentItem = imageViewPager.getCurrentItem();
                if (currentItem != previousPosition) {
                    updateDots(currentItem % imageList.size());
                    updateImageDetails(currentItem % imageList.size());
                    previousPosition = currentItem;
                }
            }
        });


        // Taking care of the service bar
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.list_trips);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize your lists (fetch data or use dummy data)
        flightList = new ArrayList<>();
        flightList.add(selectedTrip.getSelectedFlight());
        flightList.add(selectedTrip.getSelectedReturnedFlight());
        hotelList = new ArrayList<>();
        hotelList.add(selectedTrip.getSelectedHotel());
        restaurantList = selectedTrip.getSelectedRestaurants();
        attractionList = selectedTrip.getSelectedAttractions();

        // Initialize adapters
        flightAdapter = new FlightListAdapter(this, flightList, null, null, null, null, false, false, null);
        hotelAdapter = new HotelListAdapter(this, null ,hotelList, null, null,  false);
        restaurantAdapter = new RestaurantListAdapter(this, restaurantList, false);
        attractionAdapter = new AttractionListAdapter(this, attractionList, false);

        // Set click listeners for buttons
        findViewById(R.id.flight_icon).setOnClickListener(v -> {
            recyclerView.setAdapter(flightAdapter);
        });

        findViewById(R.id.accommodation_icon).setOnClickListener(v -> {
            recyclerView.setAdapter(hotelAdapter);
        });

        findViewById(R.id.restaurant_icon).setOnClickListener(v -> {
            recyclerView.setAdapter(restaurantAdapter);
        });

        findViewById(R.id.attraction_icon).setOnClickListener(v -> {
            recyclerView.setAdapter(attractionAdapter);
        });
    }


    private void updateDots(int position) {
        // Reset all dots
        for (int i = 0; i < dots.length; i++) {
            if (i == position) {
                dots[i].setBackgroundResource(R.drawable.rectangle_9_shape);  // Wide circle
                dots[i].getLayoutParams().width = 20;
            } else {
                dots[i].setBackgroundResource(R.drawable.ellipse_15_shape);  // Normal dot
                dots[i].getLayoutParams().width = 5;
            }
        }

    }

    private void updateImageDetails(int position) {
        TextView location = findViewById(R.id.location);
        TextView attractionName = findViewById(R.id.attraction_name);
        TextView price = findViewById(R.id.price);

        switch (position) {
            case 0:
                location.setText("Bangkok, Thailand");
                attractionName.setText("Capital of Thailand");
                price.setText("10,000$");
                break;
            case 1:
                location.setText("Paris, France");
                attractionName.setText("Eiffel Tower");
                price.setText("12,000$");
                break;
            case 2:
                location.setText("New York, USA");
                attractionName.setText("Statue of Liberty");
                price.setText("15,000$");
                break;
            case 3:
                location.setText("Jerusalem, Israel");
                attractionName.setText("The Cotel");
                price.setText("20,000$");
                break;
            case 4:
                location.setText("Amsterdam, Holland");
                attractionName.setText("Van Gogh Museum");
                price.setText("12,000$");
                break;

        }
    }

    // Return the last two parts (city and country)
    public static String extractCityCountry(String airportDetails) {
        String[] parts = airportDetails.split(", ");

        if (parts.length >= 3) {
            return parts[1] + ", " + parts[2];
        } else {
            return airportDetails;
        }
    }
    private void askOpenAI() {
        String tripDetails = "Here are the details of my trip:\n\n";

        // Flight details
        Flight selectedFlight = selectedTrip.getSelectedFlight();
        Flight returnFlight = selectedTrip.getSelectedReturnedFlight();
        tripDetails += "Outbound Flight: " + selectedFlight.getFlightNumber() + " from " + selectedFlight.getDeparture() + " to " + selectedFlight.getArrival() + " on " + selectedFlight.getTakeoff() + ".\n";
        tripDetails += "Return Flight: " + returnFlight.getFlightNumber() + " from " + returnFlight.getDeparture() + " to " + returnFlight.getArrival() + " on " + returnFlight.getTakeoff() + ".\n";

        // Hotel details
        Hotel selectedHotel = selectedTrip.getSelectedHotel();
        tripDetails += "Hotel: " + selectedHotel.getName() + " located in " + selectedHotel.getLocation() + " with a rating of " + selectedHotel.getRating() + " stars.\n";

        // Restaurant details
        List<Restaurant> selectedRestaurants = selectedTrip.getSelectedRestaurants();
        tripDetails += "Restaurants:\n";
        for (Restaurant restaurant : selectedRestaurants) {
            tripDetails += "- " + restaurant.getName() + ", located in " + restaurant.getLocation() + ", serves " + restaurant.getCuisine() + " cuisine. Average cost: $" + restaurant.getAverageCost() + ". Rating: " + restaurant.getRating() + "/5.\n";
        }

        // Attraction details
        List<Attraction> selectedAttractions = selectedTrip.getSelectedAttractions();
        tripDetails += "Attractions:\n";
        for (Attraction attraction : selectedAttractions) {
            tripDetails += "- " + attraction.getAttraction() + " in " + attraction.getLocation() + ". Rating: " + attraction.getRating() + "/5. Average cost: $" + attraction.getAverageCost() + ". Kid-friendly: " + attraction.getKidFriendly() + ".\n";
        }

        final String tripDet = tripDetails;
        // Initialize StringBuilder to concatenate responses
        StringBuilder fullResponse = new StringBuilder();

        // Send the first request for advantages
        sendAIRequest("(very short answer)Tell me like 3 good advantages of this trip:\n\n" + tripDet, new Callback<OpenAIResponse>() {
            @Override
            public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response) {
                if (response.isSuccessful()) {
                    fullResponse.append("### Advantages:\n");
                    fullResponse.append(response.body().getChoices().get(0).getMessage().getContent());
                    fullResponse.append("\n\n");

                    // Send the second request for disadvantages
                    sendAIRequest("(very short answer)Tell me 3 disadvantages of this trip:\n\n" + tripDet, new Callback<OpenAIResponse>() {
                        @Override
                        public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response2) {
                            if (response2.isSuccessful()) {
                                fullResponse.append("### Disadvantages:\n");
                                fullResponse.append(response2.body().getChoices().get(0).getMessage().getContent());
                                fullResponse.append("\n\n");

                                // Send the third request for weather and attire
                                sendAIRequest("(very short answer for the questions)What will the weather be like, and what should I wear during the trip to " + tripDet, new Callback<OpenAIResponse>() {
                                    @Override
                                    public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response3) {
                                        if (response3.isSuccessful()) {
                                            fullResponse.append("### Weather and Attire:\n");
                                            fullResponse.append(response3.body().getChoices().get(0).getMessage().getContent());

                                            // After all responses, show the AI response in a popup
                                            showAIResponsePopup(fullResponse.toString());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                                        Toast.makeText(TourDetailsActivity.this, "Failed to get weather response", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                            Toast.makeText(TourDetailsActivity.this, "Failed to get disadvantages response", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                Toast.makeText(TourDetailsActivity.this, "Failed to get advantages response", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Helper method to send individual OpenAI requests
    private void sendAIRequest(String prompt, Callback<OpenAIResponse> callback) {
        // Create your API request
        OpenAIRequest request = new OpenAIRequest("gpt-4-turbo", Arrays.asList(new Message("user", prompt)), 100);

        // Make the API call
        OpenAIApi api = ApiClient.getOpenAIApi();
        Call<OpenAIResponse> call = api.getAIResponse(request);

        // Execute the call
        call.enqueue(callback);
    }

    // Method to show AI response in a popup
    private void showAIResponsePopup(String aiResponse) {
        // Inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_ai_response, null);

        // Find the TextView in the custom layout
        TextView responseTextView = dialogView.findViewById(R.id.ai_response_text);

        // Set the AI response text
        responseTextView.setText(aiResponse);

        // Create and show the dialog with the custom layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Close button
        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}