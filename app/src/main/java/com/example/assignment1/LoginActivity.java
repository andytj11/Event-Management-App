/*
Name: ANDY TJANDRA
Student ID: 32898460
email: andy0002@student.monash.edu
 */
package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefillUsername();
    }

    public void onLoginButtonClick(View view){
        EditText usernameEditText = findViewById(R.id.usernameEditText2);
        String username = usernameEditText.getText().toString();

        EditText passwordEditText = findViewById(R.id.passwordEditText2);
        String password = passwordEditText.getText().toString();

        if (loginSuccessful(username, password)){
            redirectToDashboardPage();
        }
        else {
            Toast.makeText(this, "Authentication failure: Username or Password incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean loginSuccessful(String username, String password){
        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);

        String usernameRestored = sharedPreferences.getString("KEY_USERNAME", "");
        String passwordRestored = sharedPreferences.getString("KEY_PASSWORD", "");

        if (username.equals(usernameRestored) && password.equals(passwordRestored)) return true;
        return false;
    }
    
    private void redirectToDashboardPage(){
        Intent intent = new Intent(this, EventActivity.class);
        startActivity(intent);
    }

    private void prefillUsername(){
        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        String usernameRestored = sharedPreferences.getString("KEY_USERNAME", "");

        EditText usernameEditText = findViewById(R.id.usernameEditText2);
        usernameEditText.setText(usernameRestored);
    }

    public void redirectToRegisterPage(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}