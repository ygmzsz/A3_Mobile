/*
File          : MainActivity.java
Project       : PROG3510 - Assign-02:
Programmer    : Ygnacio Maza Sanchez and Dionisio Estupin III
File Version  : 2025-03-16
Description   : Android Trip Planner App with enhanced functionality for Assignment 2.
 */

package com.example.lecture4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private EditText cityInput;
    private Spinner countrySpinner;
    private DatePicker startDatePicker, endDatePicker;
    private EditText peopleCountInput;
    private Button nextButton;
    private Button searchButton;
    private Button viewTripsButton;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Trip Planner");

        // Initialize executor service for background tasks
        executorService = Executors.newSingleThreadExecutor();

        // Initialize UI components
        cityInput = findViewById(R.id.cityInput);
        countrySpinner = findViewById(R.id.countrySpinner);
        startDatePicker = findViewById(R.id.startDatePicker);
        endDatePicker = findViewById(R.id.endDatePicker);
        peopleCountInput = findViewById(R.id.peopleCountInput);
        nextButton = findViewById(R.id.nextButton);
        searchButton = findViewById(R.id.searchButton);
        viewTripsButton = findViewById(R.id.viewTripsButton);

        // Set up the country spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.country_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);

        // Set up Next button click listener
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    saveTripData();
                }
            }
        });

        // Set up Search button click listener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = cityInput.getText().toString();
                if (!city.isEmpty()) {
                    Intent searchIntent = new Intent(Intent.ACTION_VIEW);
                    searchIntent.setData(Uri.parse("https://www.google.com/search?q=travel+to+" + city));
                    startActivity(searchIntent);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a city first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up View Trips button click listener
        viewTripsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Trip History feature coming soon", Toast.LENGTH_SHORT).show();
                // We've removed the reference to TripListActivity
            }
        });
    }

    private boolean validateInputs() {
        if (cityInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter a city", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (peopleCountInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter number of people", Toast.LENGTH_SHORT).show();
            return false;
        }

        Calendar startDate = Calendar.getInstance();
        startDate.set(
                startDatePicker.getYear(),
                startDatePicker.getMonth(),
                startDatePicker.getDayOfMonth()
        );

        Calendar endDate = Calendar.getInstance();
        endDate.set(
                endDatePicker.getYear(),
                endDatePicker.getMonth(),
                endDatePicker.getDayOfMonth()
        );

        if (endDate.before(startDate)) {
            Toast.makeText(this, "End date cannot be before start date", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveTripData() {
        final String city = cityInput.getText().toString();
        final String country = countrySpinner.getSelectedItem().toString();
        final String startDate = startDatePicker.getDayOfMonth() + "/" + (startDatePicker.getMonth() + 1) + "/" + startDatePicker.getYear();
        final String endDate = endDatePicker.getDayOfMonth() + "/" + (endDatePicker.getMonth() + 1) + "/" + endDatePicker.getYear();
        final String peopleCount = peopleCountInput.getText().toString();

        // Use executor service to handle background tasks
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // Save trip data to file
                boolean fileSuccess = saveToFile(city, country, startDate, endDate, peopleCount);

                // Update UI on main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (fileSuccess) {
                            // Start Activity2 with trip details
                            Intent intent = new Intent(MainActivity.this, Activity2.class);
                            intent.putExtra("city", city);
                            intent.putExtra("country", country);
                            intent.putExtra("startDate", startDate);
                            intent.putExtra("endDate", endDate);
                            intent.putExtra("peopleCount", peopleCount);
                            startActivity(intent);
                        } else {
                            Toast.makeText(
                                    MainActivity.this,
                                    "Error saving trip to file",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                });
            }
        });
    }

    private boolean saveToFile(String city, String country, String startDate, String endDate, String peopleCount) {
        try {
            // Save trip data to file
            File file = new File(getApplicationContext().getFilesDir(), "trips.txt");
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.append(city + "," + country + "," + startDate + "," + endDate + "," + peopleCount + "\n");
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mymenu, menu);
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_help) {
            // Open help website
            Intent helpIntent = new Intent(Intent.ACTION_VIEW);
            helpIntent.setData(Uri.parse("https://www.tourismwebsite.com/help"));
            startActivity(helpIntent);
            return true;
        } else if (id == R.id.menu_call) {
            // Call customer service
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:1800TRIPHELP"));
            startActivity(callIntent);
            return true;
        } else if (id == R.id.menu_settings) {
            Toast.makeText(this, "Settings coming soon", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown executor service
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}