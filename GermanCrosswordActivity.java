package com.example.myapplicationapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GermanCrosswordActivity extends AppCompatActivity {

    private GermanCrosswordGridAdapter adapter;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "GermanCrosswordPrefs";

    private String[][] crosswordGrid = {
            {"H", "", "S", ""},
            {"A", "U", ""},
            {"S", "", "N"},
            {"H", "", "D", "", "E"},
            {"K", "", "Z", ""},
            {"F", "", "S"},
    };

    private String[] words = {"HAUS", "SEE", "SONNE", "HUND", "KATZE", "FLUSS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.german_crossword);

        GridView gridView = findViewById(R.id.crosswordGrid);
        Button submitButton = findViewById(R.id.btn_submit);
        Button saveButton = findViewById(R.id.btn_save);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        adapter = new GermanCrosswordGridAdapter(this, crosswordGrid, prefs);
        gridView.setAdapter(adapter);

        submitButton.setOnClickListener(view -> checkAnswers());
        saveButton.setOnClickListener(view -> saveProgress());
    }

    private void checkAnswers() {
        if (adapter.isCrosswordCorrect(words)) {
            Toast.makeText(this, "Gut gemacht! Du hast das Kreuzworträtsel gelöst!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Versuche es noch einmal. Einige Wörter sind falsch.", Toast.LENGTH_LONG).show();
        }
    }

    private void saveProgress() {
        adapter.saveUserProgress();
        Toast.makeText(this, "Fortschritt gespeichert!", Toast.LENGTH_SHORT).show();
    }
}
