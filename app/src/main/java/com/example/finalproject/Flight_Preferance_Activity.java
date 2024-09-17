package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.adapters.FlightListAdapter;
import com.example.finalproject.adapters.TripListAdapter;
import com.example.finalproject.api.FlightAPI;
import com.example.finalproject.api.TripAPI;
import com.example.finalproject.items.Flight;
import com.example.finalproject.items.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Flight_Preferance_Activity extends AppCompatActivity {

    private ImageView backButton;
    private AutoCompleteTextView startLocation;
    private AutoCompleteTextView destinationLocation;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private RelativeLayout[] rangeDateLayout;
    private View[] rangeDateViews;
    private TextView[] rangeDateTextViews;
    private String[] minDays = {"2", "5", "7", "15", "30"};
    private String[] maxDays = {"4", "7", "14", "30", "null"};
    private int selectedIndexDays = 1; // Store the default index of the selected option
    private RelativeLayout[] numPeopleLayout;
    private View[] numPeopleViews;
    private TextView[] numPeopleTextViews;
    private int selectedIndexNumPeople = 3; // Store the default index of the selected option
    EditText maxBudgetEditText;
    CheckBox roundedTripCheckBox;
    CheckBox allowConnectionCheckBox;
    CheckBox greedyAlgorithm;
    private List<Trip> tripList = new ArrayList<>();
    private Button next_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_preferance_page);

        // Taking care of back button
        // Set onClickListener for the back button to go to WelcomeActivity
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        // Taking care of start and destination locations
        startLocation = findViewById(R.id.start_location);
        destinationLocation = findViewById(R.id.destination_location);


        List<String> flightDestinations = Arrays.asList(
                "Abu Dhabi, UAE",
                "Addis Ababa, Ethiopia",
                "Amsterdam, Netherlands",
                "Andorra",
                "Auckland, New Zealand",
                "Bangkok, Thailand",
                "Barcelona, Spain",
                "Beijing, China",
                "Berlin, Germany",
                "Bhutan",
                "Bogota, Colombia",
                "Brisbane, Australia",
                "Brussels, Belgium",
                "Buenos Aires, Argentina",
                "Cairo, Egypt",
                "Cancun, Mexico",
                "Cape Town, South Africa",
                "Caracas, Venezuela",
                "Casablanca, Morocco",
                "Chicago, USA",
                "Copenhagen, Denmark",
                "Cyprus",
                "Dallas, USA",
                "Doha, Qatar",
                "Dubai, UAE",
                "Edinburgh, UK",
                "Fiji",
                "Frankfurt, Germany",
                "Geneva, Switzerland",
                "Guadalajara, Mexico",
                "Guangzhou, China",
                "Hanoi, Vietnam",
                "Helsinki, Finland",
                "Ho Chi Minh City, Vietnam",
                "Hong Kong, China",
                "Houston, USA",
                "Iceland",
                "Israel",
                "Jakarta, Indonesia",
                "Johannesburg, South Africa",
                "Kuala Lumpur, Malaysia",
                "Lagos, Nigeria",
                "Lima, Peru",
                "Lisbon, Portugal",
                "London, UK",
                "Los Angeles, USA",
                "Luxembourg",
                "Madrid, Spain",
                "Maldives",
                "Malta",
                "Manchester, UK",
                "Mauritius",
                "Melbourne, Australia",
                "Mexico City, Mexico",
                "Milan, Italy",
                "Monaco",
                "Montreal, Canada",
                "Munich, Germany",
                "Nairobi, Kenya",
                "Nepal",
                "New Delhi, India",
                "New York, USA",
                "Nice, France",
                "Osaka, Japan",
                "Oslo, Norway",
                "Papua New Guinea",
                "Paris, France",
                "Porto, Portugal",
                "Rio de Janeiro, Brazil",
                "Rome, Italy",
                "San Francisco, USA",
                "San Marino",
                "Sao Paulo, Brazil",
                "Santiago, Chile",
                "Seoul, South Korea",
                "Seychelles",
                "Shanghai, China",
                "Singapore",
                "Sri Lanka",
                "Stockholm, Sweden",
                "Sydney, Australia",
                "Tokyo, Japan",
                "Toronto, Canada",
                "Vancouver, Canada",
                "Venice, Italy",
                "Vienna, Austria",
                "Wellington, New Zealand",
                "Zurich, Switzerland"
        );


        // Create an ArrayAdapter to store the locations and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, flightDestinations);
        startLocation.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, flightDestinations);
        destinationLocation.setAdapter(adapter2);

        // Show all options when the user focuses on the input field without typing anything
        startLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    startLocation.showDropDown();
                }
            }
        });

        destinationLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    destinationLocation.showDropDown();
                }
            }
        });

        // Taking care of start and end date
        startDateTextView = findViewById(R.id.start_date);
        endDateTextView = findViewById(R.id.end_date);

        // Set OnClickListener to open DatePickerDialog when clicked
        startDateTextView.setOnClickListener(v -> showDatePickerDialog(startDateTextView));
        endDateTextView.setOnClickListener(v -> showDatePickerDialog(endDateTextView));


        // Taking care of travel days appearance and value
        // Initialize views
        rangeDateLayout = new RelativeLayout[] {
                findViewById(R.id.two_to_four_days1),
                findViewById(R.id.five_to_seven_days1),
                findViewById(R.id.seven_to_fourteen_days1),
                findViewById(R.id.fifteen_to_thirty_days1),
                findViewById(R.id.above_thirty_days1)
        };

        rangeDateViews = new View[] {
                findViewById(R.id.two_to_four_days2),
                findViewById(R.id.five_to_seven_days2),
                findViewById(R.id.seven_to_fourteen_days2),
                findViewById(R.id.fifteen_to_thirty_days2),
                findViewById(R.id.above_thirty_days2)
        };

        rangeDateTextViews = new TextView[] {
                ((TextView) findViewById(R.id.two_to_four_days3)),
                ((TextView) findViewById(R.id.five_to_seven_days3)),
                ((TextView) findViewById(R.id.seven_to_fourteen_days3)),
                ((TextView) findViewById(R.id.fifteen_to_thirty_days3)),
                ((TextView) findViewById(R.id.above_thirty_days3))
        };


        // Set OnClickListener for each RelativeLayout
        for (int i = 0; i < rangeDateLayout.length; i++) {
            final int index = i;
            rangeDateLayout[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateStyles(index);
                }
            });
        }

        // Taking care of number of people
        // Initialize views
        numPeopleLayout = new RelativeLayout[] {
                findViewById(R.id.one_person1),
                findViewById(R.id.two_person1),
                findViewById(R.id.three_person1),
                findViewById(R.id.four_person1),
                findViewById(R.id.five_person1),
                findViewById(R.id.six_person1),
                findViewById(R.id.seven_person1),
                findViewById(R.id.eight_person1),
                findViewById(R.id.nine_person1),
                findViewById(R.id.ten_person1),
                findViewById(R.id.eleven_person1),
                findViewById(R.id.twelve_person1),
                findViewById(R.id.thirteen_person1),
                findViewById(R.id.fourteen_person1),
                findViewById(R.id.fifteen_person1),
                findViewById(R.id.sixteen_person1)
        };

        numPeopleViews = new View[] {
                findViewById(R.id.one_person2),
                findViewById(R.id.two_person2),
                findViewById(R.id.three_person2),
                findViewById(R.id.four_person2),
                findViewById(R.id.five_person2),
                findViewById(R.id.six_person2),
                findViewById(R.id.seven_person2),
                findViewById(R.id.eight_person2),
                findViewById(R.id.nine_person2),
                findViewById(R.id.ten_person2),
                findViewById(R.id.eleven_person2),
                findViewById(R.id.twelve_person2),
                findViewById(R.id.thirteen_person2),
                findViewById(R.id.fourteen_person2),
                findViewById(R.id.fifteen_person2),
                findViewById(R.id.sixteen_person2)
        };

        numPeopleTextViews = new TextView[] {
                ((TextView) findViewById(R.id.one_person3)),
                ((TextView) findViewById(R.id.two_person3)),
                ((TextView) findViewById(R.id.three_person3)),
                ((TextView) findViewById(R.id.four_person3)),
                ((TextView) findViewById(R.id.five_person3)),
                ((TextView) findViewById(R.id.six_person3)),
                ((TextView) findViewById(R.id.seven_person3)),
                ((TextView) findViewById(R.id.eight_person3)),
                ((TextView) findViewById(R.id.nine_person3)),
                ((TextView) findViewById(R.id.ten_person3)),
                ((TextView) findViewById(R.id.eleven_person3)),
                ((TextView) findViewById(R.id.twelve_person3)),
                ((TextView) findViewById(R.id.thirteen_person3)),
                ((TextView) findViewById(R.id.fourteen_person3)),
                ((TextView) findViewById(R.id.fifteen_person3)),
                ((TextView) findViewById(R.id.sixteen_person3))
        };


        // Set OnClickListener for each RelativeLayout
        for (int i = 0; i < numPeopleLayout.length; i++) {
            final int index = i;
            numPeopleLayout[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateStyles2(index);
                }
            });
        }

        // taking care of budget, round trip, and connection
        maxBudgetEditText = findViewById(R.id.max_budget);
        roundedTripCheckBox = findViewById(R.id.rounded_trip);
        allowConnectionCheckBox = findViewById(R.id.allow_connection);

        // Taking care of greedy algorithm
        greedyAlgorithm = findViewById(R.id.greedy_algorithm);

        //taking care of clicking on next
        next_button = findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected location from the AutoCompleteTextView
                String departureLocation = startLocation.getText().toString();
                String arrivalLocation = destinationLocation.getText().toString();
                String departureDate = startDateTextView.getText().toString();
                String arrivalDate = endDateTextView.getText().toString();
                String minDay = minDays[selectedIndexDays];
                String maxDay = maxDays[selectedIndexDays];
                String peopleNumber = String.valueOf(selectedIndexNumPeople + 1);
                String maxBudget = maxBudgetEditText.getText().toString();
                String isRoundedTrip = roundedTripCheckBox.isChecked() ? "true" : "false";
                String isAllowConnection = allowConnectionCheckBox.isChecked() ? "true" : "false";

                // Input validation
                // Check if the start location and destination are the same
                if (departureLocation.isEmpty() || arrivalLocation.isEmpty()) {
                    Toast.makeText(Flight_Preferance_Activity.this, "Please enter both departure and destination locations", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (departureLocation.equals(arrivalLocation)) {
                    Toast.makeText(Flight_Preferance_Activity.this, "Start and destination locations cannot be the same", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the dates are not empty
                if (departureDate.isEmpty() || arrivalDate.isEmpty()) {
                    Toast.makeText(Flight_Preferance_Activity.this, "Please select both start and end dates", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if endDate is not before startDate
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    Calendar startDate = Calendar.getInstance();
                    Calendar endDate = Calendar.getInstance();

                    startDate.setTime(dateFormat.parse(departureDate));
                    endDate.setTime(dateFormat.parse(arrivalDate));

                    if (endDate.before(startDate)) {
                        Toast.makeText(Flight_Preferance_Activity.this, "End date cannot be before start date", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Check if max budget is empty
                if (maxBudget.isEmpty()) {
                    Toast.makeText(Flight_Preferance_Activity.this, "Please enter a maximum budget", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Logging to check the values in the console
                Log.d("FlightInfo", "departureLocation: " + departureLocation);
                Log.d("FlightInfo", "arrivalLocation: " + arrivalLocation);
                Log.d("FlightInfo", "Start Date: " + departureDate + ", End Date: " + arrivalDate);
                Log.d("FlightInfo", "minimum days: " + minDay + ", maximum days: " + maxDay);
                Log.d("FlightInfo", "number of people: " + peopleNumber);
                Log.d("FlightInfo", "maxBudget: " + maxBudget);
                Log.d("FlightInfo", "isRoundedTrip: " + isRoundedTrip);
                Log.d("FlightInfo", "isAllowConnection: " + isAllowConnection);


                if(greedyAlgorithm.isChecked()){
                    // Create filter request
                    FlightFilterRequest filterRequest = new FlightFilterRequest(
                            Double.parseDouble(maxBudget),
                            departureLocation,
                            arrivalLocation,
                            departureDate,
                            arrivalDate
                    );

                    // Fetch flights
                    fetchFlights(filterRequest, maxDay, minDay, maxBudget, isRoundedTrip, peopleNumber);
                }
                else{
                    Intent intent = new Intent(Flight_Preferance_Activity.this, FlightsActivity.class);
                    intent.putExtra("departureLocation", departureLocation);
                    intent.putExtra("arrivalLocation", arrivalLocation);
                    intent.putExtra("departureDate", departureDate);
                    intent.putExtra("arrivalDate", arrivalDate);
                    intent.putExtra("minDay", minDay);
                    intent.putExtra("maxDay", maxDay);
                    intent.putExtra("peopleNumber", peopleNumber);
                    intent.putExtra("maxPrice", maxBudget);
                    intent.putExtra("ROUNDED_TRIP", isRoundedTrip);
                    intent.putExtra("ALLOW_CONNECTION", isAllowConnection);
                    // Pass data to FlightsActivity using intent
                    startActivity(intent);
                }
            }
        });

    }

    // Function that shows the calender.
    private void showDatePickerDialog(final TextView dateTextView) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Flight_Preferance_Activity.this,  // Use the name of your activity
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Create a Calendar instance with the selected date
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);

                        // Format the date in yyyy-MM-dd
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = dateFormat.format(selectedDate.getTime());

                        // Set the formatted date in the TextView
                        dateTextView.setText(formattedDate);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    // The function changes the color of the chosen attribute. All the other attributes are se to default.
    private void updateStyles(int selectedIndex) {
        this.selectedIndexDays = selectedIndex; // Update the selected index
        for (int i = 0; i < rangeDateViews.length; i++) {
            if (i == selectedIndex) {
                rangeDateViews[i].setBackgroundResource(R.drawable.rectangle_21_shape);
                rangeDateTextViews[i].setTextColor(Color.WHITE);
            } else {
                rangeDateViews[i].setBackgroundResource(R.drawable.rectangle_24_shape);
                rangeDateTextViews[i].setTextColor(Color.BLACK);
            }
        }
    }
    private void updateStyles2(int selectedIndex) {
        this.selectedIndexNumPeople = selectedIndex; // Update the selected index
        for (int i = 0; i < numPeopleViews.length; i++) {
            if (i == selectedIndex) {
                numPeopleViews[i].setBackgroundResource(R.drawable.rectangle_21_shape);
                numPeopleTextViews[i].setTextColor(Color.WHITE);
            } else {
                numPeopleViews[i].setBackgroundResource(R.drawable.rectangle_24_shape);
                numPeopleTextViews[i].setTextColor(Color.BLACK);
            }
        }
    }

    // The greedy algorithm
    private void fetchFlights(FlightFilterRequest request, String days, String daysMin, String maxPrice, String isRoundedTrip, String peopleNumber) {
        FlightAPI flightAPI = RetrofitClient.getClient("http://192.168.1.41:5000").create(FlightAPI.class);

        Call<List<Flight>> call = flightAPI.filterFlights(request);
        call.enqueue(new Callback<List<Flight>>() {
            @Override
            public void onResponse(Call<List<Flight>> call, Response<List<Flight>> response) {
                if (!response.isSuccessful()) {
                    Log.e("FlightsActivity", "Response error: " + response.code());
                    return;
                }

                List<Flight> flightList = response.body();
                if (flightList != null) {
                    getUserTrips();
                    Flight selectedFlight = selectBestFlight(flightList, tripList, Integer.parseInt(maxPrice));
                    if (isRoundedTrip.equals("true")){
                        String returnDate = calculateReturnDate(selectedFlight.getTakeoff(), days);  // Calculate return date by adding the user's stay duration
                        String returnDateMin = calculateReturnDate(selectedFlight.getTakeoff(), daysMin);  // Calculate return date by adding the user's stay duration
                        String returnDepartureLocation = selectedFlight.getArrival();
                        String returnArrivalLocation = selectedFlight.getDeparture();

                        // Create filter request for return flights
                        FlightFilterRequest filterRequest = new FlightFilterRequest(
                                Double.parseDouble(maxPrice),  // Max price is not relevant for return flight
                                returnDepartureLocation,
                                returnArrivalLocation,
                                returnDateMin,
                                returnDate
                        );
                        fetchReturnFlights(filterRequest, selectedFlight, peopleNumber, maxPrice);
                    }

                    else{
                        Intent intent = new Intent(Flight_Preferance_Activity.this, Hotel_Preferance_Activity.class);
                        intent.putExtra("selectedFlight", selectedFlight);  // Pass the selected flight
                        intent.putExtra("peopleNumber", peopleNumber);  // Use maxPrice from constructor
                        startActivity(intent);
                    }
                }
                else{
                    Toast.makeText(Flight_Preferance_Activity.this, "No flights were found, try to change your preferences", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Flight>> call, Throwable t) {
                Log.e("FlightsActivity", "Request failed: " + t.getMessage());
            }
        });
    }

    private void fetchReturnFlights(FlightFilterRequest request, Flight selectedFlight, String peopleNumber, String maxPrice) {
        FlightAPI flightAPI = RetrofitClient.getClient("http://192.168.1.41:5000").create(FlightAPI.class);

        Call<List<Flight>> call = flightAPI.filterFlights(request);
        call.enqueue(new Callback<List<Flight>>() {
            @Override
            public void onResponse(Call<List<Flight>> call, Response<List<Flight>> response) {
                if (!response.isSuccessful()) {
                    Log.e("ReturnFlightsActivity", "Response error: " + response.code());
                    return;
                }

                List<Flight> flightList = response.body();
                if (flightList != null) {
                    Flight selectedReturnFlight = selectBestFlight(flightList, tripList, Integer.parseInt(maxPrice));
                    Intent intent = new Intent(Flight_Preferance_Activity.this, Hotel_Preferance_Activity.class);
                    intent.putExtra("selectedFlight", selectedFlight);  // Pass the selected outbound flight
                    intent.putExtra("selectedReturnedFlight", selectedReturnFlight);  // Pass the selected return flight
                    intent.putExtra("peopleNumber", peopleNumber);  // Use maxPrice from constructor
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Flight_Preferance_Activity.this, "No flights were found, try to change your preferences", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Flight>> call, Throwable t) {
                Log.e("ReturnFlightsActivity", "Request failed: " + t.getMessage());
            }
        });
    }

    // Getting all users trips
    private void getUserTrips() {
        String username = globalVars.username;

        TripAPI tripAPI = RetrofitClient.getClient("http://192.168.1.41:5000").create(TripAPI.class);
        Call<List<Trip>> call = tripAPI.getUserTrips(username);
        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(Flight_Preferance_Activity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                tripList = response.body();
            }
            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                Log.e("MyTripsActivity", "Failed to fetch trips: " + t.getMessage());
                Toast.makeText(Flight_Preferance_Activity.this, "Request failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // select the best flight with all the considerations.
    public Flight selectBestFlight(List<Flight> availableFlights, List<Trip> userTrips, int maxPrice) {
        // Calculate average price user spends on flights from past trips
        double totalSpent = 0;
        int count = 0;
        for (Trip trip : userTrips) {
            if (trip.getSelectedFlight() != null && trip.getSelectedFlight().getPrice() < maxPrice) {
                totalSpent += trip.getSelectedFlight().getPrice();
                count++;
            }
            if (trip.getSelectedReturnedFlight() != null && trip.getSelectedReturnedFlight().getPrice() < maxPrice) {
                totalSpent += trip.getSelectedReturnedFlight().getPrice();
                count++;
            }
        }
        double averageSpent = (count > 0) ? totalSpent / count : 0; // If no trips, use 0
        Map<String, Double> companyUsagePercentage = calculateCompanyUsagePercentage(userTrips);

        // Initialize variables to track the best flights
        Flight bestFlight = null;
        //Flight bestFlightWithLayover = null;
        double bestScore = Double.MAX_VALUE;
        //double bestLayoverScore = Double.MAX_VALUE;

        // Rank flights based on user's company preference, price deviation, and whether it's direct or with layover
        for (Flight flight : availableFlights) {
            double priceDeviation = Math.abs(flight.getPrice() - averageSpent);

            // Company preference score
            double companyUsageScore = companyUsagePercentage.getOrDefault(flight.getCompany(), 0.0);

            // Score calculation (smaller score is better)
            double score = priceDeviation;
            score *= (1 - companyUsageScore);

            /*
            if (flight.size == 1) { // directed flight
                score *= 0.8;
            }
            */
            Log.d("BestFlight", "flight score: " + score + "flight companyUsageScore: " + companyUsageScore + "price deviation: " + priceDeviation);

            if (score < bestScore) {
                bestScore = score;
                bestFlight = flight;
            }
        }
        return bestFlight;
    }

    public Map<String, Double> calculateCompanyUsagePercentage(List<Trip> userTrips) {
        Map<String, Integer> companyFrequency = new HashMap<>();
        int totalFlights = 0;

        // Calculate the frequency of companies from userTrips
        for (Trip trip : userTrips) {
            if (trip.getSelectedFlight() != null) {
                Flight flight = trip.getSelectedFlight();
                companyFrequency.put(flight.getCompany(), companyFrequency.getOrDefault(flight.getCompany(), 0) + 1);
                totalFlights++;
            }
            if (trip.getSelectedReturnedFlight() != null) {
                Flight flight = trip.getSelectedReturnedFlight();
                companyFrequency.put(flight.getCompany(), companyFrequency.getOrDefault(flight.getCompany(), 0) + 1);
                totalFlights++;
            }
        }

        // Convert the company frequency map to percentages
        Map<String, Double> companyUsagePercentage = new HashMap<>();
        for (Map.Entry<String, Integer> entry : companyFrequency.entrySet()) {
            double percentage = (double) entry.getValue() / totalFlights * 100; // Convert to percentage
            companyUsagePercentage.put(entry.getKey(), percentage);
        }
        return companyUsagePercentage;
    }

    private String calculateReturnDate(String departureDate, String days) {
        // Define the date format used in the input and output
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Parse the departure date string to a Date object
            Date date = sdf.parse(departureDate);

            // Create a Calendar object and set it to the parsed date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Add the number of days to the calendar
            calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(days));

            // Get the new date and format it back to a string
            Date returnDate = calendar.getTime();
            return sdf.format(returnDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}