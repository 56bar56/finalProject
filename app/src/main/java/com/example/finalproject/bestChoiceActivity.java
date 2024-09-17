package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.adapters.AttractionListAdapter;
import com.example.finalproject.adapters.FlightListAdapter;
import com.example.finalproject.adapters.HotelListAdapter;
import com.example.finalproject.adapters.RestaurantListAdapter;
import com.example.finalproject.api.AttractionAPI;
import com.example.finalproject.api.HotelAPI;
import com.example.finalproject.api.RestaurantAPI;
import com.example.finalproject.api.TripAPI;
import com.example.finalproject.api.FlightAPI;
import com.example.finalproject.items.Attraction;
import com.example.finalproject.items.Flight;
import com.example.finalproject.items.Hotel;
import com.example.finalproject.items.Restaurant;
import com.example.finalproject.items.Trip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class bestChoiceActivity extends AppCompatActivity {
    private ImageView vButton;
    private ImageView xButton;
    private TextView title;
    private RecyclerView recyclerView;
    private FlightListAdapter flightAdapter;
    private HotelListAdapter hotelAdapter;
    private RestaurantListAdapter restaurantAdapter;
    private AttractionListAdapter attractionAdapter;

    private List<Trip> tripList;
    private List<Flight> flights;
    private List<Flight> flightsReturned;
    private List<Hotel> hotels;
    private List<Restaurant> restaurants;
    private List<Attraction> attractions;
    private Flight bestFlight;
    private Flight returnedFlight;
    private Hotel returnHotel;
    private ArrayList<Restaurant> returnedRestaurants;
    private List<Attraction> returnedAttractions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_choice);
        getUserTrips();
        xButton = findViewById(R.id.delete_button);

        xButton.setOnClickListener(v -> {
            Intent intent = new Intent(bestChoiceActivity.this, WelcomeActivity.class);
            startActivity(intent);
        });

    }
    private void createTripRecommendations(List<Trip> userTrips) {
        if (userTrips == null || userTrips.isEmpty()) {
            title = findViewById(R.id.title);
            title.setText("cant create trip - no previous trips");
            return;
        }

        // Maps to store frequency of departure and arrival locations
        Map<String, Integer> departureCount = new HashMap<>();
        Map<String, Integer> arrivalCount = new HashMap<>();
        double totalFlightCost = 0.0;
        int flightCount = 0;

        for (Trip trip : userTrips) {
            String departureLocation = trip.getSelectedFlight().getDeparture();
            String arrivalLocation = trip.getSelectedFlight().getArrival();
            double flightPrice = trip.getSelectedFlight().getPrice();

            // Count departure locations
            departureCount.put(departureLocation, departureCount.getOrDefault(departureLocation, 0) + 1);

            // Count arrival locations
            arrivalCount.put(arrivalLocation, arrivalCount.getOrDefault(arrivalLocation, 0) + 1);

            // Sum the total flight prices and count the number of flights
            totalFlightCost += flightPrice;
            flightCount++;
        }

        // Find the most common departure (user's home) and arrival locations
        String mostCommonDeparture = findMostCommonLocation(departureCount);
        String mostCommonArrival = findMostCommonLocation(arrivalCount);

        // Calculate the average price paid for flights
        double averageFlightPrice = flightCount > 0 ? totalFlightCost / flightCount : 0.0;


        // Create a refined flight filter request
        sendFlightFilterRequest(mostCommonDeparture, mostCommonArrival, averageFlightPrice);

        //bestFlight = selectBestFlight(flights);

        int countForReturned = 0;
        int countForNotReturned = 0;
        for (Trip trip : userTrips) {
            if(trip.getSelectedFlight().getDeparture().equals(mostCommonDeparture)){
                if(trip.getSelectedFlight().getDeparture().equals(mostCommonArrival)){
                    if(trip.getSelectedReturnedFlight() == null){
                        countForNotReturned++;
                    }
                    else{
                        countForNotReturned++;
                    }
                }
            }
        }
        if(countForReturned <= countForNotReturned){
            returnedFlight = null;
        }
        else{
            sendReturnedFlightFilterRequest(mostCommonArrival ,mostCommonDeparture, averageFlightPrice, bestFlight.getLanding());
        }
        String mostCommonArrivalCity = "";

        // Split the string by comma and take the first part
        if (mostCommonArrival != null && mostCommonArrival.contains(",")) {
            mostCommonArrivalCity = mostCommonArrival.split(",")[1].trim(); // Get the city and trim any whitespace
        }

        //get hotel
        //analyzeHotelPreferences(mostCommonArrivalCity, mostCommonArrival);

        //get the average x
        int numberOfRestaurantsInTrip = 0;
        int counterTrips = 0;
        for (Trip trip : userTrips) {
            if(trip.getSelectedFlight().getDeparture().equals(mostCommonDeparture)){
                if(trip.getSelectedFlight().getArrival().equals(mostCommonArrival)) {
                    numberOfRestaurantsInTrip += trip.getSelectedRestaurants().size();
                    counterTrips++;
                }
            }
        }
        int averageRestaurants = 4;
        if(counterTrips != 0) {
            averageRestaurants = numberOfRestaurantsInTrip / counterTrips;
        }

        //analyzeRestaurantPreferences(averageRestaurants, mostCommonArrival);

        //get the average x
        int numberOfAttractionInTrip = 0;
        counterTrips = 0;
        for (Trip trip : userTrips) {
            if(trip.getSelectedFlight().getDeparture().equals(mostCommonDeparture)){
                if(trip.getSelectedFlight().getArrival().equals(mostCommonArrival)) {
                    numberOfAttractionInTrip += trip.getSelectedAttractions().size();
                    counterTrips++;
                }
            }
        }
        int averageAttractions = 4;
        if(counterTrips != 0) {
            averageAttractions = numberOfAttractionInTrip / counterTrips;
        }

        analyzeHotelPreferences(mostCommonArrivalCity, mostCommonArrival, averageRestaurants, averageAttractions);
    }
    private void createTripRecommendationsPartTwo(){
        title = findViewById(R.id.title);
        Trip newTripForUser = new Trip(bestFlight, returnedFlight, returnHotel, returnedRestaurants, returnedAttractions, globalVars.username);

        recyclerView = findViewById(R.id.list_trips);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Flight> flightList = new ArrayList<>();
        if (bestFlight != null) {
            flightList.add(bestFlight);
        }
        if (returnedFlight != null) {
            flightList.add(returnedFlight);
        }
        ArrayList<Hotel>hotelList = new ArrayList<>();
        hotelList.add(returnHotel);

        // Initialize adapters
        flightAdapter = new FlightListAdapter(this, flightList, null, null, null, null, false, false, null);
        hotelAdapter = new HotelListAdapter(this, null ,hotelList, null, null,  false);
        restaurantAdapter = new RestaurantListAdapter(this, returnedRestaurants, false);
        attractionAdapter = new AttractionListAdapter(this, returnedAttractions, false);

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
        vButton = findViewById(R.id.confirm_button);
        xButton = findViewById(R.id.delete_button);

        vButton.setOnClickListener(v -> {
            sendTripToServer(newTripForUser);
            Intent intent = new Intent(bestChoiceActivity.this, WelcomeActivity.class);
            startActivity(intent);
        });

        xButton.setOnClickListener(v -> {
            Intent intent = new Intent(bestChoiceActivity.this, WelcomeActivity.class);
            startActivity(intent);
        });
        title.setText("Your Trip Is Ready !");

    }
    private void getUserTrips() {
        String username = globalVars.username;

        TripAPI tripAPI = RetrofitClient.getClient("http://192.168.1.41:5000").create(TripAPI.class);
        Call<List<Trip>> call = tripAPI.getUserTrips(username);

        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                tripList = response.body();
                createTripRecommendations(tripList);
            }

            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                Log.e("bestChoiceActivity", "Failed to fetch trips: " + t.getMessage());
                Toast.makeText(bestChoiceActivity.this, "Request failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendTripToServer(Trip myTrip) {
        // Send a request to the server to create the trip (use Retrofit, Volley, or another library)
        // This is a placeholder to show where you would put the network request
        TripAPI tripAPI = RetrofitClient.getClient("http://192.168.1.41:5000").create(TripAPI.class);
        Call<Trip> call = tripAPI.createTrip(myTrip);
        call.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                if (!response.isSuccessful()) {
                    Log.e("AttractionsResult", "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                Log.e("AttractionsActivity", "Request failed: " + t.getMessage());
            }
        });

    }

    private String findMostCommonLocation(Map<String, Integer> locationCount) {
        String mostCommonLocation = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : locationCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostCommonLocation = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        return mostCommonLocation;
    }
    private void sendReturnedFlightFilterRequest(String departureLocation, String arrivalLocation, double averagePrice, String departureDate) {
        FlightFilterRequest filterRequest = new FlightFilterRequest(
                averagePrice,
                departureLocation,
                arrivalLocation,
                departureDate, // You can refine this with a specific date range if needed
                null
        );


        // Call the API to get filtered flights
        FlightAPI flightAPI = RetrofitClient.getClient("http://192.168.1.41:5000").create(FlightAPI.class);
        Call<List<Flight>> call = flightAPI.filterFlights(filterRequest);

        call.enqueue(new Callback<List<Flight>>() {
            @Override
            public void onResponse(Call<List<Flight>> call, Response<List<Flight>> response) {

                flightsReturned = response.body();
                if(flightsReturned == null){
                    returnedFlight = null;
                }
                else{
                    returnedFlight = selectBestFlight(flightsReturned);
                }
            }

            @Override
            public void onFailure(Call<List<Flight>> call, Throwable t) {
                Log.e("bestChoiceActivity", "Flight request failed: " + t.getMessage());
            }
        });
    }
    // Method to get the flight with the lowest price
    public Flight selectBestFlight(List<Flight> flights) {
        // Sort flights by price in ascending order (lowest price first)
        return Collections.min(flights, new Comparator<Flight>() {
            @Override
            public int compare(Flight f1, Flight f2) {
                return Double.compare(f1.getPrice(), f2.getPrice());
            }
        });
    }

    private void sendFlightFilterRequest(String departureLocation, String arrivalLocation, double averagePrice) {
        FlightFilterRequest filterRequest = new FlightFilterRequest(
                averagePrice,
                departureLocation,
                arrivalLocation,
                null, // You can refine this with a specific date range if needed
                null
        );

        // Call the API to get filtered flights
        FlightAPI flightAPI = RetrofitClient.getClient("http://192.168.1.41:5000").create(FlightAPI.class);
        Call<List<Flight>> call = flightAPI.filterFlights(filterRequest);

        call.enqueue(new Callback<List<Flight>>() {
            @Override
            public void onResponse(Call<List<Flight>> call, Response<List<Flight>> response) {


                flights = response.body();
                bestFlight = selectBestFlight(flights);

            }

            @Override
            public void onFailure(Call<List<Flight>> call, Throwable t) {
                Log.e("bestChoiceActivity", "Flight request failed: " + t.getMessage());
            }
        });
    }
    private void analyzeHotelPreferences(String destination, String fullAirPort, int averageRestaurants, int averageAttractions) {
        double maxHotelCost = Double.MIN_VALUE; // Start with a very low value
        double minRating = Double.MAX_VALUE; // Start with a very high value
        int hotelCount = 0;
        int cabinCount = 0;
        int hotelPreferenceCount = 0;
        double maxKmFromCity = Double.MIN_VALUE; // Start with a very low value

        for (Trip trip : tripList) {
            // Only consider trips where the user stayed at the destination we are traveling to
            if (trip.getSelectedFlight().getArrival().equals(fullAirPort)) {
                String accommodationType = trip.getSelectedHotel().getType();
                double hotelPrice = trip.getSelectedHotel().getPricePerNight();
                double hotelRating = trip.getSelectedHotel().getRating();
                double kmFromCity = trip.getSelectedHotel().getDistanceFromCenterKm();

                // Update the max hotel cost
                if (hotelPrice > maxHotelCost) {
                    maxHotelCost = hotelPrice;
                }

                // Update the min rating
                if (hotelRating < minRating) {
                    minRating = hotelRating;
                }

                // Update the max distance from the city
                if (kmFromCity > maxKmFromCity) {
                    maxKmFromCity = kmFromCity;
                }

                // Count accommodation preferences
                if ("cabbin".equalsIgnoreCase(accommodationType)) {
                    cabinCount++;
                } else if ("hotel".equalsIgnoreCase(accommodationType)) {
                    hotelPreferenceCount++;
                }

                hotelCount++;
            }
        }

        // Handle case where no valid hotels were found
        if (hotelCount == 0) {
            maxHotelCost = 0.0;
            minRating = 0.0;
            maxKmFromCity = 0.0;
        }

        String preferredAccommodationType = (cabinCount > hotelPreferenceCount) ? "cabbin" : "hotel";

        // Send request to get the hotels
        sendHotelFilterRequest(destination, maxHotelCost, minRating, maxKmFromCity, preferredAccommodationType, fullAirPort, averageRestaurants, averageAttractions);
    }

    private void sendHotelFilterRequest(String location, double averagePrice, double rating, double kmFromCity, String hotelOrCabin, String mostCommonArrival, int averageRestaurants, int averageAttractions) {
        HotelFilterRequest filterRequest = new HotelFilterRequest(
                averagePrice,
                location,
                rating,
                kmFromCity,
                hotelOrCabin
        );

        // Call the API to get filtered hotels
        HotelAPI hotelAPI = RetrofitClient.getClient("http://192.168.1.41:5000").create(HotelAPI.class);
        Call<List<Hotel>> call = hotelAPI.filterHotels(filterRequest);

        call.enqueue(new Callback<List<Hotel>>() {
            @Override
            public void onResponse(Call<List<Hotel>> call, Response<List<Hotel>> response) {
                hotels = response.body();
                returnHotel = getBestHotel(hotels);
                analyzeRestaurantPreferences(averageRestaurants, mostCommonArrival, averageAttractions, location);


                // Handle the hotel list (e.g., display it or use it for recommendations)
            }

            @Override
            public void onFailure(Call<List<Hotel>> call, Throwable t) {
                Log.e("bestChoiceActivity", "Hotel request failed: " + t.getMessage());
            }
        });
    }
    // Sort hotels and rank them based on price, rating, and distance from the city center
    public static Hotel getBestHotel(List<Hotel> hotels) {
        // Create a copy of the original list
        List<Hotel> byPrice = new ArrayList<>(hotels);
        List<Hotel> byRating = new ArrayList<>(hotels);
        List<Hotel> byDistance = new ArrayList<>(hotels);

        // Sort by price (ascending, lowest price first)
        Collections.sort(byPrice, new Comparator<Hotel>() {
            @Override
            public int compare(Hotel h1, Hotel h2) {
                return Double.compare(h1.getPricePerNight(), h2.getPricePerNight());
            }
        });

        // Sort by rating (descending, highest rating first)
        Collections.sort(byRating, new Comparator<Hotel>() {
            @Override
            public int compare(Hotel h1, Hotel h2) {
                return Double.compare(h2.getRating(), h1.getRating());
            }
        });

        // Sort by distance from city center (ascending, closest first)
        Collections.sort(byDistance, new Comparator<Hotel>() {
            @Override
            public int compare(Hotel h1, Hotel h2) {
                return Double.compare(h1.getDistanceFromCenterKm(), h2.getDistanceFromCenterKm());
            }
        });

        // Create a map to store the combined rank of each hotel
        Map<Hotel, Integer> combinedRanks = new HashMap<>();

        // Rank based on price
        for (int i = 0; i < byPrice.size(); i++) {
            Hotel hotel = byPrice.get(i);
            combinedRanks.put(hotel, i); // Rank is the index (0 is the best)
        }

        // Rank based on rating and add to the existing rank
        for (int i = 0; i < byRating.size(); i++) {
            Hotel hotel = byRating.get(i);
            combinedRanks.put(hotel, combinedRanks.get(hotel) + i); // Add rating rank to existing rank
        }

        // Rank based on distance from city center and add to the existing rank
        for (int i = 0; i < byDistance.size(); i++) {
            Hotel hotel = byDistance.get(i);
            combinedRanks.put(hotel, combinedRanks.get(hotel) + i); // Add distance rank to existing rank
        }

        // Sort hotels based on their combined rank
        List<Hotel> sortedByCombinedRank = new ArrayList<>(hotels);
        Collections.sort(sortedByCombinedRank, new Comparator<Hotel>() {
            @Override
            public int compare(Hotel h1, Hotel h2) {
                return Integer.compare(combinedRanks.get(h1), combinedRanks.get(h2)); // Lower combined rank is better
            }
        });

        // Return the hotel with the lowest combined rank (best hotel)
        return sortedByCombinedRank.get(0); // The best hotel is the first in the sorted list
    }
    private void analyzeRestaurantPreferences(int averageRestaurants, String fullAirPort, int averageAttractions, String destination) {
        List<String> locationList = new ArrayList<>();
        List<String> cuisineList = new ArrayList<>();
        int maxAverageCost = 0;
        float minRating = Float.MAX_VALUE;

        // Loop through the user's trips to gather restaurant preferences
        for (Trip trip : tripList) {
            // Only consider trips where the user stayed at the destination we are analyzing
            if (trip.getSelectedFlight().getArrival().equals(fullAirPort)) {

                // Loop through the restaurants the user visited during the trip
                for (Restaurant restaurant : trip.getSelectedRestaurants()) {
                    String cuisine = restaurant.getCuisine();
                    String location = restaurant.getLocation();
                    int averageCost = restaurant.getAverageCost();
                    float rating = restaurant.getRating();

                    // Add the cuisine to the cuisine list if it's not already present
                    if (!cuisineList.contains(cuisine)) {
                        cuisineList.add(cuisine);
                    }
                    if (!locationList.contains(location)) {
                        locationList.add(location);
                    }

                    // Track the maximum average cost
                    if (averageCost > maxAverageCost) {
                        maxAverageCost = averageCost;
                    }

                    // Track the minimum rating
                    if (rating < minRating) {
                        minRating = rating;
                    }
                }
            }
        }

        // If no restaurants were found in the trips, set a default value for minRating
        if (minRating == Float.MAX_VALUE) {
            minRating = 0.0f; // Set default rating if no restaurants have been found
        }

        // Send request to get the restaurants
        sendRestaurantFilterRequest(locationList, cuisineList, maxAverageCost, minRating, averageRestaurants, fullAirPort, averageAttractions);
    }

    private void sendRestaurantFilterRequest(List<String> locationList, List<String> cuisineList, int averageCost, float rating, int averageRestaurants, String fullAirPort, int averageAttractions) {
        List<String> cityList = new ArrayList<>(); // To store extracted city names

        for (String location : locationList) {
            if (location != null && location.contains(",")) {
                String city = location.split(",")[0].trim(); // Get the city name
                cityList.add(city); // Add the extracted city name to cityList
            }
        }
        RestaurantFilterRequest filterRequest = new RestaurantFilterRequest(
                cityList,
                cuisineList,
                averageCost,
                rating
        );

        // Call the API to get filtered restaurants
        RestaurantAPI restaurantAPI = RetrofitClient.getClient("http://192.168.1.41:5000").create(RestaurantAPI.class);
        Call<List<Restaurant>> call = restaurantAPI.getFilteredRestaurants(filterRequest);

        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {

                restaurants = response.body();
                returnedRestaurants = getBestRestaurants(restaurants, (int)averageRestaurants);
                analyzeAttractionPreferences(averageAttractions, fullAirPort);

            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.e("bestChoiceActivity", "Restaurant request failed: " + t.getMessage());
            }
        });
    }
    // Sort restaurants and rank them based on rating and average cost
    public static ArrayList<Restaurant> getBestRestaurants(List<Restaurant> restaurants, int x) {
        if (restaurants == null || restaurants.isEmpty()) {
            return new ArrayList<>(); // Return an empty ArrayList if there are no restaurants
        }

        // Create a copy of the original list
        List<Restaurant> byRating = new ArrayList<>(restaurants);
        List<Restaurant> byCost = new ArrayList<>(restaurants);

        // Sort by rating (descending, highest rating first)
        Collections.sort(byRating, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant r1, Restaurant r2) {
                return Float.compare(r2.getRating(), r1.getRating()); // Highest rating first
            }
        });

        // Sort by cost (ascending, lowest cost first)
        Collections.sort(byCost, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant r1, Restaurant r2) {
                return Integer.compare(r1.getAverageCost(), r2.getAverageCost()); // Lowest cost first
            }
        });

        // Create a map to store the combined rank of each restaurant
        Map<Restaurant, Integer> combinedRanks = new HashMap<>();

        // Rank based on rating
        for (int i = 0; i < byRating.size(); i++) {
            Restaurant restaurant = byRating.get(i);
            combinedRanks.put(restaurant, i); // Rank is the index (0 is the best)
        }

        // Rank based on cost and add to the existing rank
        for (int i = 0; i < byCost.size(); i++) {
            Restaurant restaurant = byCost.get(i);
            combinedRanks.put(restaurant, combinedRanks.get(restaurant) + i); // Add cost rank to existing rank
        }

        // Sort restaurants based on their combined rank
        List<Restaurant> sortedByCombinedRank = new ArrayList<>(restaurants);
        Collections.sort(sortedByCombinedRank, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant r1, Restaurant r2) {
                return Integer.compare(combinedRanks.get(r1), combinedRanks.get(r2)); // Lower combined rank is better
            }
        });

        // Ensure we don't attempt to access a sublist with a size larger than the list itself
        int toIndex = Math.min(x, sortedByCombinedRank.size());

        // Create a new ArrayList from the sublist and return it
        return new ArrayList<>(sortedByCombinedRank.subList(0, toIndex));
    }
    private void analyzeAttractionPreferences(int averageAttractions, String fullAirPort) {
        List<String> locationList = new ArrayList<>();
        List<String> attractionTypeList = new ArrayList<>();
        double maxAverageCost = 0;
        float minRating = Float.MAX_VALUE;
        int kidFriendlyYesCount = 0;
        int kidFriendlyNoCount = 0;

        // Loop through the user's trips to gather attraction preferences
        for (Trip trip : tripList) {
            // Only consider trips where the user stayed at the destination we are analyzing
            if (trip.getSelectedFlight().getArrival().equals(fullAirPort)) {

                // Loop through the attractions the user visited during the trip
                for (Attraction attraction : trip.getSelectedAttractions()) {
                    String attractionType = attraction.getAttraction();
                    String location = attraction.getLocation();
                    double averageCost = attraction.getAverageCost();
                    float rating = attraction.getRating();
                    String kidFriendly = attraction.getKidFriendly();

                    if (!locationList.contains(location)) {
                        locationList.add(location);
                    }
                    // Add the attraction type to the list if it's not already present
                    if (!attractionTypeList.contains(attractionType)) {
                        attractionTypeList.add(attractionType);
                    }

                    // Track the maximum average cost
                    if (averageCost > maxAverageCost) {
                        maxAverageCost = averageCost;
                    }

                    // Track the minimum rating
                    if (rating < minRating) {
                        minRating = rating;
                    }

                    // Count "kid-friendly" occurrences
                    if (kidFriendly.equalsIgnoreCase("yes")) {
                        kidFriendlyYesCount++;
                    } else if (kidFriendly.equalsIgnoreCase("no")) {
                        kidFriendlyNoCount++;
                    }
                }
            }
        }

        // If no attractions were found in the trips, set a default value for minRating
        if (minRating == Float.MAX_VALUE) {
            minRating = 0.0f; // Set default rating if no attractions have been found
        }

        // Determine the majority kid-friendly preference
        String kidFriendly = kidFriendlyYesCount > kidFriendlyNoCount ? "yes" : "no";

        // Send request to get the attractions
        sendAttractionFilterRequest(locationList, attractionTypeList, maxAverageCost, minRating, kidFriendly, averageAttractions);
    }

    private void sendAttractionFilterRequest(List<String> locationList, List<String> attractionTypeList, double averageCost, float rating, String kidFriendly, int averageAttractions) {
        AttractionFilterRequest filterRequest = new AttractionFilterRequest(
                locationList,
                attractionTypeList,
                (int)averageCost,
                rating,
                kidFriendly
        );

        // Call the API to get filtered attractions
        AttractionAPI attractionAPI = RetrofitClient.getClient("http://192.168.1.41:5000").create(AttractionAPI.class);
        Call<List<Attraction>> call = attractionAPI.getFilteredAttractions(filterRequest);

        call.enqueue(new Callback<List<Attraction>>() {
            @Override
            public void onResponse(Call<List<Attraction>> call, Response<List<Attraction>> response) {
                attractions = response.body();
                returnedAttractions = getBestAttractions(attractions, averageAttractions);
                createTripRecommendationsPartTwo();

            }

            @Override
            public void onFailure(Call<List<Attraction>> call, Throwable t) {
                Log.e("bestChoiceActivity", "Attraction request failed: " + t.getMessage());
            }
        });
    }

    // Sort attractions and rank them based on rating and average cost.
    public static List<Attraction> getBestAttractions(List<Attraction> attractions, int x) {
        // Create a copy of the original list
        List<Attraction> byRating = new ArrayList<>(attractions);
        List<Attraction> byCost = new ArrayList<>(attractions);

        // Sort by rating (descending, highest rating first)
        Collections.sort(byRating, new Comparator<Attraction>() {
            @Override
            public int compare(Attraction a1, Attraction a2) {
                return Float.compare(a2.getRating(), a1.getRating()); // Highest rating first
            }
        });

        // Sort by cost (ascending, lowest cost first)
        Collections.sort(byCost, new Comparator<Attraction>() {
            @Override
            public int compare(Attraction a1, Attraction a2) {
                return Double.compare(a1.getAverageCost(), a2.getAverageCost()); // Lowest cost first
            }
        });

        // Create a map to store the combined rank of each attraction
        Map<Attraction, Integer> combinedRanks = new HashMap<>();

        // Rank based on rating
        for (int i = 0; i < byRating.size(); i++) {
            Attraction attraction = byRating.get(i);
            combinedRanks.put(attraction, i); // Rank is the index (0 is the best)
        }

        // Rank based on cost and add to the existing rank
        for (int i = 0; i < byCost.size(); i++) {
            Attraction attraction = byCost.get(i);
            combinedRanks.put(attraction, combinedRanks.get(attraction) + i); // Add cost rank to existing rank
        }

        // Sort attractions based on their combined rank
        List<Attraction> sortedByCombinedRank = new ArrayList<>(attractions);
        Collections.sort(sortedByCombinedRank, new Comparator<Attraction>() {
            @Override
            public int compare(Attraction a1, Attraction a2) {
                return Integer.compare(combinedRanks.get(a1), combinedRanks.get(a2)); // Lower combined rank is better
            }
        });

        // Return the top x attractions based on combined ranking
        return sortedByCombinedRank.subList(0, Math.min(x, sortedByCombinedRank.size()));
    }
}