package com.example.finalproject.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalproject.R;
import com.example.finalproject.items.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {
    private List<Restaurant> restaurantList;
    private List<Restaurant> selectedRestaurants = new ArrayList<>();
    private Context context;

    public RestaurantListAdapter(Context context, List<Restaurant> restaurantList) {
        this.context = context;
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.restaurant_layout, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);

        // Set restaurant details
        holder.name.setText(restaurant.getName());
        holder.type.setText("Type: " + restaurant.getCuisine());
        holder.location.setText("Location: " + restaurant.getLocation());
        holder.rating.setText("Rating: " + restaurant.getRating() + " stars");
        holder.price.setText(restaurant.getAverageCost() + "$");

        String imageURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSgxXwf-aqJyL94fRvQSwBc90U-u5wMMFFl3A&s";
        Glide.with(context)
                .load(imageURL)
                .placeholder(R.drawable.restaurant_default)  // Optional placeholder while loading
                .error(R.drawable.restaurant_default)    // Optional error placeholder
                .into(holder.profileImg);

        // Set background color based on selection state
        if (selectedRestaurants.contains(restaurant)) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFE3CA"));
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);  // Default white
        }

        // Set OnClickListener to handle selection/deselection
        holder.itemView.setOnClickListener(v -> {
            if (selectedRestaurants.contains(restaurant)) {
                selectedRestaurants.remove(restaurant);
                holder.itemView.setBackgroundColor(Color.WHITE);  // Deselect
            } else {
                selectedRestaurants.add(restaurant);
                holder.itemView.setBackgroundColor(Color.parseColor("#FFE3CA"));  // Select
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    // Expose the selected restaurants
    public List<Restaurant> getSelectedRestaurants() {
        return selectedRestaurants;
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, rating, type, price;
        ImageView profileImg;

        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.restaurant_name);
            type = itemView.findViewById(R.id.type);
            location = itemView.findViewById(R.id.location);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
            profileImg = itemView.findViewById(R.id.profileImg);
        }
    }
}
