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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Flight_Preferance_Activity extends AppCompatActivity {

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
    private Button next_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_preferance_page);

        // Taking care of start and destination locations
        startLocation = findViewById(R.id.start_location);
        destinationLocation = findViewById(R.id.destination_location);


        List<String> flightDestinations = Arrays.asList(
                "Abu Dhabi, United Arab Emirates",
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
                "Chicago, United States",
                "Copenhagen, Denmark",
                "Cyprus",
                "Dallas, United States",
                "Doha, Qatar",
                "Dubai, United Arab Emirates",
                "Edinburgh, United Kingdom",
                "Fiji",
                "Frankfurt, Germany",
                "Geneva, Switzerland",
                "Guadalajara, Mexico",
                "Guangzhou, China",
                "Hanoi, Vietnam",
                "Helsinki, Finland",
                "Ho Chi Minh City, Vietnam",
                "Hong Kong, China",
                "Houston, United States",
                "Iceland",
                "Israel",
                "Jakarta, Indonesia",
                "Johannesburg, South Africa",
                "Kuala Lumpur, Malaysia",
                "Lagos, Nigeria",
                "Lima, Peru",
                "Lisbon, Portugal",
                "London, United Kingdom",
                "Los Angeles, United States",
                "Luxembourg",
                "Madrid, Spain",
                "Maldives",
                "Malta",
                "Manchester, United Kingdom",
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
                "New York, United States",
                "Nice, France",
                "Osaka, Japan",
                "Oslo, Norway",
                "Papua New Guinea",
                "Paris, France",
                "Porto, Portugal",
                "Rio de Janeiro, Brazil",
                "Rome, Italy",
                "San Francisco, United States",
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



                // Logging to check the values in the console
                Log.d("FlightInfo", "departureLocation: " + departureLocation);
                Log.d("FlightInfo", "arrivalLocation: " + arrivalLocation);
                Log.d("FlightInfo", "Start Date: " + departureDate + ", End Date: " + arrivalDate);
                Log.d("FlightInfo", "minimum days: " + minDay + ", maximum days: " + maxDay);
                Log.d("FlightInfo", "number of people: " + peopleNumber);
                Log.d("FlightInfo", "maxBudget: " + maxBudget);
                Log.d("FlightInfo", "isRoundedTrip: " + isRoundedTrip);
                Log.d("FlightInfo", "isAllowConnection: " + isAllowConnection);


                // Pass data to FlightsActivity using intent
                Intent intent = new Intent(Flight_Preferance_Activity.this, NewTripActivity.class); //TODO change to FlightsActivity.class
                intent.putExtra("departureLocation", departureLocation);
                intent.putExtra("arrivalLocation", arrivalLocation);
                intent.putExtra("departureDate", departureDate);
                intent.putExtra("minDay", minDay);
                intent.putExtra("maxDay", maxDay);
                intent.putExtra("peopleNumber", peopleNumber);
                intent.putExtra("maxPrice", maxBudget);
                intent.putExtra("ROUNDED_TRIP", isRoundedTrip);
                intent.putExtra("ALLOW_CONNECTION", isAllowConnection);

                startActivity(intent);

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
}