package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.api.UsersApiToken;
import com.example.finalproject.api.WebServiceAPI;
import com.example.finalproject.items.UserToGet;
import com.example.finalproject.items.logInSave;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WelcomeActivity extends AppCompatActivity {
    TextView hello_name;
    ImageView chat;
    ImageView profile;
    Button new_trip;
    Button my_trips;
    private WebServiceAPI webServiceAPI;
    private Retrofit retrofit;

    private UsersApiToken user;


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
        String username = globalVars.username;
        String password = globalVars.password;
        String[] displayName = {null};
        user = new UsersApiToken();
        retrofit = new Retrofit.Builder()
                .baseUrl(globalVars.server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
        Callback<ResponseBody> callbackForGetUserInfo = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (!response.isSuccessful()) {
                        Toast.makeText(WelcomeActivity.this, "Incorrect username and/or password", Toast.LENGTH_LONG).show();
                    } else {
                        String token = response.body().string();
                        String authorizationHeader = "Bearer " + token;
                        //logInSaveDao.insert(new logInSave(globalVars.username,globalVars.password));
                        String funcUserName = username;
                        Call<UserToGet> call2 = webServiceAPI.getUser(authorizationHeader, funcUserName);
                        call2.enqueue(new Callback<UserToGet>() {
                            @Override
                            public void onResponse(Call<UserToGet> call2, Response<UserToGet> response2) {
                                UserToGet serverReturn = response2.body();
                                displayName[0] = serverReturn.getDisplayName();
                                hello_name.setText("Hello " + displayName[0] + ",");
                            }

                            @Override
                            public void onFailure(Call<UserToGet> call2, Throwable t) {
                                Toast.makeText(WelcomeActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(WelcomeActivity.this, "problem with connecting to the server", Toast.LENGTH_LONG).show();
            }
        };
        user.getUser(username, password, callbackForGetUserInfo);

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
