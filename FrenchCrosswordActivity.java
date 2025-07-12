package com.example.myapplicationapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FrenchCrosswordActivity extends AppCompatActivity {

    private FrenchCrosswordGridAdapter adapter;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "CrosswordPrefs";

    // Updated 10x10 crossword grid with correct word structure
    private String[][] crosswordGrid = {
            {"C", "", "A", "",},
            {"", "H", "", "E", "",},
            {"U","", "A", "", "D", ""},
            {"M", "", "I", "", "O", "", "N"},
            {"", "O", "", "T", "", "R", "",},
            { "É", "", "O", "", "E",},
            {"P", "O", "", "M", "", },
            {"", "O", "U", "", "E",},
            {"M", "", "R", "", "O", "", "E"}
    };

    private String[] words = {"CHAT", "CHIEN", "MAISON", "VOITURE", "ÉCOLE", "JARDIN", "POMME", "ROUGE", "MER", "SOLEIL"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.french_crossword);

        GridView gridView = findViewById(R.id.crosswordGrid);
        Button submitButton = findViewById(R.id.btn_submit);
        Button saveButton = findViewById(R.id.btn_save);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        adapter = new FrenchCrosswordGridAdapter(this, crosswordGrid, prefs);

        gridView.setAdapter(adapter);

        submitButton.setOnClickListener(view -> checkAnswers());
        saveButton.setOnClickListener(view -> saveProgress());
    }

    private void checkAnswers() {
        if (adapter.isCrosswordCorrect(words)) {
            Toast.makeText(this, "Bravo! You completed the crossword!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Try again! Some words are incorrect.", Toast.LENGTH_LONG).show();
        }
    }

    private void saveProgress() {
        adapter.saveUserProgress();
        Toast.makeText(this, "Progress saved!", Toast.LENGTH_SHORT).show();
    }
}
