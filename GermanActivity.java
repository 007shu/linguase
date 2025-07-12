package com.example.myapplicationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class



GermanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_german);

        Button btnStartLearning = findViewById(R.id.btn_start_learning);

        // Navigate to German Quiz Activity
        btnStartLearning.setOnClickListener(v -> {
            Intent intent = new Intent(GermanActivity.this, LearningOptionsActivity_German.class);
            startActivity(intent);
        });
    }
}

