package com.example.myapplicationapp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SpanishCrosswordActivity extends AppCompatActivity {

    private SpanishCrosswordGridAdapter adapter;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "SpanishCrosswordPrefs";

    private String[][] crosswordGrid = {
            {"C", "", "S", ""},
            {"M", "A", "", ""},
            {"S", "", "L", ""},
            {"P", "", "R", "", "O"},
            {"G", "", "T", ""},
            {"R", "", "O", ""}
    };

    private String[] words = {"CASA", "MAR", "SOL", "PERRO", "GATO", "RÍO"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spanish_crossword);

        GridView gridView = findViewById(R.id.crosswordGrid);
        Button submitButton = findViewById(R.id.btn_submit);
        Button saveButton = findViewById(R.id.btn_save);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        adapter = new SpanishCrosswordGridAdapter(this, crosswordGrid, prefs);
        gridView.setAdapter(adapter);

        submitButton.setOnClickListener(view -> checkAnswers());
        saveButton.setOnClickListener(view -> saveProgress());
    }

    private void checkAnswers() {
        if (adapter.isCrosswordCorrect(words)) {
            Toast.makeText(this, "¡Bravo! Has completado el crucigrama!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Inténtalo de nuevo. Algunas palabras son incorrectas.", Toast.LENGTH_LONG).show();
        }
    }

    private void saveProgress() {
        adapter.saveUserProgress();
        Toast.makeText(this, "Progreso guardado!", Toast.LENGTH_SHORT).show();
    }
}
