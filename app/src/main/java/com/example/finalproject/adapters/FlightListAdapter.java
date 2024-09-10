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

import com.example.finalproject.Hotel_Preferance_Activity;
import com.example.finalproject.R;
import com.example.finalproject.ReturnFlightsActivity;
import com.example.finalproject.items.Flight;

import java.util.List;

public class FlightListAdapter extends RecyclerView.Adapter<FlightListAdapter.FlightViewHolder> {
    private List<Flight> flightList;
    private Context context;
    private String days, daysMin, maxPrice;  // extra data in case we are in FlightActivity.java
    private Flight selectedFlight;  // extra data in case we are in ReturnFlightActivity.java

    public FlightListAdapter(Context context, List<Flight> flightList, String days, String daysMin, String maxPrice, Flight selectedFlight) {
        this.context = context;
        this.flightList = flightList;
        this.days = days;
        this.daysMin = daysMin;
        this.maxPrice = maxPrice;
        this.selectedFlight = selectedFlight;
    }

    @NonNull
    @Override
    public FlightViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.flight_layout, parent, false);
        return new FlightViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightViewHolder holder, int position) {
        Flight flight = flightList.get(position);

        // Set flight details
        holder.flightNumber.setText("Flight Number: " + flight.getFlightNumber());
        holder.departure.setText("Departure: " + flight.getDeparture());
        holder.arrival.setText("Arrival: " + flight.getArrival());
        holder.price.setText(flight.getPrice() + "$");

        // Logic to handle company logo or fallback text
        int logoResId = getCompanyLogoResId(flight.getCompany());
        if (logoResId != 0) {
            holder.profileImg.setImageResource(logoResId);
            holder.profileImg.setVisibility(View.VISIBLE);
            holder.fallbackText.setVisibility(View.GONE);
        } else {
            holder.profileImg.setVisibility(View.GONE);
            holder.fallbackText.setText(flight.getCompany());
            holder.fallbackText.setVisibility(View.VISIBLE);
        }

        // Set OnClickListener for each flight item
        holder.itemView.setOnClickListener(v -> {
            if(selectedFlight == null) {
                Intent intent = new Intent(context, ReturnFlightsActivity.class);
                intent.putExtra("selectedFlight", flight);  // Pass the selected flight
                intent.putExtra("tripDays", days);  // Use days from constructor
                intent.putExtra("tripDaysMin", daysMin);  // Use daysMin from constructor
                intent.putExtra("maxPrice", maxPrice);  // Use maxPrice from constructor
                context.startActivity(intent);
            }
            else {
                Intent intent = new Intent(context, Hotel_Preferance_Activity.class);
                intent.putExtra("selectedFlight", selectedFlight);  // Pass the selected outbound flight
                intent.putExtra("selectedReturnedFlight", flight);  // Pass the selected return flight
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView flightNumber, departure, arrival, price, fallbackText;
        ImageView profileImg;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            flightNumber = itemView.findViewById(R.id.flight_number);
            departure = itemView.findViewById(R.id.departure);
            arrival = itemView.findViewById(R.id.arrival);
            price = itemView.findViewById(R.id.price);
            profileImg = itemView.findViewById(R.id.profileImg);
            fallbackText = itemView.findViewById(R.id.fallbackText);
        }
    }

    // Method to get company logo resource ID
    private int getCompanyLogoResId(String company) {
        switch (company) {
            case "Emirates":
                return R.drawable.emirates_logo;
            case "Qatar Airways":
                return R.drawable.qatar_logo;
            case "United Airlines":
                return R.drawable.united_logo;
            // Add other airlines and their logos
            default:
                return 0;  // 0 means no logo found
        }
    }
}
