package com.example.finalproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FlightListAdapter extends RecyclerView.Adapter<FlightListAdapter.FlightViewHolder> {
    private List<Flight> flightList;
    private Context context;
    private String days, daysMin, maxPrice;  // extra data in case we are in FlightActivity.java
    private Flight selectedFlight;  // extra data in case we are in ReturnFlightActivity.java
    private boolean isRoundedTrip;
    Boolean needOnClick; // Just on Activity pages needed, on TourDetailsActivity there is no need.

    private String peopleNumber;


    public FlightListAdapter(Context context, List<Flight> flightList, String days, String daysMin, String maxPrice, Flight selectedFlight, boolean isRoundedTrip, boolean needOnClick, String peopleNumber) {
        this.context = context;
        this.flightList = flightList;
        this.days = days;
        this.daysMin = daysMin;
        this.maxPrice = maxPrice;
        this.selectedFlight = selectedFlight;
        this.isRoundedTrip = isRoundedTrip;
        this.needOnClick = needOnClick;
        this.peopleNumber = peopleNumber;

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
        holder.takeoff.setText("Takeoff: " + formatDateString(flight.getTakeoff()));
        holder.landing.setText("Lending: " + formatDateString(flight.getLanding()));
        // Format price as an integer
        int priceAsInt = (int) flight.getPrice();  // Cast the double price to an integer
        holder.price.setText(priceAsInt + "$");  // Display price as integer

        // Logic to handle company logo or fallback text
        int logoResId = getCompanyLogoResId(flight.getCompany());
        try {
            holder.profileImg.setImageResource(logoResId);
            holder.profileImg.setVisibility(View.VISIBLE);
            holder.fallbackText.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e("ProfileImgError", "Failed to set image resource: " + e.getMessage());
            holder.profileImg.setVisibility(View.GONE);
            holder.fallbackText.setText(flight.getCompany());
            holder.fallbackText.setVisibility(View.VISIBLE);
        }

        if(needOnClick) {
            // Set OnClickListener for each flight item
            holder.itemView.setOnClickListener(v -> {
                if (selectedFlight == null) {
                    Intent intent = new Intent(context, ReturnFlightsActivity.class);
                    intent.putExtra("selectedFlight", flight);  // Pass the selected flight
                    intent.putExtra("tripDays", days);  // Use days from constructor
                    intent.putExtra("tripDaysMin", daysMin);  // Use daysMin from constructor
                    intent.putExtra("maxPrice", maxPrice);  // Use maxPrice from constructor
                    intent.putExtra("peopleNumber", peopleNumber);  // Use maxPrice from constructor
                    if (!isRoundedTrip) {
                        intent = new Intent(context, Hotel_Preferance_Activity.class);
                        intent.putExtra("selectedFlight", flight);  // Pass the selected flight
                        intent.putExtra("tripDays", days);  // Use days from constructor
                        intent.putExtra("tripDaysMin", daysMin);  // Use daysMin from constructor
                        intent.putExtra("maxPrice", maxPrice);  // Use maxPrice from constructor
                        intent.putExtra("peopleNumber", peopleNumber);  // Use maxPrice from constructor
                    }
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, Hotel_Preferance_Activity.class);
                    intent.putExtra("selectedFlight", selectedFlight);  // Pass the selected outbound flight
                    intent.putExtra("selectedReturnedFlight", flight);  // Pass the selected return flight
                    intent.putExtra("peopleNumber", peopleNumber);  // Use maxPrice from constructor
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return flightList.size();
    }

    public static class FlightViewHolder extends RecyclerView.ViewHolder {
        TextView flightNumber, departure, arrival, takeoff, landing, price, fallbackText;
        ImageView profileImg;

        public FlightViewHolder(@NonNull View itemView) {
            super(itemView);
            flightNumber = itemView.findViewById(R.id.flight_number);
            departure = itemView.findViewById(R.id.departure);
            arrival = itemView.findViewById(R.id.arrival);
            takeoff = itemView.findViewById(R.id.takeoff);
            landing = itemView.findViewById(R.id.landing);
            price = itemView.findViewById(R.id.price);
            profileImg = itemView.findViewById(R.id.profileImg);
            fallbackText = itemView.findViewById(R.id.fallbackText);
        }
    }

    public static String formatDateString(String input) {
        // Input date format
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        // Desired output format
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yy HH:mm");

        try {
            // Parse the input date string
            Date date = inputFormat.parse(input);
            // Format the date to the desired format
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Handle error appropriately
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
            case "Delta Airlines":
                return R.drawable.delta_logo;
            case "British Airways":
                return R.drawable.british_airways_logo;
            case "Qantas":
                return R.drawable.qantas_logo;
            case "American Airlines":
                return R.drawable.american_airlines_logo;
            case "Air France":
                return R.drawable.airfrance_logo;
            case "Lufthansa":
                return R.drawable.lufthansa_logo;
            // Add other airlines and their logos
            default:
                return 0;  // 0 means no logo found
        }
    }
}
