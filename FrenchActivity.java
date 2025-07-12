package com.example.myapplicationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class FrenchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_french);

        Button btnStartLearning = findViewById(R.id.btn_start_learning);

        // Navigate to French Quiz Activity

            btnStartLearning.setOnClickListener(v -> {
                Intent intent = new Intent(FrenchActivity.this, LearningOptionsActivity_French.class);
                startActivity(intent);
            });

    }
}




