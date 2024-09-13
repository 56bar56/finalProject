package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.finalproject.adapters.ImagePagerAdapter;
import com.example.finalproject.items.Attraction;
import com.example.finalproject.items.Trip;
import com.google.android.material.tabs.TabLayout;

import java.util.Arrays;
import java.util.List;

public class TourDetailsActivity extends AppCompatActivity {

    private ImageView backButton;
    private Trip selectedTrip;
    private TextView title;
    private List<Attraction> attractions;
    private ViewPager2 imageViewPager;
    private View[] dots;
    private List<Integer> imageList;
    private int previousPosition = -1; // Track the current image


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour_details_page);

        // Taking care of back button
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });

        selectedTrip = (Trip) getIntent().getSerializableExtra("selectedTrip");

        // Set title
        title = findViewById(R.id.title_destination);
        title.setText(extractCityCountry(selectedTrip.getSelectedFlight().getArrival()));

        // Take care image_info RelativeLayout
        //attractions = selectedTrip.getSelectedAttractions();



        // Initialize images
        imageList = Arrays.asList(R.drawable.top3number2, R.drawable.top3number3, R.drawable.ashim_d_silva_ihjohaud8ry_unsplash_1_ek1,
                R.drawable.ashim_d_silva_ihjohaud8ry_unsplash_1, R.drawable.top3number3);

        imageViewPager = findViewById(R.id.image_view_pager);

        ImagePagerAdapter adapter = new ImagePagerAdapter(this, imageList);
        imageViewPager.setAdapter(adapter);

        // Initialize dots
        dots = new View[] {
                findViewById(R.id.dot1), findViewById(R.id.dot2),
                findViewById(R.id.dot3), findViewById(R.id.dot4), findViewById(R.id.dot5)
        };


        // Connect TabLayout with ViewPager2

        // Set a PageTransformer to handle page change updates
        imageViewPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                int currentItem = imageViewPager.getCurrentItem();
                if (currentItem != previousPosition) {
                    updateDots(currentItem % imageList.size());
                    updateImageDetails(currentItem % imageList.size());
                    previousPosition = currentItem;
                }
            }
        });

    }


    private void updateDots(int position) {
        // Reset all dots
        for (int i = 0; i < dots.length; i++) {
            if (i == position) {
                dots[i].setBackgroundResource(R.drawable.rectangle_9_shape);  // Wide circle
                dots[i].getLayoutParams().width = 20;
            } else {
                dots[i].setBackgroundResource(R.drawable.ellipse_15_shape);  // Normal dot
                dots[i].getLayoutParams().width = 5;
            }
        }

    }

    private void updateImageDetails(int position) {
        TextView location = findViewById(R.id.location);
        TextView attractionName = findViewById(R.id.attraction_name);
        TextView price = findViewById(R.id.price);
        TextView celsius = findViewById(R.id.celsius);

        switch (position) {
            case 0:
                location.setText("Bangkok, Thailand");
                attractionName.setText("Capital of Thailand");
                price.setText("10,000$");
                celsius.setText("30°C");
                break;
            case 1:
                location.setText("Paris, France");
                attractionName.setText("Eiffel Tower");
                price.setText("12,000$");
                celsius.setText("25°C");
                break;
            case 2:
                location.setText("New York, USA");
                attractionName.setText("Statue of Liberty");
                price.setText("15,000$");
                celsius.setText("20°C");
                break;
            case 3:
                location.setText("Jerusalem, Israel");
                attractionName.setText("The Cotel");
                price.setText("20,000$");
                celsius.setText("30°C");
                break;
            case 4:
                location.setText("Amsterdam, Holland");
                attractionName.setText("Van Gogh Museum");
                price.setText("12,000$");
                celsius.setText("17°C");
                break;

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
}