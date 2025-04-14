/*
File          : Activity2.Java
Project       : PROG3510 - Assign-01:
Programmer    : Ygnacio Maza Sanchez and Dionisio Estupin III
File Version  : 2025-02-13
Description   : Write a Android Trip Planner App.
 */

package com.example.lecture4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class Activity2 extends AppCompatActivity {
    private static final String TAG = Activity2.class.getSimpleName();

    private TextView cityCountryText, datesText, peopleCountText;
    private EditText budgetInput;
    private Spinner airlineSpinner, classSpinner;
    private Button goTo3;
    private String city, country, startDate, endDate, peopleCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        setTitle("Flight Details");

        // Initialize views
        cityCountryText = findViewById(R.id.cityCountryText);
        datesText = findViewById(R.id.datesText);
        peopleCountText = findViewById(R.id.peopleCountText);
        budgetInput = findViewById(R.id.budgetInput);
        airlineSpinner = findViewById(R.id.airlineSpinner);
        classSpinner = findViewById(R.id.classSpinner);
        goTo3 = findViewById(R.id.goTo3);

        // Set up spinners
        ArrayAdapter<CharSequence> airlineAdapter = ArrayAdapter.createFromResource(this,
                R.array.airline_list, android.R.layout.simple_spinner_item);
        airlineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        airlineSpinner.setAdapter(airlineAdapter);

        ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(this,
                R.array.class_list, android.R.layout.simple_spinner_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(classAdapter);

        // Get data from Intent
        Intent receivedIntent = getIntent();
        city = receivedIntent.getStringExtra("city");
        country = receivedIntent.getStringExtra("country");
        startDate = receivedIntent.getStringExtra("startDate");
        endDate = receivedIntent.getStringExtra("endDate");
        peopleCount = receivedIntent.getStringExtra("peopleCount");

        // Display received data
        cityCountryText.setText(String.format("Destination: %s, %s", city, country));
        datesText.setText(String.format("Dates: %s to %s", startDate, endDate));
        peopleCountText.setText(String.format("Number of Travelers: %s", peopleCount));

        ActivityResultLauncher<Intent> getContent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if(o.getResultCode() == RESULT_OK) {
                            Intent receivedData = o.getData();
                            String dataReceived = null;
                            if (receivedData != null) {
                                dataReceived = receivedData.getStringExtra("data3");
                                Log.d(TAG, "Data received: " + dataReceived);
                                Toast.makeText(Activity2.this, dataReceived, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

        goTo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (budgetInput.getText().toString().isEmpty()) {
                    Toast.makeText(Activity2.this,
                            "Please enter your budget", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent myIntent = new Intent(Activity2.this, Activity3.class);
                // Pass all data to Activity3
                myIntent.putExtra("city", city);
                myIntent.putExtra("country", country);
                myIntent.putExtra("startDate", startDate);
                myIntent.putExtra("endDate", endDate);
                myIntent.putExtra("peopleCount", peopleCount);
                myIntent.putExtra("budget", budgetInput.getText().toString());
                myIntent.putExtra("airline", airlineSpinner.getSelectedItem().toString());
                myIntent.putExtra("travelClass", classSpinner.getSelectedItem().toString());
                myIntent.putExtra("data2", "hello from activity 2");
                getContent.launch(myIntent);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Toast.makeText(Activity2.this, "Back Pressed", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(callback);
    }
}