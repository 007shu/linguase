package com.example.myapplicationapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.RecognitionListener;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class JapaneseSpeechRecognitionActivity extends AppCompatActivity {
    private TextToSpeech textToSpeech;
    private SpeechRecognizer speechRecognizer;
    private TextView textViewWord, textViewResult, textViewFeedback, textViewScore;
    private Button buttonStart, buttonListen, buttonNewWord, buttonAddCustom;
    private EditText editTextCustomWord;
    private MediaPlayer successSound;
    private Vibrator vibrator;
    private int score = 0;

    // Japanese words & sentences
    private String[] japaneseWords = {
            "こんにちは", "ありがとう", "さようなら", "おはようございます", "おやすみなさい", "すみません"
    };

    private String[] japaneseSentences = {
            "私は日本語を勉強しています", "これはペンです", "あなたの名前は何ですか", "日本の文化が好きです", "明日は晴れますか？"
    };

    private String currentWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_japanese_speech_recognition);

        textViewWord = findViewById(R.id.textViewWord);
        textViewResult = findViewById(R.id.textViewResult);
        textViewFeedback = findViewById(R.id.textViewFeedback);
        textViewScore = findViewById(R.id.textViewScore);
        buttonStart = findViewById(R.id.buttonStart);
        buttonListen = findViewById(R.id.buttonListen);
        buttonNewWord = findViewById(R.id.buttonNewWord);
        buttonAddCustom = findViewById(R.id.buttonAddCustom);
        editTextCustomWord = findViewById(R.id.editTextCustomWord);

        // Initialize TTS
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.JAPANESE);
            }
        });

        // Initialize Sound & Vibration
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        try {
            successSound = MediaPlayer.create(this, R.raw.success_sound);
        } catch (Exception e) {
            successSound = null; // If the sound file is missing, prevent crash
        }

        // Initialize Speech Recognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String recognizedText = matches.get(0);
                    textViewResult.setText("You said: " + recognizedText);
                    checkPronunciation(recognizedText, currentWord);
                }
            }

            @Override public void onError(int error) {
                textViewFeedback.setText("❌ Recognition error. Try again!");
            }
            @Override public void onReadyForSpeech(Bundle params) {}
            @Override public void onBeginningOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onEndOfSpeech() {}
            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onEvent(int eventType, Bundle params) {}
        });

        // Set a random word initially
        setRandomWord();

        // Listen to pronunciation
        buttonListen.setOnClickListener(v -> textToSpeech.speak(currentWord, TextToSpeech.QUEUE_FLUSH, null, null));

        // Start Speech Recognition
        buttonStart.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ja-JP");
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say: " + currentWord);
            speechRecognizer.startListening(intent);
        });

        // Get new word
        buttonNewWord.setOnClickListener(v -> setRandomWord());

        // Add custom word
        buttonAddCustom.setOnClickListener(v -> {
            String customWord = editTextCustomWord.getText().toString().trim();
            if (!customWord.isEmpty()) {
                currentWord = customWord;
                textViewWord.setText(currentWord);
                textViewResult.setText("Your Pronunciation:");
                textViewFeedback.setText("Try pronouncing the word!");
                editTextCustomWord.setText("");
            }
        });
    }

    // Selects a random word or sentence
    private void setRandomWord() {
        Random random = new Random();
        if (random.nextBoolean()) {
            currentWord = japaneseWords[random.nextInt(japaneseWords.length)];
        } else {
            currentWord = japaneseSentences[random.nextInt(japaneseSentences.length)];
        }
        textViewWord.setText(currentWord);
        textViewResult.setText("Your Pronunciation:");
        textViewFeedback.setText("Try pronouncing the word!");
    }

    // Compare pronunciation accuracy
    private void checkPronunciation(String recognized, String correct) {
        int distance = levenshteinDistance(recognized, correct);
        if (distance == 0) {
            textViewFeedback.setText("✅ Perfect pronunciation! 🎉");
            score++;
            if (successSound != null) successSound.start();
        } else if (distance <= 2) {
            textViewFeedback.setText("🟡 Almost correct! Keep practicing. 😊");
        } else {
            textViewFeedback.setText("❌ Try again! 😅");
            if (vibrator != null) vibrator.vibrate(300);  // Vibrate on incorrect answer
        }
        textViewScore.setText("Score: " + score);
    }

    // Levenshtein Distance Algorithm
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) dp[i][j] = j;
                else if (j == 0) dp[i][j] = i;
                else {
                    int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
                }
            }
        }
        return dp[s1.length()][s2.length()];
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
        if (successSound != null) {
            successSound.release();
        }
        super.onDestroy();
    }
}