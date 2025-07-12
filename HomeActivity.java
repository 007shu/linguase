package com.example.myapplicationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        LinearLayout cardFrench = findViewById(R.id.cardFrench);
        LinearLayout cardSpanish = findViewById(R.id.cardSpanish);
        LinearLayout cardJapanese = findViewById(R.id.cardJapanese);
        LinearLayout cardGerman = findViewById(R.id.cardGerman);

        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnHelp = findViewById(R.id.btnHelp);
        Button btnMore = findViewById(R.id.btnMore);

        cardFrench.setOnClickListener(view -> navigateToActivity(FrenchActivity.class, "French selected"));
        cardSpanish.setOnClickListener(view -> navigateToActivity(SpanishActivity.class, "Spanish selected"));
        cardJapanese.setOnClickListener(view -> navigateToActivity(JapaneseActivity.class, "Japanese selected"));
        cardGerman.setOnClickListener(view -> navigateToActivity(GermanActivity.class, "German selected"));

        btnProfile.setOnClickListener(view -> navigateToActivity(ProfileActivity.class, "Navigating to Profile"));
        btnHelp.setOnClickListener(view -> navigateToActivity(HelpActivity.class, "Navigating to Help"));
        btnMore.setOnClickListener(view -> navigateToActivity(MoreActivity.class, "Navigating to More"));
    }



    private void navigateToActivity(Class<?> targetActivity, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, targetActivity));
    }
}


