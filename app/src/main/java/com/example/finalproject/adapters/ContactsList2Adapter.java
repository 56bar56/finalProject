package com.example.finalproject.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalproject.FinishTripActivity;
import com.example.finalproject.R;
import com.example.finalproject.items.Contact;
import com.example.finalproject.items.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class ContactsList2Adapter extends RecyclerView.Adapter<ContactsList2Adapter.Contacts2ViewHolder> {
    private List<Contact> contactsList;
    private List<Contact> selectedContacts = new ArrayList<>();
    private int maxSelections;
    private SelectionCountUpdateListener selectionCountUpdateListener;
    private Context context;

    public ContactsList2Adapter(Context context, List<Contact> contactsList, int maxSelections, SelectionCountUpdateListener selectionCountUpdateListener) {
        this.context = context;
        this.contactsList = contactsList;
        this.maxSelections = maxSelections;
        this.selectionCountUpdateListener = selectionCountUpdateListener;

    }

    @NonNull
    @Override
    public Contacts2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_layout2, parent, false);
        return new Contacts2ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Contacts2ViewHolder holder, int position) {
        Contact contact = contactsList.get(position);

        // Set restaurant details
        holder.displayName.setText(contact.getUser().getDisplayName());
        holder.username.setText(contact.getUser().getUsername());

        //TODO take care of profile image

        // Set background color based on selection state
        if (selectedContacts.contains(contact)) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFE3CA"));
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);  // Default white
        }

        // Set OnClickListener to handle selection/deselection
        holder.itemView.setOnClickListener(v -> {
            if (selectedContacts.contains(contact)) {
                selectedContacts.remove(contact);
                holder.itemView.setBackgroundColor(Color.WHITE);  // Deselect
            } else {
                if (selectedContacts.size() < maxSelections) {
                    selectedContacts.add(contact);
                    holder.itemView.setBackgroundColor(Color.parseColor("#FFE3CA"));  // Select
                }
                else {
                    // Show a message if the user tries to select more than allowed
                    Toast.makeText(context, "You can only select " + maxSelections + " items.", Toast.LENGTH_SHORT).show();
                }
            }
            // Update the selection count via the callback
            int remainingSelections = maxSelections - selectedContacts.size();
            selectionCountUpdateListener.onSelectionCountUpdated(remainingSelections);
        });

    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    // Expose the selected contacts
    public List<Contact> getSelectedContacts() {
        return selectedContacts;
    }

    public interface SelectionCountUpdateListener {
        void onSelectionCountUpdated(int remainingSelections);
    }

    public static class Contacts2ViewHolder extends RecyclerView.ViewHolder {
        TextView displayName, username;
        ImageView profileImg;

        public Contacts2ViewHolder(@NonNull View itemView) {
            super(itemView);
            displayName = itemView.findViewById(R.id.display_name);
            username = itemView.findViewById(R.id.username);
            profileImg = itemView.findViewById(R.id.profileImg);
        }
    }

}
