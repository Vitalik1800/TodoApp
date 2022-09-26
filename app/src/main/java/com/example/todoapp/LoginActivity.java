package com.example.todoapp;

import android.app.*;
import android.content.Intent;
import android.content.res.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.GoogleSignInActivity;
import com.example.todoapp.MainActivity;
import com.example.todoapp.RegisterActivity;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.*;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    Spinner lang;
    public static final String[] languages = {"Виберіть мову:", "Українська", "English", "Deutsch"};
    TextView tv_register, forgotPass;
    EditText emailEdt, passEdt;
    Button btnLogin;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseUser user;
    SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        lang = findViewById(R.id.lang);
        signInButton = findViewById(R.id.sign_in_button);
        emailEdt = findViewById(R.id.emailEdt);
        forgotPass = findViewById(R.id.forgotPass);
        passEdt = findViewById(R.id.passEdt);
        btnLogin = findViewById(R.id.btnLogin);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        tv_register = findViewById(R.id.tv_register);
        tv_register.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
        btnLogin.setOnClickListener(v -> PerformLogin());
        signInButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, GoogleSignInActivity.class)));
        forgotPass.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lang.setAdapter(adapter);
        lang.setSelection(0);
        lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLang = adapterView.getItemAtPosition(i).toString();
                switch (selectedLang) {
                    case "Українська":
                        setLocal(LoginActivity.this, "uk");
                        finish();
                        startActivity(getIntent());
                        languages[0] = "Виберіть мову:";
                        break;
                    case "English":
                        setLocal(LoginActivity.this, "en");
                        finish();
                        startActivity(getIntent());
                        languages[0] = "Choose a language:";
                        break;
                    case "Deutsch":
                        setLocal(LoginActivity.this, "de");
                        finish();
                        startActivity(getIntent());
                        languages[0] = "Wählen Sie eine Sprache:";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setLocal(Activity activity, String langCode){
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private void PerformLogin(){
        String email = emailEdt.getText().toString();
        String password = passEdt.getText().toString();
        if(!email.matches(emailPattern)){
            emailEdt.setError(getString(R.string.enter_email));
        } else if(password.isEmpty() || password.length() < 6){
            passEdt.setError(getString(R.string.enter_pass));
        } else {
            progressDialog.setMessage(getString(R.string.Register_login));
            progressDialog.setTitle(getString(R.string.Auth));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    sendUserToNextActivity();
                    Toast.makeText(LoginActivity.this, R.string.auth_success, Toast.LENGTH_LONG).show();
                } else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, " "+task.getException(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void sendUserToNextActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}