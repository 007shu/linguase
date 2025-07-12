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

public class SpanishPictureRecognitionActivity extends AppCompatActivity {

    private ImageView imageView;
    private RadioGroup optionsGroup;
    private Button nextButton, submitButton;

    private final Question[] questions = {
            new Question(R.drawable.paella, "Paella", new String[]{"Paella", "Tacos", "Sushi", "Pizza", "Arepa"}), // Cuisine
            new Question(R.drawable.guernica, "Guernica", new String[]{"Las Meninas", "El Grito", "Guernica", "La Gioconda", "El Jardín de las Delicias"}), // Art
            new Question(R.drawable.madrid, "Madrid", new String[]{"Barcelona", "Madrid", "Sevilla", "Valencia", "Bilbao"}), // Cities
            new Question(R.drawable.independencia, "1810", new String[]{"1492", "1810", "1914", "1789", "1605"}), // History (Spanish Independence)
            new Question(R.drawable.alhambra, "Alhambra", new String[]{"Sagrada Familia", "Alhambra", "Mezquita de Córdoba", "Prado", "Acueducto de Segovia"}) // Landmarks
    };


    private int currentQuestionIndex = 0;
    private boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spanish_picture_recognition);

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
            Toast.makeText(this, "¡Quiz terminado!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "¡Incorrecto! La respuesta correcta es: " + correctAnswer, Toast.LENGTH_SHORT).show();
            }

            answered = true;
            nextButton.setEnabled(true);
            submitButton.setEnabled(false);
        } else {
            Toast.makeText(this, "¡Seleccione una respuesta!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNextQuestion() {
        if (answered) {
            currentQuestionIndex++;
            loadQuestion();
        } else {
            Toast.makeText(this, "¡Envíe su respuesta primero!", Toast.LENGTH_SHORT).show();
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
