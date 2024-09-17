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
    Boolean needOnClick; // Just on Activity pages needed, on TourDetailsActivity there is no need.

    public RestaurantListAdapter(Context context, List<Restaurant> restaurantList, boolean needOnClick) {
        this.context = context;
        this.restaurantList = restaurantList;
        this.needOnClick = needOnClick;
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

        String imageURL = chooseLink(restaurant.getCuisine());
        //String imageURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSgxXwf-aqJyL94fRvQSwBc90U-u5wMMFFl3A&s";
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

        if(needOnClick) {
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

    private String chooseLink(String cuisine) {
        switch (cuisine) {
            case "Asian":
                return "https://images.prismic.io/facility-websites/Zk56Niol0Zci9Xx0_asian_food.png?auto=format%2Ccompress&rect=0%2C28%2C1792%2C968&w=1000&h=540"; // good
            case "American":
                return "https://thumbs.dreamstime.com/b/celebration-th-july-american-food-hamburger-bacon-cheese-tomato-onion-sausages-corn-cob-flags-generative-ai-311825121.jpg"; // good
            case "Italian":
                return "https://cdn.vox-cdn.com/thumbor/9Pqb6itdo3bYKyfN7-jCKwYCrTM=/0x0:2000x1333/1200x900/filters:focal(840x507:1160x827)/cdn.vox-cdn.com/uploads/chorus_image/image/73240096/2023_04_26_Funke_031.0.jpg"; //good
            case "French":
                return "https://d2lswn7b0fl4u2.cloudfront.net/photos/pg-french-foods-1628267961.jpg"; // good
            case "Mexican":
                return "https://www.ibnbattutamall.com/uploads/blogs/intro-1674486308.jpg"; // good
            case "Mediterranean":
                return "https://www.sfoasisgrill.com/wp-content/uploads/2021/12/Mediterranean-Restaurants-1.jpg"; // good
            default:
                return "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSgxXwf-aqJyL94fRvQSwBc90U-u5wMMFFl3A&s"; // good
        }
    }
}
