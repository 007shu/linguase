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

public class GermanPictureRecognitionActivity extends AppCompatActivity {

    private ImageView imageView;
    private RadioGroup optionsGroup;
    private Button nextButton, submitButton;

    private final Question[] questions = {
            new Question(R.drawable.brezel, "Welche dieser Speisen gehört zur deutschen Küche?",
                    new String[]{"Brezel","Beethoven","Berlin","Brandenburger Tor"}),

            new Question(R.drawable.beethoven, "Welcher berühmte deutsche Komponist schrieb die 9. Sinfonie?",
                    new String[]{"Beethoven","Brezel","Berlin","Brandenburger Tor"}),

            new Question(R.drawable.berlin, "Welche Stadt ist die Hauptstadt Deutschlands?",
                    new String[]{"Berlin","Beethoven","Brandenburger Tor","Brezel"}),

            new Question(R.drawable.brandenburger_tor, "Welches Wahrzeichen steht in Berlin?",
                    new String[]{"Brandenburger Tor","Berlin","Beethoven","Brezel"}),

            new Question(R.drawable.berliner_mauer, "Wann fiel die Berliner Mauer?",
                    new String[]{"1989", "1991", "1993","1990"})
    };


    private int currentQuestionIndex = 0;
    private boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_german_picture_recognition);

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
            Toast.makeText(this, "Quiz beendet!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(this, "Richtig!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Falsch! Die richtige Antwort ist: " + correctAnswer, Toast.LENGTH_SHORT).show();
            }

            answered = true;
            nextButton.setEnabled(true);
            submitButton.setEnabled(false);
        } else {
            Toast.makeText(this, "Bitte eine Antwort auswählen!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNextQuestion() {
        if (answered) {
            currentQuestionIndex++;
            loadQuestion();
        } else {
            Toast.makeText(this, "Bitte zuerst die Antwort einreichen!", Toast.LENGTH_SHORT).show();
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
