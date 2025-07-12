package com.example.myapplicationapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class SpanishSpeechRecognitionActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private TextView txtWord, txtFeedback, txtScore, txtDifficulty;
    private Button btnListen, btnSpeak, btnNext, btnChangeDifficulty;
    private int score = 0;
    private String targetPhrase;
    private String difficultyLevel = "Easy"; // Default level
    private Random random = new Random();

    private Map<String, List<String>> difficultyWords = new HashMap<String, List<String>>() {{
        put("Easy", Arrays.asList("Hola", "Adiós", "Gracias", "Perro", "Gato"));
        put("Medium", Arrays.asList("¿Cómo estás?", "Me gusta el español", "Voy a la tienda"));
        put("Hard", Arrays.asList("El clima es muy bueno hoy", "Necesito ayuda con mi tarea", "Mañana voy a viajar a España"));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spanish_speech_recognition);

        txtWord = findViewById(R.id.txt_word);
        txtFeedback = findViewById(R.id.txt_feedback);
        txtScore = findViewById(R.id.txt_score);
        txtDifficulty = findViewById(R.id.txt_difficulty);
        btnListen = findViewById(R.id.btn_listen);
        btnSpeak = findViewById(R.id.btn_speak);
        btnNext = findViewById(R.id.btn_next);
        btnChangeDifficulty = findViewById(R.id.btn_change_difficulty);

        // Initialize TTS
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(new Locale("es", "ES")); // Spanish
            }
        });

        btnListen.setOnClickListener(v -> speakWord());
        btnSpeak.setOnClickListener(v -> startSpeechRecognition());
        btnNext.setOnClickListener(v -> selectNewPhrase());
        btnChangeDifficulty.setOnClickListener(v -> changeDifficulty());

        selectNewPhrase();
    }

    private void selectNewPhrase() {
        List<String> words = difficultyWords.get(difficultyLevel);
        targetPhrase = words.get(random.nextInt(words.size()));
        txtWord.setText(targetPhrase);
        txtFeedback.setText("Try pronouncing the phrase.");
    }

    private void speakWord() {
        tts.speak(targetPhrase, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronounce: " + targetPhrase);

        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Speech recognition not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                String userSpeech = results.get(0).toLowerCase();
                int similarity = calculateSimilarity(targetPhrase.toLowerCase(), userSpeech);
                if (similarity >= 80) { // Threshold for correct pronunciation
                    txtFeedback.setText("✅ Perfect pronunciation! Similarity: " + similarity + "%");
                    score++;
                } else {
                    txtFeedback.setText("❌ Try again! You said: " + userSpeech + " (Similarity: " + similarity + "%)");
                }
                txtScore.setText("Score: " + score);
            }
        }
    }

    private int calculateSimilarity(String original, String spoken) {
        int maxLength = Math.max(original.length(), spoken.length());
        int minLength = Math.min(original.length(), spoken.length());
        int matches = 0;

        for (int i = 0; i < minLength; i++) {
            if (original.charAt(i) == spoken.charAt(i)) {
                matches++;
            }
        }

        return (int) (((double) matches / maxLength) * 100);
    }

    private void changeDifficulty() {
        if (difficultyLevel.equals("Easy")) {
            difficultyLevel = "Medium";
        } else if (difficultyLevel.equals("Medium")) {
            difficultyLevel = "Hard";
        } else {
            difficultyLevel = "Easy";
        }
        txtDifficulty.setText("Difficulty: " + difficultyLevel);
        selectNewPhrase();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
