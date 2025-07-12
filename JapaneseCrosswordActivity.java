package com.example.myapplicationapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class JapaneseCrosswordActivity extends AppCompatActivity {

    private JapaneseCrosswordGridAdapter adapter;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "JapaneseCrosswordPrefs";

    // Updated crossword grid with more five-letter Japanese words
    private String[][] crosswordGrid = {
            {"さ", "く", "", "ん", ""},
            {"", "ま", "", "り",},
            {"た", "", "の", "",},
            {"み", "", "ん", "", "い"},
            {"す", "", "か", "", "ろ"}
    };

    private String[] words = {"さくらんぼ", "ひまわり", "たけのこ", "みかんせい", "すいかめろ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.japanese_crossword);

        GridView gridView = findViewById(R.id.crosswordGrid);
        Button submitButton = findViewById(R.id.btn_submit);
        Button saveButton = findViewById(R.id.btn_save);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        adapter = new JapaneseCrosswordGridAdapter(this, crosswordGrid, prefs);
        gridView.setAdapter(adapter);

        submitButton.setOnClickListener(view -> checkAnswers());
        saveButton.setOnClickListener(view -> saveProgress());
    }

    private void checkAnswers() {
        if (adapter.isCrosswordCorrect(words)) {
            Toast.makeText(this, "素晴らしい！クロスワードを完成しました！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "もう一度試してください。間違った単語があります。", Toast.LENGTH_LONG).show();
        }
    }

    private void saveProgress() {
        adapter.saveUserProgress();
        Toast.makeText(this, "進捗が保存されました!", Toast.LENGTH_SHORT).show();
    }
}
