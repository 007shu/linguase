package com.example.myapplicationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JapanesePictureRecognitionActivity extends AppCompatActivity {

    private ImageView imageView;
    private RadioGroup optionsGroup;
    private Button nextButton, submitButton;

    private final Question[] questions = {
            new Question(R.drawable.sushi, "寿司", new String[]{"寿司", "ラーメン", "天ぷら", "うどん", "焼肉"}), // Cuisine
            new Question(R.drawable.kabuki, "歌舞伎", new String[]{"歌舞伎", "能", "狂言", "茶道", "華道"}), // Traditional Japanese Performing Art
            new Question(R.drawable.kyoto, "京都", new String[]{"京都", "大阪", "東京", "奈良", "福岡"}), // City
            new Question(R.drawable.samurai, "侍", new String[]{"侍", "忍者", "武士", "僧侶", "剣士"}), // History
            new Question(R.drawable.jupiter, "木星", new String[]{"木星", "地球", "火星", "金星", "土星"}) // Science
    };


    private int currentQuestionIndex = 0;
    private boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_japanese_picture_recognition);

        imageView = findViewById(R.id.imageView);
        optionsGroup = findViewById(R.id.optionsGroup);
        nextButton = findViewById(R.id.btnNext);
        submitButton = findViewById(R.id.btnSubmit);

        loadQuestion();

        submitButton.setOnClickListener(v -> checkAnswer());
        nextButton.setOnClickListener(v -> loadNextQuestion());
    }

    private void loadQuestion() {
        if (currentQuestionIndex >= questions.length) {
            Toast.makeText(this, "クイズ終了!", Toast.LENGTH_LONG).show();
            nextButton.setEnabled(false);
            submitButton.setEnabled(false);
            return;
        }

        Question question = questions[currentQuestionIndex];

        imageView.setImageResource(question.imageResId);

        List<String> shuffledOptions = new ArrayList<>(Arrays.asList(question.options));
        Collections.shuffle(shuffledOptions);

        optionsGroup.removeAllViews();

        for (String option : shuffledOptions) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioButton.setTextSize(18);
            radioButton.setPadding(10, 10, 10, 10);
            optionsGroup.addView(radioButton);
        }

        optionsGroup.clearCheck();
        answered = false;
        nextButton.setEnabled(false);
        submitButton.setEnabled(true);
    }

    private void checkAnswer() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedButton = findViewById(selectedId);
            String selectedAnswer = selectedButton.getText().toString();
            String correctAnswer = questions[currentQuestionIndex].correctAnswer;

            if (selectedAnswer.equals(correctAnswer)) {
                Toast.makeText(this, "正解!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "不正解! 正しい答えは: " + correctAnswer, Toast.LENGTH_SHORT).show();
            }

            answered = true;
            nextButton.setEnabled(true);
            submitButton.setEnabled(false);
        } else {
            Toast.makeText(this, "回答を選択してください!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNextQuestion() {
        if (answered) {
            currentQuestionIndex++;
            loadQuestion();
        } else {
            Toast.makeText(this, "まず回答を提出してください!", Toast.LENGTH_SHORT).show();
        }
    }

    private static class Question {
        int imageResId;
        String correctAnswer;
        String[] options;

        Question(int imageResId, String correctAnswer, String[] options) {
            this.imageResId = imageResId;
            this.correctAnswer = correctAnswer;
            this.options = options;
        }
    }
}
