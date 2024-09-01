package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class questionnaireActivity extends AppCompatActivity {

    private EditText destinationInput;
    private EditText minBudgetInput;
    private EditText maxBudgetInput;
    private Spinner locationPreferenceSpinner;
    private Spinner tripTypeSpinner;
    private EditText startDateInput;
    private EditText endDateInput;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        // Initialize the input fields and buttons
        destinationInput = findViewById(R.id.destinationInput);
        minBudgetInput = findViewById(R.id.minBudgetInput);
        maxBudgetInput = findViewById(R.id.maxBudgetInput);
        locationPreferenceSpinner = findViewById(R.id.locationPreferenceSpinner);
        tripTypeSpinner = findViewById(R.id.tripTypeSpinner);
        startDateInput = findViewById(R.id.startDateInput);
        endDateInput = findViewById(R.id.endDateInput);
        submitButton = findViewById(R.id.submitButton);

        // Set up the spinners with options
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this,
                R.array.location_preferences, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationPreferenceSpinner.setAdapter(locationAdapter);

        ArrayAdapter<CharSequence> tripTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.trip_types, android.R.layout.simple_spinner_item);
        tripTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tripTypeSpinner.setAdapter(tripTypeAdapter);

        // Set a click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values
                String destination = destinationInput.getText().toString();
                String minBudget = minBudgetInput.getText().toString();
                String maxBudget = maxBudgetInput.getText().toString();
                String locationPreference = locationPreferenceSpinner.getSelectedItem().toString();
                String tripType = tripTypeSpinner.getSelectedItem().toString();
                String startDate = startDateInput.getText().toString();
                String endDate = endDateInput.getText().toString();

                // You can add validation here if needed
                if (destination.isEmpty() || minBudget.isEmpty() || maxBudget.isEmpty() ||
                        startDate.isEmpty() || endDate.isEmpty()) {
                    Toast.makeText(questionnaireActivity.this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Process the data (e.g., save it, send it to another activity)
                    Toast.makeText(questionnaireActivity.this, "Trip preferences saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
