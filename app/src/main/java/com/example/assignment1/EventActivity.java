package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment1.provider.Category;
import com.example.assignment1.provider.CategoryViewModel;
import com.example.assignment1.provider.Event;
import com.example.assignment1.provider.EventViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.StringTokenizer;

public class EventActivity extends AppCompatActivity {

    // ====================================================================================================================================================

    /*/
       --------------------------------------------------------
       All Initialization here.
       --------------------------------------------------------
    */

    /*
    from Assignment 1.
     */
    private EditText eventIdEditText, eventNameEditText, categoryIDEditText, ticketsAvailableEditText;
    private Switch isEventActiveSwitch;

    /*
    from Assignment 2.
     */
    private Toolbar toolbar;
    private DrawerLayout drawerlayout;
    private NavigationView navigationView;

    /*
    for Requirement B (Database).
     */
    private List<Category> listCategory = new ArrayList<>();
    private CategoryViewModel categoryViewModel;
    private EventViewModel eventViewModel;

    /*
    for Requirement E (Gestures).
     */

    // help detect basic gestures like scroll, single tap, double tap, etc
    private GestureDetectorCompat mDetector;
    private TextView gestureTypeTextView;
    private View touchPad;


    // end of initialization here.
    // ====================================================================================================================================================
    // ====================================================================================================================================================



    // ====================================================================================================================================================

    /*/
       --------------------------------------------------------
       OnCreate Method.
       --------------------------------------------------------
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity_dashboard);

        eventIdEditText = findViewById(R.id.eventIdEditText);
        eventNameEditText = findViewById(R.id.eventNameEditText);
        categoryIDEditText = findViewById(R.id.categoryIDEditText);
        ticketsAvailableEditText = findViewById(R.id.ticketsAvailableEditText);
        isEventActiveSwitch = findViewById(R.id.isEventActiveSwitch);

        // from Assignment 1.
        EventSMSReceiver myBroadCastReceiver = new EventActivity.EventSMSReceiver();
        registerReceiver(myBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER_EVENT), RECEIVER_EXPORTED);

        // from Assignment 2, but now we set the title to be Assignment 3.
        // toolbar.
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Assignment-3");

        // drawer layout.
        drawerlayout = findViewById(R.id.drawer_layout_dashboard);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerlayout, toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();

        // navigation view.
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new MyNavigationListener(this));

        // FAB (floating-action button)
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(this::onSaveEventButtonClick);

        // Assignment 3.
        // view model (for requirement B - database)
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        retrieveListCategory();

        gestureTypeTextView = findViewById(R.id.gestureTypeTextView);
        touchPad = findViewById(R.id.touchPad);

        // initialise new instance of CustomGestureDetector class
        MyCustomGestureDetector customGestureDetector = new MyCustomGestureDetector();

        // register GestureDetector and set listener as CustomGestureDetector
        mDetector = new GestureDetectorCompat(this, customGestureDetector);

        touchPad.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // link detector to here to allow it to detect onLongPress and onDoubleTap
                mDetector.onTouchEvent(event);
                // return true so that this touch listener will handle the touch events
                return true;
            }
        });
    }

    // end of onCreate method.
    // ====================================================================================================================================================
    // ====================================================================================================================================================



    // Retrieve Categories data from room database and add data into listCategory
    private void retrieveListCategory(){
        // the database will perform a read operation and retrieve the categories data available.
        categoryViewModel.getAllCategory().observe(this, newData -> {
            if (listCategory != null){
                listCategory.clear();
            }
            listCategory.addAll(newData);
        });
    }


    // ====================================================================================================================================================

    /*/
        --------------------------------------------------------
        Below are codes for Gestures. (Requirement E)
        --------------------------------------------------------
     */

    /*
    This is MyCustomGestureDetector class extending
    Convenience class (SimpleOnGestureListener)
    */
    class MyCustomGestureDetector extends GestureDetector.SimpleOnGestureListener{
        /*
        Double Tap Gesture
        (if double taps on touchpad area, save new event)
         */
        @Override
        public boolean onDoubleTap(@NonNull MotionEvent e) {
            gestureTypeTextView.setText("onDoubleTap");
            onSaveEventButtonClick(touchPad);
            return true;
        }

        /*
        Long Press Gesture
        (if long press on touchpad area, clear all fields)
         */
        @Override
        public void onLongPress(@NonNull MotionEvent e) {
            gestureTypeTextView.setText("onLongPress");
            clearEventForm();
        }

    }

