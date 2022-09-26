package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailRec;
    Button btnRec;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailRec = findViewById(R.id.emailRec);
        btnRec = findViewById(R.id.btnRec);
        auth = FirebaseAuth.getInstance();
        btnRec.setOnClickListener(v -> {
            String email = emailRec.getText().toString();
            if(TextUtils.isEmpty(email)){
                Toast.makeText(ForgotPasswordActivity.this, getString(R.string.please_enter_email), Toast.LENGTH_LONG).show();
            }
            else{
                auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.please_visit_email), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                    } else{
                        String message = Objects.requireNonNull(task.getException()).getMessage();
                        Toast.makeText(ForgotPasswordActivity.this, "" + message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

}