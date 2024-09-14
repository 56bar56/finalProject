package com.example.finalproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalproject.Hotel_Preferance_Activity;
import com.example.finalproject.R;
import com.example.finalproject.ReturnFlightsActivity;
import com.example.finalproject.TourDetailsActivity;
import com.example.finalproject.items.Flight;
import com.example.finalproject.items.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripViewHolder> {
    private List<Trip> tripList;
    private List<Trip> fullTripList; // To keep the full trip list for restoring after filtering
    private Context context;

    public TripListAdapter(Context context, List<Trip> tripList) {
        this.context = context;
        this.tripList = new ArrayList<>(tripList); // Initialize with the full list
        this.fullTripList = new ArrayList<>(tripList); // Save the full list for later
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trip_layout, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        if (tripList != null && position < tripList.size()) {
            Trip trip = tripList.get(position);

            // Set flight details
            if (trip.getSelectedReturnedFlight() == null)
                holder.arrowType.setImageResource(R.drawable.arrow_icon);
            else
                holder.arrowType.setImageResource(R.drawable.double_arrow);
            holder.fromAirport.setText(extractCityCountry(trip.getSelectedFlight().getDeparture()));
            holder.toAirport.setText(extractCityCountry(trip.getSelectedFlight().getArrival()));
            String dateStart = trip.getSelectedFlight().getTakeoff().substring(0, 10);
            String dateEnd = "";
            if (trip.getSelectedReturnedFlight() != null){
                dateEnd = trip.getSelectedReturnedFlight().getLanding().substring(0, 10);
            }
            else {
                dateEnd = "None";
            }
            holder.date.setText(dateStart + " - " + dateEnd);
            if (trip.getSelectedReturnedFlight() != null) {
                holder.numDays.setText(calculateDaysBetween(dateStart, dateEnd) + " days");
            }
            else{
                holder.numDays.setText("no return flight scheduled");
            }
            //holder.numPeople.setText(trip.getNumberOfPeople() + " people"); TODO
            // Format price as an integer
            int price = (int) trip.getPriceForTrip();
            holder.price.setText(price + "$");

            String imageUrl = "https://www.torontopho.com/images/blog/2023/09/Vietnam_A_Journey_Through_History_Cuisine_and_Natural_Wonders2.jpg";
            //TODO change to the hotel img
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.top3number2)  // Optional placeholder while loading
                    .error(R.drawable.top3number2)    // Optional error placeholder
                    .into(holder.profileImg);


            // Set OnClickListener for each trip item
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, TourDetailsActivity.class);
                intent.putExtra("selectedTrip", trip);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    // Method to filter trips
    public void filter(String query) {
        if (query.isEmpty()) {
            // Restore the original list when the search query is empty
            tripList.clear();
            tripList.addAll(fullTripList);
        } else {
            List<Trip> filteredList = new ArrayList<>();
            for (Trip trip : fullTripList) {
                if (trip.getSelectedFlight().getArrival().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(trip);
                }
            }
            tripList.clear();
            tripList.addAll(filteredList); // Add filtered trips to the list
        }
        notifyDataSetChanged(); // Notify adapter about the updated data
    }


    public static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView fromAirport, toAirport, date, numDays, numPeople, price;
        ImageView arrowType, profileImg;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            fromAirport = itemView.findViewById(R.id.from_airport);
            arrowType = itemView.findViewById(R.id.arrow_type);
            toAirport = itemView.findViewById(R.id.to_airport);
            date = itemView.findViewById(R.id.date);
            numDays = itemView.findViewById(R.id.num_of_days);
            numPeople = itemView.findViewById(R.id.num_of_people);
            price = itemView.findViewById(R.id.price);
            profileImg = itemView.findViewById(R.id.profileImg);
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

    // Function gets 2 dates and return the number of days in between.
    public static long calculateDaysBetween(String string1, String string2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Extract only the date part from the strings
            String datePart1 = string1.substring(0, 10);
            String datePart2 = string2.substring(0, 10);

            // Parse the date strings
            Date date1 = sdf.parse(datePart1);
            Date date2 = sdf.parse(datePart2);

            // Calculate the difference in milliseconds
            long diffInMillies = date2.getTime() - date1.getTime();

            // Convert milliseconds to days
            long daysBetween = diffInMillies / (1000 * 60 * 60 * 24);

            return daysBetween;

        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
