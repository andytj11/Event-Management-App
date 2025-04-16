/*
Name: ANDY TJANDRA
Student ID: 32898460
email: andy0002@student.monash.edu
 */
package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/*
Name User should be at least 1 character.
Password User should be at least 3 digits.
*/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS
        }, 0);
    }

    public void onRegisterButtonCLick(View view){
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        String username = usernameEditText.getText().toString();

        EditText passwordEditText = findViewById(R.id.passwordEditText);
        String password = passwordEditText.getText().toString();

        EditText passwordConfirmationEditText = findViewById(R.id.passwordConfirmationEditText);
        String passwordConfirmation = passwordConfirmationEditText.getText().toString();

        if (validUsername(username) && samePassword(password, passwordConfirmation) && validPassword(password)){
            saveUserToSharedPreference(username, password);
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            redirectUserToLoginPage(view);
        }
        else {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validUsername(String username){
        if (username.length() >= 1) return true;
        return false;
    }
    private boolean samePassword(String pw, String pwConfirmation){
        if (pw.equals(pwConfirmation)) return true;
        return false;
    }

    private boolean validPassword(String pw){
        if (pw.length() >= 3) return true;
        return false;
    }

    private void saveUserToSharedPreference(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("KEY_USERNAME", username);
        editor.putString("KEY_PASSWORD", password);

        editor.apply();
    }

    public void redirectUserToLoginPage(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}