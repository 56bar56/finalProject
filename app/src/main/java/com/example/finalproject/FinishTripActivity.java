package com.example.finalproject;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.finalproject.adapters.ContactsList2Adapter;
import com.example.finalproject.adapters.RestaurantListAdapter;
import com.example.finalproject.api.TripAPI;
import com.example.finalproject.api.UsersApiToken;
import com.example.finalproject.api.WebServiceAPI;
import com.example.finalproject.items.Attraction;
import com.example.finalproject.items.Contact;
import com.example.finalproject.items.Flight;
import com.example.finalproject.items.Hotel;
import com.example.finalproject.items.Restaurant;
import com.example.finalproject.items.Trip;

import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import retrofit2.Callback;
import retrofit2.Response;

public class FinishTripActivity extends AppCompatActivity implements ContactsList2Adapter.SelectionCountUpdateListener {

    private WebServiceAPI webServiceAPI;
    private PostDao postDao;
    private AppDB db;
    private UsersApiToken user;
    private Retrofit retrofit;

    private ImageView backButton;
    private RecyclerView contactRecyclerView;
    private List<Contact> contactList;
    private int maxSelections;
    private ContactsList2Adapter adapter;
    private TextView selectionCountText;
    private Button doneButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_trip);

        // Taking care of back button
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        contactRecyclerView = findViewById(R.id.contacts_list);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectionCountText = findViewById(R.id.selection_count_text);
        String peopleNumber = getIntent().getStringExtra("peopleNumber");
        maxSelections = Integer.parseInt(peopleNumber) - 1;
        selectionCountText.setText("Selections remaining: " + maxSelections);
        doneButton = findViewById(R.id.done_button);

        Flight selectedFlight = (Flight) getIntent().getSerializableExtra("selectedFlight");
        Flight selectedReturnedFlight = (Flight) getIntent().getSerializableExtra("selectedReturnedFlight");
        Hotel selectedHotel = (Hotel) getIntent().getSerializableExtra("selectedHotel");
        ArrayList<Restaurant> returnedRestaurants = (ArrayList<Restaurant>) getIntent().getSerializableExtra("selectedRestaurants");
        ArrayList<Attraction> returnedAttractions = (ArrayList<Attraction>) getIntent().getSerializableExtra("selectedAttractions");

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "PostsDB").allowMainThreadQueries().build();
        postDao = db.postDao();
        user = new UsersApiToken();
        retrofit = new Retrofit.Builder()
                .baseUrl(globalVars.server)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);

        // Sample data: List of contacts
        contactList = new ArrayList<>();
        List<DbObject> l = postDao.index();
        for (DbObject dbobj : l) {
            contactList.add(dbobj.getContactName());
        }
        if(contactList.isEmpty()){
            sendTripToServer(selectedFlight, selectedReturnedFlight, selectedHotel, returnedRestaurants, returnedAttractions, globalVars.username);
            Intent intent = new Intent(FinishTripActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }

        adapter = new ContactsList2Adapter(this, contactList, maxSelections, this);  // Pass 'this' as the listener
        contactRecyclerView.setAdapter(adapter);


        // Handle the Done button click to navigate back to the welcome page
        doneButton.setOnClickListener(v -> {
            for(Contact contact: adapter.getSelectedContacts()){
                String username = contact.getUser().getUsername();
                sendTripToServer(selectedFlight, selectedReturnedFlight, selectedHotel, returnedRestaurants, returnedAttractions, username);
            }
            sendTripToServer(selectedFlight, selectedReturnedFlight, selectedHotel, returnedRestaurants, returnedAttractions, globalVars.username);
            Intent intent = new Intent(FinishTripActivity.this, WelcomeActivity.class);
            startActivity(intent);
        });
    }


    // Update the selection count text and button state when the selection changes
    @Override
    public void onSelectionCountUpdated(int remainingSelections) {
        selectionCountText.setText("Selections remaining: " + remainingSelections);
        // Enable the done button only if the exact number of maxSelections is reached
        doneButton.setEnabled(adapter.getSelectedContacts().size() <= maxSelections);
    }

    private void sendTripToServer(Flight selectedFlight, Flight selectedReturnedFlight, Hotel selectedHotel, ArrayList<Restaurant> returnedRestaurants, List<Attraction> selectedAttractions, String username) {
        // Send a request to the server to create the trip (use Retrofit, Volley, or another library)
        // This is a placeholder to show where you would put the network request

        Trip myTrip = new Trip(selectedFlight, selectedReturnedFlight, selectedHotel, returnedRestaurants, selectedAttractions, username);
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
}
