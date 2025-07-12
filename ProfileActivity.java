package com.example.myapplicationapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private ImageView userAvatar;
    private EditText editUserName, editUserEmail;
    private ProgressBar progressGerman, progressFrench, progressSpanish, progressJapanese;
    private Button btnEditProfile, btnChooseAvatar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Views
        userAvatar = findViewById(R.id.user_avatar);
        editUserName = findViewById(R.id.edit_user_name);
        editUserEmail = findViewById(R.id.edit_user_email);
        progressGerman = findViewById(R.id.progress_german);
        progressFrench = findViewById(R.id.progress_french);
        progressSpanish = findViewById(R.id.progress_spanish);
        progressJapanese = findViewById(R.id.progress_japanese);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnChooseAvatar = findViewById(R.id.btn_choose_avatar);

        // Set Initial User Data (Example: Replace with dynamic data fetched from a database or API)
        editUserName.setText("John Doe");
        editUserEmail.setText("johndoe@example.com");
        progressGerman.setProgress(60);
        progressFrench.setProgress(40);
        progressSpanish.setProgress(80);
        progressJapanese.setProgress(20);

        // Button to Choose Avatar
        btnChooseAvatar.setOnClickListener(v -> {
            // Logic to open an avatar picker or gallery
            Toast.makeText(ProfileActivity.this, "Choose Avatar Clicked", Toast.LENGTH_SHORT).show();
        });

        // Save Profile Changes
        btnEditProfile.setOnClickListener(v -> {
            String userName = editUserName.getText().toString();
            String userEmail = editUserEmail.getText().toString();

            if (userName.isEmpty() || userEmail.isEmpty()) {
                Toast.makeText(ProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Save the data (to local storage, database, or an API)
                Toast.makeText(ProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

                // Update UI with the new data (if necessary)
                // Example: Display updated name and email
                editUserName.setText(userName);
                editUserEmail.setText(userEmail);
            }
        });
    }
}


