package com.example.aura;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private CheckBox cbTerms;
    private TextView tvLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db    = FirebaseFirestore.getInstance();

        initViews();
        setListeners();
    }

    private void initViews() {
        etFullName        = findViewById(R.id.etFullName);
        etEmail           = findViewById(R.id.etEmail);
        etPassword        = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp         = findViewById(R.id.btnSignUp);
        cbTerms           = findViewById(R.id.cbTerms);
        tvLogin           = findViewById(R.id.tvLogin);
    }

    private void setListeners() {

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        btnSignUp.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name     = etFullName.getText().toString().trim();
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm  = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etFullName.setError("Enter your name");
            etFullName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter email");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("Min 6 characters");
            etPassword.requestFocus();
            return;
        }
        if (!password.equals(confirm)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }
        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please agree to Terms & Conditions",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        btnSignUp.setEnabled(false);
        btnSignUp.setText("Creating account...");

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Save user data to Firestore
                        String uid = mAuth.getCurrentUser().getUid();
                        Map<String, Object> user = new HashMap<>();
                        user.put("name",  name);
                        user.put("email", email);
                        user.put("uid",   uid);

                        db.collection("users").document(uid)
                                .set(user)
                                .addOnSuccessListener(a -> {
                                    Toast.makeText(this,
                                            "Account created successfully!",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, LoginActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                    finish();
                                });
                    } else {
                        btnSignUp.setEnabled(true);
                        btnSignUp.setText("Sign Up");
                        String err = task.getException() != null
                                ? task.getException().getMessage()
                                : "Registration failed";
                        Toast.makeText(this, err, Toast.LENGTH_LONG).show();
                    }
                });
    }
}