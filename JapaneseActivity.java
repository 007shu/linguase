package com.example.myapplicationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class JapaneseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_japanese);

        Button btnStartLearning = findViewById(R.id.btn_start_learning);

        // Navigate to French Quiz Activity
        btnStartLearning.setOnClickListener(v -> {
            Intent intent = new Intent(JapaneseActivity.this, LearningOptionsActivity_Japanese.class);
            startActivity(intent);
        });
    }
}


