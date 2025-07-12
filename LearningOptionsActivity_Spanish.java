package com.example.myapplicationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LearningOptionsActivity_Spanish extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_options_spanish);

        Button btnQuiz = findViewById(R.id.btn_quiz);
        Button btnCrossword = findViewById(R.id.btn_crossword);
        Button btnPictureRecognition = findViewById(R.id.btn_picture_recognition);
        Button btnSpeechRecognition = findViewById(R.id.btn_speech_recognition);

        btnQuiz.setOnClickListener(v -> startActivity(new Intent(this, SpanishQuizActivity.class)));
        btnCrossword.setOnClickListener(v -> startActivity(new Intent(this, SpanishCrosswordActivity.class)));
        btnPictureRecognition.setOnClickListener(v -> startActivity(new Intent(this, SpanishPictureRecognitionActivity.class)));
        btnSpeechRecognition.setOnClickListener(v -> startActivity(new Intent(this, SpanishSpeechRecognitionActivity.class)));
    }
}