package com.example.myapplicationapp;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

public class JapaneseQuizActivity extends AppCompatActivity {
    private Interpreter tflite;
    private TextView questionTextView;
    private RadioGroup optionsGroup;
    private Button submitButton;
    private String[] questions = {
            "What is the capital of Japan?",
            "How do you say 'apple' in Japanese?"
    };
    private String[][] options = {
            {"ロンドン", "東京", "マドリード", "ローマ"},
            {"リンゴ", "オレンジ", "バナナ", "ブドウ"}
    };
    private int questionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_french_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        optionsGroup = findViewById(R.id.optionsGroup);
        submitButton = findViewById(R.id.submitButton);

        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadQuestion();

        submitButton.setOnClickListener(v -> checkAnswer());
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd("model_Japanese.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.getStartOffset(), fileDescriptor.getDeclaredLength());
    }

    private void loadQuestion() {
        if (questionIndex < questions.length) {
            questionTextView.setText(questions[questionIndex]);
            optionsGroup.removeAllViews();
            for (String option : options[questionIndex]) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(option);
                optionsGroup.addView(radioButton);
            }
        } else {
            questionTextView.setText("Quiz finished!");
            submitButton.setEnabled(false);
        }
    }

    private void checkAnswer() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();
        if (selectedId == -1) return;

        int predictedIndex = predictAnswer(questions[questionIndex]);
        int correctIndex = Arrays.asList(options[questionIndex]).indexOf(options[questionIndex][predictedIndex]);

        RadioButton selectedRadioButton = findViewById(selectedId);
        String selectedAnswer = selectedRadioButton.getText().toString();
        String correctAnswer = options[questionIndex][correctIndex];

        if (selectedAnswer.equals(correctAnswer)) {
            questionTextView.setText("Correct!");
        } else {
            questionTextView.setText("Incorrect. Try again.");
        }

        questionIndex++;
        loadQuestion();
    }

    private int predictAnswer(String question) {
        float[][] input = new float[1][20];  // Assume padded sequence size
        float[][] output = new float[1][4];  // 4 answer choices

        tflite.run(input, output);
        return getMaxIndex(output[0]);
    }

    private int getMaxIndex(float[] arr) {
        int maxIndex = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > arr[maxIndex]) maxIndex = i;
        }
        return maxIndex;
    }
}
