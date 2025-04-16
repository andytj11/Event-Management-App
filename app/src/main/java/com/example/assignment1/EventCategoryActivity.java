/*
Name: ANDY TJANDRA
Student ID: 32898460
email: andy0002@student.monash.edu
 */
package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment1.provider.Category;
import com.example.assignment1.provider.CategoryViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.StringTokenizer;

public class EventCategoryActivity extends AppCompatActivity {

    // Initialization here.
    private CategoryViewModel categoryViewModel;

    // ====================================================================================================================================================

    /*/
       --------------------------------------------------------
       OnCreate Method.
       --------------------------------------------------------
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_category);

        // from Assignment 1.
        EventSMSReceiver myBroadCastReceiver = new EventSMSReceiver();
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER_CATEGORY), RECEIVER_EXPORTED);

        // from Assignment 2, but now we set the title to be Assignment 3.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Categories");
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Assignment 3.
        // view model (for requirement B - database)
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    // end of onCreate method.
    // ====================================================================================================================================================
    // ====================================================================================================================================================



    // ====================================================================================================================================================

    /*/
        --------------------------------------------------------
        Below are codes for Save Button.
        --------------------------------------------------------
     */

    public void onSaveButtonClick(View view) {
        EditText categoryNameEditText = findViewById(R.id.categoryNameEditText);
        String categoryName = categoryNameEditText.getText().toString();

        EditText eventCountEditText = findViewById(R.id.eventCountEditText);
        String eventCountString = eventCountEditText.getText().toString();
        if (eventCountString.isEmpty()){
            eventCountString = "0";
            eventCountEditText.setText(eventCountString);
        }
        int eventCount = convertStringToInt(eventCountString);

        Switch isCategoryActiveSwitch = findViewById(R.id.isCategoryActiveSwitch);
        boolean isCategoryActive = isCategoryActiveSwitch.isChecked();

        EditText categoryLocationEditText = findViewById(R.id.categoryLocationEditText);
        String categoryLocation = categoryLocationEditText.getText().toString();

        if (validData(categoryName, eventCount)){
            String categoryID = generateRandomCategoryID();
            displayCategoryID(categoryID);
            Category newCategory = new Category(categoryID, categoryName, eventCount, isCategoryActive,categoryLocation);

            // saving it to database.
            categoryViewModel.insert(newCategory);
            Toast.makeText(this, String.format("Category saved successfully: %s", categoryID), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // end of save button operations.
    // ====================================================================================================================================================
    // ====================================================================================================================================================



    // ====================================================================================================================================================

    /*/
        --------------------------------------------------------
        Below are codes for Validation
        --------------------------------------------------------
     */

    private int convertStringToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private boolean validData(String categoryName, int eventCount) {
        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Category Name should not be Empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!validCategoryName(categoryName)){
            Toast.makeText(this, "Category Name can only be AlphaNumeric", Toast.LENGTH_SHORT).show();
            return false;
        } else if (eventCount < 0) {
            Toast.makeText(this, "Event Count Number should be Positive", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validCategoryName(String categoryName){
        if (categoryName.matches("^[a-zA-Z0-9 ]+$")) {
            try{
                int validation = Integer.parseInt(categoryName);
                return false;
            } catch(Exception e){
                return true;
            }
        }
        return false;
    }

    private char generateRandomCharacter(){
        Random r = new Random();
        return (char)(r.nextInt(26) + 'A');
    }

    private String generateRandomCategoryID(){
        Random r = new Random();
        String fourDigits = String.valueOf(r.nextInt(9000) + 1000);
        String str = "C" + generateRandomCharacter() + generateRandomCharacter() + "-" + fourDigits;

        return str;
    }

    public void displayCategoryID(String categoryID){
        TextView categoryIDFieldTextView = findViewById(R.id.categoryIDFieldTextView);
        categoryIDFieldTextView.setText(categoryID);
    }

    // end of codes for validation data.
    // ====================================================================================================================================================
    // ====================================================================================================================================================



    // ====================================================================================================================================================

    /*/
       --------------------------------------------------------
       Below are codes for Prefill Data with SMS.
       --------------------------------------------------------
    */

    class EventSMSReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            assert msg != null;
            msg = msg.replace(";", ";|");

            StringTokenizer sT = new StringTokenizer(msg, ";");
            if (sT.countTokens() != 3) {
                Toast.makeText(context, "Count token should be 3", Toast.LENGTH_SHORT).show();
                return;
            }

            String categoryName = "";
            String eventCountString = "";
            String isCategoryActiveString = "";

            try {
                categoryName = sT.nextToken();

                eventCountString = sT.nextToken();
                eventCountString = eventCountString.substring(1);
                if (eventCountString.isEmpty()){
                    eventCountString = "0";
                }

                isCategoryActiveString = sT.nextToken();
                isCategoryActiveString = isCategoryActiveString.substring(1);
                if (isCategoryActiveString.isEmpty()){
                    isCategoryActiveString = "false";
                }

            } catch (Exception e) {
                Toast.makeText(context, "Missing Parameters or are not separated by semicolon", Toast.LENGTH_SHORT).show();
                return;
            }

            if(validBooleanString(isCategoryActiveString) && validMessageData(categoryName, eventCountString)){
                prefillCategoryData(categoryName, eventCountString, convertStringToBool(isCategoryActiveString));
            }
        }
    }

    private boolean validBooleanString(String str){
        if (str.equalsIgnoreCase("TRUE") || (str.equalsIgnoreCase("FALSE"))){
            return true;
        }
        else if (str.isEmpty()){
            return true;
        } else {
            Toast.makeText(this, str + " is not a Boolean", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean validMessageData(String categoryName, String eventCountStr){
        int eventCount = 0;
        if (Objects.equals(eventCountStr, "")){
            eventCountStr = "0";
        }
        if (!eventCountStr.isEmpty()){
            try {
                eventCount = Integer.parseInt(eventCountStr);
                if (eventCount < 0) {
                    Toast.makeText(this, "Event Count Number should be Positive", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }

        if(categoryName.isEmpty() || categoryName.matches("^.[^a-zA-Z0-9 ].$")) {
            Toast.makeText(this, "Category Name should not be Empty or Value must be correct", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean convertStringToBool(String str){
        if (str.equalsIgnoreCase("TRUE")) {
            return true;
        }
        return false;
    }

    public void prefillCategoryData(String categoryName, String eventCountString, boolean isCategoryActive){
        EditText categoryNameEditText = findViewById(R.id.categoryNameEditText);
        EditText eventCountEditText = findViewById(R.id.eventCountEditText);
        Switch isCategoryActiveSwitch = findViewById(R.id.isCategoryActiveSwitch);

        categoryNameEditText.setText(categoryName);
        eventCountEditText.setText(eventCountString);
        isCategoryActiveSwitch.setChecked(isCategoryActive);
    }

    // end of prefill data with sms.
    // ====================================================================================================================================================
    // ====================================================================================================================================================

}