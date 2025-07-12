package com.example.myapplicationapp;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;
import java.util.Random;

public class GermanSpeechRecognitionActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private TextView txtWord, txtFeedback, txtInstructions, txtScore, txtDifficulty, txtAttempts;
    private Button btnSpeak, btnNewPhrase, btnNext, btnRepeat, btnIncreaseDifficulty, btnDecreaseDifficulty, btnReset;
    private String[] germanPhrases = {
            "Hallo", "Guten Morgen", "Wie geht es Ihnen?", "Ich liebe Deutsch", "Sprechen Sie Deutsch?",
            "Wo ist der Bahnhof?", "Könnten Sie das bitte wiederholen?", "Ich verstehe nicht", "Ich möchte etwas essen", "Danke schön"
    };
    private String currentPhrase;
    private int score = 0;
    private int difficultyLevel = 1;
    private int attempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_german_speech_recognition);

        txtWord = findViewById(R.id.txtWord);
        txtFeedback = findViewById(R.id.txtFeedback);
        txtInstructions = findViewById(R.id.txtInstructions);
        txtScore = findViewById(R.id.txtScore);
        txtDifficulty = findViewById(R.id.txtDifficulty);
        txtAttempts = findViewById(R.id.txtAttempts);
        btnSpeak = findViewById(R.id.btnSpeak);
        btnNewPhrase = findViewById(R.id.btnNewPhrase);
        btnNext = findViewById(R.id.btnNext);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnIncreaseDifficulty = findViewById(R.id.btnIncreaseDifficulty);
        btnDecreaseDifficulty = findViewById(R.id.btnDecreaseDifficulty);
        btnReset = findViewById(R.id.btnReset);

        txtInstructions.setText("Tap 'Listen to Pronunciation' to hear the phrase and try to repeat it!");
        updateUI();
        generateNewPhrase();

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.GERMAN);
                textToSpeech.setSpeechRate(0.9f);
                textToSpeech.setPitch(1.0f);
            }
        });

        btnSpeak.setOnClickListener(v -> speakPhrase());
        btnNewPhrase.setOnClickListener(v -> generateNewPhrase());
        btnNext.setOnClickListener(v -> generateNewPhrase());
        btnRepeat.setOnClickListener(v -> repeatPhrase());
        btnIncreaseDifficulty.setOnClickListener(v -> changeDifficulty(1));
        btnDecreaseDifficulty.setOnClickListener(v -> changeDifficulty(-1));
        btnReset.setOnClickListener(v -> resetGame());
    }

    private void generateNewPhrase() {
        Random random = new Random();
        currentPhrase = germanPhrases[random.nextInt(germanPhrases.length)];
        txtWord.setText(currentPhrase);
        txtFeedback.setText("");
        attempts = 0;
        updateUI();
    }

    private void speakPhrase() {
        if (textToSpeech != null) {
            textToSpeech.speak(currentPhrase, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void repeatPhrase() {
        attempts++;
        txtFeedback.setText("Try pronouncing: " + currentPhrase);
        updateUI();
    }

    private void updateUI() {
        txtScore.setText("Score: " + score);
        txtDifficulty.setText("Difficulty: " + difficultyLevel);
        txtAttempts.setText("Attempts: " + attempts);
    }

    private void changeDifficulty(int change) {
        difficultyLevel = Math.max(1, Math.min(5, difficultyLevel + change));
        updateUI();
        Toast.makeText(this, "Difficulty set to " + difficultyLevel, Toast.LENGTH_SHORT).show();
    }

    private void resetGame() {
        score = 0;
        attempts = 0;
        difficultyLevel = 1;
        updateUI();
        generateNewPhrase();
        Toast.makeText(this, "Game Reset!", Toast.LENGTH_SHORT).show();
    }

    private void checkPronunciation(String userSpeech) {
        if (userSpeech.equalsIgnoreCase(currentPhrase)) {
            score++;
            txtFeedback.setText("✅ Correct! You said: " + userSpeech);
            txtFeedback.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            txtFeedback.setText("❌ Incorrect. You said: " + userSpeech + "\nCorrect phrase: " + currentPhrase);
            txtFeedback.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
        updateUI();
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
