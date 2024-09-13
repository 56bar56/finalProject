package com.example.finalproject;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.finalproject.api.TripAPI;
import com.example.finalproject.api.UsersApiToken;
import com.example.finalproject.api.WebServiceAPI;
import com.example.finalproject.items.Attraction;
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

public class FinishTripActivity extends AppCompatActivity {

    private ListView namesListView;
    private TextView selectionCountText;
    private Button doneButton;

    private ArrayList<String> namesList;  // List of names
    private ArrayList<String> selectedItems; // List of selected names

    private int maxSelections;
    private WebServiceAPI webServiceAPI;
    private PostDao postDao;
    private AppDB db;
    private UsersApiToken user;
    private Retrofit retrofit;
    private ArrayAdapter<String> adapter; // Adapter for the ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_trip);

        namesListView = findViewById(R.id.names_list_view);
        selectionCountText = findViewById(R.id.selection_count_text);
        doneButton = findViewById(R.id.done_button);
        String peopleNumber = getIntent().getStringExtra("peopleNumber");
        maxSelections = Integer.parseInt(peopleNumber) - 1;
        selectionCountText.setText("Selections remaining: " + maxSelections);

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

        // Sample data: List of names
        namesList = new ArrayList<>();
        List<DbObject> l = postDao.index();
        for (DbObject dbobj : l) {
            namesList.add(dbobj.getContactName().getUser().getUsername());
        }
        if(namesList.isEmpty()){

            sendTripToServer(selectedFlight, selectedReturnedFlight, selectedHotel, returnedRestaurants, returnedAttractions, globalVars.username);

            Intent intent = new Intent(FinishTripActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
        selectedItems = new ArrayList<>();

        // Set up the ListView adapter
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, namesList);
        namesListView.setAdapter(adapter);
        namesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Set up ListView item click listener
        namesListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = namesList.get(position);

            // Check if the item is already selected
            if (selectedItems.contains(selectedItem)) {
                // Remove the selection
                selectedItems.remove(selectedItem);
                namesListView.setItemChecked(position, false);  // Uncheck the item
            } else {
                // Only add a new selection if under the max limit
                if (selectedItems.size() < maxSelections) {
                    selectedItems.add(selectedItem);
                    namesListView.setItemChecked(position, true);  // Check the item
                } else {
                    // Show a message if the user tries to select more than allowed
                    Toast.makeText(FinishTripActivity.this, "You can only select " + maxSelections + " items.", Toast.LENGTH_SHORT).show();
                }
            }

            // Update the remaining selections and button state
            updateSelectionCount();
        });

        // Handle the Done button click to navigate back to the welcome page
        doneButton.setOnClickListener(v -> {
            if (selectedItems.size() == maxSelections || selectedItems.size() == namesList.size()) {
                for(String usern: selectedItems){
                    sendTripToServer(selectedFlight, selectedReturnedFlight, selectedHotel, returnedRestaurants, returnedAttractions, usern);
                }
                sendTripToServer(selectedFlight, selectedReturnedFlight, selectedHotel, returnedRestaurants, returnedAttractions, globalVars.username);
                Intent intent = new Intent(FinishTripActivity.this, WelcomeActivity.class);
                startActivity(intent);
            } else {
                // Show a message if not enough people are selected
                Toast.makeText(FinishTripActivity.this, "You must select exactly " + maxSelections + " people.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Update the selection count text and button state
    private void updateSelectionCount() {
        int remainingSelections = maxSelections - selectedItems.size();
        selectionCountText.setText("Selections remaining: " + remainingSelections);

        // Enable the done button only if the exact number of maxSelections is reached
        doneButton.setEnabled(selectedItems.size() == maxSelections);
    }

    // Disable further selections in the ListView
    private void disableFurtherSelection() {
        namesListView.setEnabled(false);  // Disable the ListView to prevent more selections
    }

    private void sendTripToServer(Flight selectedFlight, Flight selectedReturnedFlight, Hotel selectedHotel, ArrayList<Restaurant> returnedRestaurants, List<Attraction> selectedAttractions, String username) {
        // Send a request to the server to create the trip (use Retrofit, Volley, or another library)
        // This is a placeholder to show where you would put the network request

        Trip myTrip = new Trip(selectedFlight, selectedReturnedFlight, selectedHotel, returnedRestaurants, selectedAttractions, username);
        TripAPI tripAPI = RetrofitClient.getClient("http://10.0.2.2:5000").create(TripAPI.class);
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
