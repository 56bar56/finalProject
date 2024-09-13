package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private ImageView backButton;
    private Button logoutButton;
    private TextView displayName;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        // Taking care of back button
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), loginActivity.class);
            startActivity(intent);
        });

        displayName = findViewById(R.id.name);
        displayName.setText(getIntent().getStringExtra("displayName"));

        username = findViewById(R.id.username);
        username.setText(globalVars.username);
    }
}
