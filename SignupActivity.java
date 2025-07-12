package com.example.myapplicationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    private EditText emailSignup, passwordSignup;
    private Button signupButton;
    private TextView loginRedirect;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailSignup = findViewById(R.id.emailSignup);
        passwordSignup = findViewById(R.id.passwordSignup);
        signupButton = findViewById(R.id.signupButton);
        loginRedirect = findViewById(R.id.loginRedirect);
        auth = FirebaseAuth.getInstance();

        signupButton.setOnClickListener(v -> {
            String email = emailSignup.getText().toString().trim();
            String password = passwordSignup.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(SignupActivity.this, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(SignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            }
        });

        loginRedirect.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }
}

