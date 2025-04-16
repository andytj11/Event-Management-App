/*
Name: ANDY TJANDRA
Student ID: 32898460
email: andy0002@student.monash.edu
 */
package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void redirectToEventCategory(View view){
        Intent intent = new Intent(this, EventCategoryActivity.class);
        startActivity(intent);
    }

    public void redirectToEvent(View view){
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }
}