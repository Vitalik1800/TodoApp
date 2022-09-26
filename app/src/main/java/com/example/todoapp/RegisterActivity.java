package com.example.todoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText emailReg, passReg, confPassReg;
    Button btnRegister;
    TextView login;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailReg = findViewById(R.id.emailReg);
        passReg = findViewById(R.id.passReg);
        confPassReg = findViewById(R.id.confPassReg);
        btnRegister = findViewById(R.id.btnRegister);
        login = findViewById(R.id.login);
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        login.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
        btnRegister.setOnClickListener(v -> PerformAuth());
    }

    private void PerformAuth(){
        String email = emailReg.getText().toString();
        String password = passReg.getText().toString();
        String confirmPassword = confPassReg.getText().toString();
        if(!email.matches(emailPattern)){
            emailReg.setError(getString(R.string.enter_email));
        } else if(password.isEmpty() || password.length() < 6){
            passReg.setError(getString(R.string.enter_pass));
        } else if(!password.equals(confirmPassword)){
            confPassReg.setError(getString(R.string.pass_not_matched));
        } else{
            progressDialog.setMessage(getString(R.string.Register_wait));
            progressDialog.setTitle(getString(R.string.Register));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    sendUserToNextActivity();
                    Toast.makeText(RegisterActivity.this, R.string.register_success, Toast.LENGTH_LONG).show();
                } else{
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, " "+task.getException(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void sendUserToNextActivity(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}