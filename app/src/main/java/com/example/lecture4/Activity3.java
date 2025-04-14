/*
File          : Activity3.Java
Project       : PROG3510 - Assign-01:
Programmer    : Ygnacio Maza Sanchez and Dionisio Estupin III
File Version  : 2025-02-13
Description   : Write a Android Trip Planner App.
 */

package com.example.lecture4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Activity3 extends AppCompatActivity {

    private Button goBack;
    private TextView destinationSummary, travelSummary, flightSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        setTitle("Trip Summary");

        // Initialize views
        goBack = findViewById(R.id.goBack3);
        destinationSummary = findViewById(R.id.destinationSummary);
        travelSummary = findViewById(R.id.travelSummary);
        flightSummary = findViewById(R.id.flightSummary);

        // Get all data from Intent
        Intent intent = getIntent();
        String city = intent.getStringExtra("city");
        String country = intent.getStringExtra("country");
        String startDate = intent.getStringExtra("startDate");
        String endDate = intent.getStringExtra("endDate");
        String peopleCount = intent.getStringExtra("peopleCount");
        String budget = intent.getStringExtra("budget");
        String airline = intent.getStringExtra("airline");
        String travelClass = intent.getStringExtra("travelClass");

        // Format and display destination details
        String destinationText = String.format(
                "City: %s\nCountry: %s\n",
                city, country
        );
        destinationSummary.setText(destinationText);

        // Format and display travel details
        String travelText = String.format(
                "Travel Dates: %s to %s\nNumber of Travelers: %s\nBudget: $%s\n",
                startDate, endDate, peopleCount, budget
        );
        travelSummary.setText(travelText);

        // Format and display flight details
        String flightText = String.format(
                "Airline: %s\nTravel Class: %s\n",
                airline, travelClass
        );
        flightSummary.setText(flightText);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendBack = new Intent();
                sendBack.putExtra("data3", "Trip details confirmed!");
                setResult(RESULT_OK, sendBack);
                finish();
            }
        });
    }
}