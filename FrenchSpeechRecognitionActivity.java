package com.example.myapplicationapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.RecognitionListener;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class FrenchSpeechRecognitionActivity extends AppCompatActivity {
    private TextToSpeech textToSpeech;
    private SpeechRecognizer speechRecognizer;
    private TextView wordToSpell, recognitionResult, scoreView, wordHistory, hintView, timerView, highScoreView;
    private Button btnListen, btnSpeak, btnNext, btnRetry, btnHint, btnChangeLevel;
    private CountDownTimer timer;

    private String[][] phrases = {
            {"bonjour", "merci", "au revoir", "s'il vous plaît", "oui", "non"},  // Beginner
            {"je suis content", "quelle heure est-il", "pouvez-vous m'aider", "il fait beau aujourd'hui"},  // Intermediate
            {"j'aimerais réserver une table", "je voudrais une chambre avec vue", "le train part à quelle heure"}  // Advanced
    };

    private String correctWord = "";
    private int score = 0, attempts = 0, highScore = 0, difficulty = 0;
    private StringBuilder history = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_french_speech_recognition);

        wordToSpell = findViewById(R.id.word_to_spell);
        recognitionResult = findViewById(R.id.recognition_result);
        scoreView = findViewById(R.id.score_view);
        wordHistory = findViewById(R.id.word_history);
        hintView = findViewById(R.id.hint_view);
        timerView = findViewById(R.id.timer_view);
        highScoreView = findViewById(R.id.high_score_view);
        btnListen = findViewById(R.id.btn_listen);
        btnSpeak = findViewById(R.id.btn_speak);
        btnNext = findViewById(R.id.btn_next);
        btnRetry = findViewById(R.id.btn_retry);
        btnHint = findViewById(R.id.btn_hint);
        btnChangeLevel = findViewById(R.id.btn_change_level);

        requestAudioPermission();
        setNewWord();

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.FRENCH);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(getApplicationContext(), "French TTS not supported!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
            }
        });

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String recognizedText = matches.get(0);
                    recognitionResult.setText("You said: " + recognizedText);

                    if (recognizedText.equalsIgnoreCase(correctWord)) {
                        recognitionResult.setText("✅ Correct!");
                        recognitionResult.setTextColor(Color.GREEN);
                        score++;
                        updateHistory(correctWord + " ✅");
                        disableRetry();
                        updateHighScore();
                    } else {
                        recognitionResult.setText("❌ Try again!");
                        recognitionResult.setTextColor(Color.RED);
                        updateHistory(correctWord + " ❌");
                        enableRetry();
                    }
                    attempts++;
                    updateScore();
                }
            }

            @Override public void onReadyForSpeech(Bundle params) {}
            @Override public void onBeginningOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onEndOfSpeech() {}
            @Override public void onError(int error) {
                Toast.makeText(getApplicationContext(), "Speech recognition error!", Toast.LENGTH_SHORT).show();
            }
            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onEvent(int eventType, Bundle params) {}
        });

        btnListen.setOnClickListener(v -> speakWord());
        btnSpeak.setOnClickListener(v -> startSpeechRecognition());
        btnNext.setOnClickListener(v -> setNewWord());
        btnRetry.setOnClickListener(v -> startSpeechRecognition());
        btnHint.setOnClickListener(v -> showHint());
        btnChangeLevel.setOnClickListener(v -> changeDifficulty());

        disableRetry();
    }

    private void requestAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    private void setNewWord() {
        Random random = new Random();
        correctWord = phrases[difficulty][random.nextInt(phrases[difficulty].length)];
        wordToSpell.setText(correctWord);
        recognitionResult.setText("");
        hintView.setText("");
        startTimer();
        disableRetry();
    }

    private void speakWord() {
        if (textToSpeech != null) {
            textToSpeech.speak(correctWord, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.FRENCH);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronounce the word...");
        try {
            speechRecognizer.startListening(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Speech recognition not supported!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateScore() {
        scoreView.setText("Score: " + score + " / " + attempts);
    }

    private void updateHighScore() {
        if (score > highScore) {
            highScore = score;
            highScoreView.setText("High Score: " + highScore);
        }
    }

    private void updateHistory(String wordResult) {
        history.append(wordResult).append("\n");
        wordHistory.setText(history.toString());
    }

    private void enableRetry() {
        btnRetry.setEnabled(true);
    }

    private void disableRetry() {
        btnRetry.setEnabled(false);
    }

    private void showHint() {
        hintView.setText("Hint: " + correctWord.substring(0, correctWord.length() / 2) + "...");
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerView.setText("Time left: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                timerView.setText("⏳ Time up! Try again.");
                enableRetry();
            }
        }.start();
    }

    private void changeDifficulty() {
        difficulty = (difficulty + 1) % 3;
        setNewWord();
        Toast.makeText(this, "Difficulty changed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
}
