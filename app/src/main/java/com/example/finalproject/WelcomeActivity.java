package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class WelcomeActivity extends AppCompatActivity {
    TextView hello_name;
    ImageView chat;
    ImageView profile;
    Button new_trip;
    Button my_trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        // Find views by ID
        hello_name = findViewById(R.id.hello_name);
        chat = findViewById(R.id.chat);
        profile = findViewById(R.id.profile);
        new_trip = findViewById(R.id.new_trip);
        my_trips = findViewById(R.id.my_trips);

        // Get the intent and extract the name
        //TODO fix the transition to contact page
        Intent intent = getIntent();
        String displayName = intent.getStringExtra("displayName");
        String profilePic = intent.getStringExtra("profilePic");



        // Update the TextView with the user's name
        hello_name.setText("Hello " + displayName + ",");

        // Set onClickListener for the new_trip button to go to NewTripActivity
        new_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, Flight_Preferance_Activity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for the my_trips button to go to MyTripsActivity
        my_trips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, MyTripsActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for the profile button to go to ProfileActivity
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for the chat icon to go to ContactsPageActivity
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomeActivity.this, contacts_pageActivity.class);
                if(getIntent().getStringExtra("CheckWithServer").equals("yes")) {
                    intent.putExtra("CheckWithServer","yes");

                }
                else {
                    intent.putExtra("CheckWithServer","not");
                }
                startActivity(intent);
            }
        });
    }
}
