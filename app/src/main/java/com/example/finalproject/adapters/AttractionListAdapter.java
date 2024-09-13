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
import com.example.finalproject.items.Attraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AttractionListAdapter extends RecyclerView.Adapter<AttractionListAdapter.AttractionViewHolder> {
    private List<Attraction> attractionList;
    private List<Attraction> selectedAttractions = new ArrayList<>();
    private Context context;
    private HashMap<String, String> mapType = new HashMap<>();
    Boolean needOnClick; // Just on Activity pages needed, on TourDetailsActivity there is no need.

    public AttractionListAdapter(Context context, List<Attraction> attractionList, boolean needOnClick) {
        this.context = context;
        this.attractionList = attractionList;
        this.needOnClick = needOnClick;
        mapType.put("historical", "Historical Sites");
        mapType.put("museum", "Museums and Art Galleries");
        mapType.put("park", "Amusement Parks");
        mapType.put("zoo", "Zoos and Aquariums");
        mapType.put("nature", "National Parks and Nature Reserves");
        mapType.put("theater", "Theaters and Live Performances");
        mapType.put("beach", "Beaches");
        mapType.put("shopping", "Shopping Districts");

    }

    @NonNull
    @Override
    public AttractionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.attraction_layout, parent, false);
        return new AttractionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttractionViewHolder holder, int position) {
        Attraction attraction = attractionList.get(position);

        // Set restaurant details
        //holder.name.setText(attraction.getName()); //TODO
        holder.type.setText("Type: " + mapType.get(attraction.getAttraction()));
        holder.location.setText("Location: " + attraction.getLocation());
        holder.rating.setText("Rating: " + attraction.getRating() + " stars");
        holder.price.setText(attraction.getAverageCost() + "$");

        String imageURL = "https://thumbor.bigedition.com/sydney-opera-house/WC10wVtQEImDSv3RDFtzyAKEwGY=/800x0/filters:quality(80)/granite-web-prod/ca/1f/ca1f9fe650954918bb5b53fd47958764.jpeg";
        Glide.with(context)
                .load(imageURL)
                .placeholder(R.drawable.attraction_default)  // Optional placeholder while loading
                .error(R.drawable.attraction_default)    // Optional error placeholder
                .into(holder.profileImg);

        // Set background color based on selection state
        if (selectedAttractions.contains(attraction)) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFE3CA"));
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);  // Default white
        }

        if(needOnClick) {
            // Set OnClickListener to handle selection/deselection
            holder.itemView.setOnClickListener(v -> {
                if (selectedAttractions.contains(attraction)) {
                    selectedAttractions.remove(attraction);
                    holder.itemView.setBackgroundColor(Color.WHITE);  // Deselect
                } else {
                    selectedAttractions.add(attraction);
                    holder.itemView.setBackgroundColor(Color.parseColor("#FFE3CA"));  // Select
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return attractionList.size();
    }

    // Expose the selected restaurants
    public List<Attraction> getSelectedAttractions() {
        return selectedAttractions;
    }

    public static class AttractionViewHolder extends RecyclerView.ViewHolder {
        TextView name, type, location, rating, price;
        ImageView profileImg;

        public AttractionViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.attraction_name);
            type = itemView.findViewById(R.id.type);
            location = itemView.findViewById(R.id.location);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
            profileImg = itemView.findViewById(R.id.profileImg);
        }
    }
}
