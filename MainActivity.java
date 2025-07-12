package com.example.myapplicationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find buttons by their IDs
        Button loginButton = findViewById(R.id.loginActivityButton);
        Button signupButton = findViewById(R.id.signupActivityButton);

        // Set click listeners
        loginButton.setOnClickListener(v -> {
            // Navigate to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(v -> {
            // Navigate to SignupActivity
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}
