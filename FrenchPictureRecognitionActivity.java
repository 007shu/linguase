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

public class FrenchPictureRecognitionActivity extends AppCompatActivity {

    private ImageView imageView;
    private RadioGroup optionsGroup;
    private Button nextButton, submitButton;

    private final Question[] questions = {
            // Cuisine
            new Question(R.drawable.baguette, "Baguette", new String[]{"Croissant", "Baguette", "Fromage", "Vin", "Macaron"}),

            // Art
            new Question(R.drawable.monalisa, "Mona Lisa", new String[]{"Guernica", "Mona Lisa", "Le Cri", "Les Tournesols", "La Nuit étoilée"}),

            // City
            new Question(R.drawable.eiffeltower, "Paris", new String[]{"Lyon", "Marseille", "Paris", "Bordeaux", "Toulouse"}),

            // History
            new Question(R.drawable.napoleon, "Napoléon", new String[]{"Louis XIV", "Napoléon", "Charlemagne", "Clovis", "De Gaulle"}),

            // Famous Landmark
            new Question(R.drawable.notredame, "Notre-Dame", new String[]{"Mont Saint-Michel", "Château de Versailles", "Notre-Dame", "Arc de Triomphe", "Tour Eiffel"})
    };


    private int currentQuestionIndex = 0;
    private boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_french_picture_recongnition);

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
            Toast.makeText(this, "Quiz terminé!", Toast.LENGTH_LONG).show();
            nextButton.setEnabled(false);
            submitButton.setEnabled(false);
            return;
        }

        Question question = questions[currentQuestionIndex];

        imageView.setImageResource(question.imageResId);

        // Shuffle options to randomize order
        List<String> shuffledOptions = new ArrayList<>(Arrays.asList(question.options));
        Collections.shuffle(shuffledOptions);

        optionsGroup.removeAllViews(); // Clear previous options

        // Ensure exactly 5 options
        for (String option : shuffledOptions) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioButton.setTextSize(18);
            radioButton.setPadding(10, 10, 10, 10);
            optionsGroup.addView(radioButton);
        }

        optionsGroup.clearCheck(); // Reset selection
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
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Incorrect! La bonne réponse est : " + correctAnswer, Toast.LENGTH_SHORT).show();
            }

            answered = true;
            nextButton.setEnabled(true);
            submitButton.setEnabled(false);
        } else {
            Toast.makeText(this, "Sélectionnez une réponse!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNextQuestion() {
        if (answered) {
            currentQuestionIndex++;
            loadQuestion();
        } else {
            Toast.makeText(this, "Soumettez d'abord votre réponse!", Toast.LENGTH_SHORT).show();
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