    // end of doubleTap and longPress gestures.
    // ====================================================================================================================================================
    // ====================================================================================================================================================



    // ====================================================================================================================================================

    /*/
       --------------------------------------------------------
       Below are codes for NavigationView and DrawerLayout.
       --------------------------------------------------------
    */

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {

        private Context context;
        public MyNavigationListener(Context context) {
            this.context = context;
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            int id = item.getItemId();

            if (id == R.id.viewAllCatNav) {
                //
                redirectToListCategory();
            } else if (id == R.id.addCatNav) {
                redirectToNewCatForm();
            } else if (id == R.id.viewAllEventsNav) {
                redirectToListEvent();
            } else if (id == R.id.logoutNav) {
                logout();
            }
            drawerlayout.closeDrawers();
            return true;
        }

        public void redirectToListCategory(){
            Intent intent = new Intent(context, ListCategoryActivity.class);
            startActivity(intent);
        }

        public void redirectToListEvent(){
            Intent intent = new Intent(context, ListEventActivity.class);
            startActivity(intent);
        }

        public void redirectToNewCatForm(){
            Intent intent = new Intent(context, EventCategoryActivity.class);
            startActivity(intent);
        }

        public void logout() {
            Intent loginPageIntent = new Intent(context, LoginActivity.class);
            startActivity(loginPageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }


    // ====================================================================================================================================================

    /*/
       --------------------------------------------------------
       Below are codes for Option Menu.
       --------------------------------------------------------
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.clearEventFormOption) {
            // Op. A
            clearEventForm();
        } else if (itemId == R.id.deleteAllCatOption) {
            // Op. B
            deleteAllCategories();
        } else if (itemId == R.id.deleteAllEventsOption) {
            // Op. C - Delete All Events
            deleteAllEvents();
        }
        return true;
    }

    // Op A. - Clear Event Form
    public void clearEventForm(){
        eventIdEditText.setText("");
        eventNameEditText.setText("");
        categoryIDEditText.setText("");
        ticketsAvailableEditText.setText("");
        isEventActiveSwitch.setChecked(false);
    }

    // Op B. - Delete ALl Categories
    public void deleteAllCategories(){
        // we do not use shared preferences anymore now,
        // but using the database instead to perform delete all operation to remove all event categories available.
        categoryViewModel.deleteAllCategory();
        Toast.makeText(this, "All categories deleted.", Toast.LENGTH_SHORT).show();
    }

    // Op C. - Delete ALl Events
    public void deleteAllEvents(){
        // we do not use shared preferences anymore now,
        // but using the database instead to perform delete all operation to remove all events available.
        eventViewModel.deleteAllEvent();
        Toast.makeText(this, "All events have been deleted", Toast.LENGTH_SHORT).show();
    }

    // end of option menu and its operations.
    // ====================================================================================================================================================
    // ====================================================================================================================================================



    // ====================================================================================================================================================

    /*/
        --------------------------------------------------------
        Below are codes for Save Button.
        --------------------------------------------------------
     */

    public void onSaveEventButtonClick(View view){
        String eventName = eventNameEditText.getText().toString();
        String categoryID = categoryIDEditText.getText().toString();
        String ticketsAvailableString = ticketsAvailableEditText.getText().toString();

        if (ticketsAvailableString.isEmpty()){
            ticketsAvailableString = "0";
            ticketsAvailableEditText.setText(ticketsAvailableString);
        }
        int ticketsAvailable = convertStringToInt(ticketsAvailableString);

        boolean isEventActive = isEventActiveSwitch.isChecked();

        if (validData(eventName, categoryID, ticketsAvailable)){
            // generate random event ID and display it.
            String eventID = generateRandomEventID();
            displayEventID(eventID);

            // Saving to database
            Event newEvent = new Event(eventID, eventName, categoryID.toUpperCase(), ticketsAvailable, isEventActive);
            eventViewModel.insert(newEvent);

            Snackbar.make(view, String.format("Event saved: %s to %s", eventID, categoryID.toUpperCase()), Snackbar.LENGTH_LONG)
                    // undo feature, then we need the database to perform delete operation for the recent event added.
                    .setAction("Undo", v -> eventViewModel.deleteNewestEvent()).show();
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

    private boolean validData(String eventName, String categoryID, int ticketsAvailable){

        if (eventName.isEmpty()){
            Toast.makeText(this, "Event Name should not be Empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!validEventName(eventName)){
            Toast.makeText(this, "Event Name can only be AlphaNumeric", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!validCategoryId(categoryID)){
            Toast.makeText(this, "Category ID does not Exist", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (ticketsAvailable < 0) {
            Toast.makeText(this, "Tickets Available Number should be Positive", Toast.LENGTH_SHORT).show();
            return false;
        }

        // The database will perform an update operation to increment event count by 1 for that specific category ID.
        categoryViewModel.incrementEventCount(categoryID.toUpperCase());
        return true;
    }

    private boolean validEventName(String eventName){
        if (eventName.matches("^[a-zA-Z0-9 ]+$")) {
            try{
                int validation = Integer.parseInt(eventName);
                return false;
            } catch(Exception e){
                return true;
            }
        }
        return false;
    }

    private boolean validCategoryId(String categoryID){

        for (Category category : listCategory){
            if (categoryID.equalsIgnoreCase(category.getCategoryId())){
                return true;
            }
        }
        return false;
    }

    private String generateRandomEventID(){
        Random r = new Random();
        String fiveDigits = String.valueOf(r.nextInt(90000) + 10000);
        String str = "E" + generateRandomCharacter() + generateRandomCharacter() + "-" + fiveDigits;

        return str;
    }

    private char generateRandomCharacter(){
        Random r = new Random();
        return (char)(r.nextInt(26) + 'A');
    }

    public void displayEventID(String eventID){
        eventIdEditText.setText(eventID);
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
            if (sT.countTokens() != 4) {
                Toast.makeText(context, "Count token should be 4", Toast.LENGTH_SHORT).show();
                return;
            }

            String eventName = "";
            String categoryID = "";
            String ticketsAvailableString = "";
            String isEventActiveString = "";

            try {
                eventName = sT.nextToken();

                categoryID = sT.nextToken();
                categoryID = categoryID.substring(1);

                ticketsAvailableString = sT.nextToken();
                ticketsAvailableString = ticketsAvailableString.substring(1);
                if (ticketsAvailableString.isEmpty()){
                    ticketsAvailableString = "0";
                }

                isEventActiveString = sT.nextToken();
                isEventActiveString = isEventActiveString.substring(1);
                if (isEventActiveString.isEmpty()){
                    isEventActiveString = "false";
                }

            } catch (Exception e) {
                Toast.makeText(context, "Missing Parameters or are not separated by semicolon", Toast.LENGTH_SHORT).show();
                return;
            }

            if(validBooleanString(isEventActiveString) && validMessageData(categoryID, eventName, ticketsAvailableString)){
                prefillEventData(categoryID, eventName, ticketsAvailableString, convertStringToBool(isEventActiveString));
            }
        }
    }

    private boolean validBooleanString(String str){
        if (str.isEmpty() || str.equalsIgnoreCase("TRUE") || (str.equalsIgnoreCase("FALSE"))){
            return true;
        }

        Toast.makeText(this, "Not a Boolean", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean validMessageData(String categoryID, String eventName, String ticketsAvailableString){
        int ticketsAvailable = 0;

        if (!ticketsAvailableString.isEmpty()){
            try {
                if (Objects.equals(ticketsAvailableString, "")){
                    ticketsAvailableString = "0";
                }

                ticketsAvailable = Integer.parseInt(ticketsAvailableString);

                if (ticketsAvailable < 0) {
                    Toast.makeText(this, "Tickets Available Number should be Positive", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            catch (Exception e) {
                return false;
            }
        }

        if(categoryID.isEmpty()) {
            Toast.makeText(this, "Category ID should not be Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (eventName.isEmpty() || eventName.matches("^.[^a-zA-Z0-9 ].$")) {
            Toast.makeText(this, "Event Name should not be Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void prefillEventData(String categoryID, String eventName, String ticketsAvailable, boolean isEventActive){
        EditText eventNameEditText = findViewById(R.id.eventNameEditText);
        EditText categoryIDEditText = findViewById(R.id.categoryIDEditText);
        EditText ticketsAvailableEditText = findViewById(R.id.ticketsAvailableEditText);
        Switch isEventActiveSwitch = findViewById(R.id.isEventActiveSwitch);

        eventNameEditText.setText(eventName);
        categoryIDEditText.setText(categoryID);
        ticketsAvailableEditText.setText(ticketsAvailable);
        isEventActiveSwitch.setChecked(isEventActive);
    }

    private boolean convertStringToBool(String str){
        return str.equalsIgnoreCase("TRUE");
    }

    // end of prefill data with sms.
    // ====================================================================================================================================================
    // ====================================================================================================================================================

}