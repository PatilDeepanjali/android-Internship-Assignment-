package com.example.aura;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoogle, btnApple;
    private TextView tvForgotPassword, tvSignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        initViews();
        setListeners();
    }

    private void initViews() {
        etEmail          = findViewById(R.id.etEmail);
        etPassword       = findViewById(R.id.etPassword);
        btnLogin         = findViewById(R.id.btnLogin);
        btnGoogle        = findViewById(R.id.btnGoogle);
        btnApple         = findViewById(R.id.btnApple);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvSignUp         = findViewById(R.id.tvSignUp);
    }

    private void setListeners() {

        btnLogin.setOnClickListener(v -> loginUser());

        tvSignUp.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));

        tvForgotPassword.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Enter email first");
                etEmail.requestFocus();
                return;
            }
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this,
                                    "Reset link sent to " + email,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this,
                                    "Failed. Check your email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnGoogle.setOnClickListener(v ->
                Toast.makeText(this, "Google Sign-In coming soon",
                        Toast.LENGTH_SHORT).show());

        btnApple.setOnClickListener(v ->
                Toast.makeText(this, "Apple Sign-In coming soon",
                        Toast.LENGTH_SHORT).show());
    }

    private void loginUser() {
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter email");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter password");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Min 6 characters");
            etPassword.requestFocus();
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Logging in...");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Login →");
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Welcome back!",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, HomeActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    } else {
                        String err = task.getException() != null
                                ? task.getException().getMessage()
                                : "Login failed";
                        Toast.makeText(this, err,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}