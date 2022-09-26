package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button logout = findViewById(R.id.logout);
        TextView mail = findViewById(R.id.mail);
        TextView name = findViewById(R.id.name);
        mail.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        logout.setOnClickListener(v -> SignOut());
    }

    private void SignOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
    }
}