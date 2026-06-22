package com.example.aura;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MedicalActivity extends AppCompatActivity {

    private TextView tvScreenTitle, tvSeeAllTopics;
    private ImageView ivSearch;
    private CircleImageView ivAvatar;
    private BottomNavigationView bottomNav;

    private CardView cardRegistered, cardInquiry, cardPharmacy, cardLecture;
    private CardView cardTopic1, cardTopic2;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_medical);

        mAuth = FirebaseAuth.getInstance();

        initViews();
        setupClickListeners();
        setupBottomNav();
    }

    private void initViews() {
        tvScreenTitle = findViewById(R.id.tvScreenTitle);
        tvSeeAllTopics = findViewById(R.id.tvSeeAllTopics);
        ivSearch = findViewById(R.id.ivSearch);
        ivAvatar = findViewById(R.id.ivAvatar);
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_profile);

        cardRegistered = findViewById(R.id.cardRegistered);
        cardInquiry = findViewById(R.id.cardInquiry);
        cardPharmacy = findViewById(R.id.cardPharmacy);
        cardLecture = findViewById(R.id.cardLecture);
        cardTopic1 = findViewById(R.id.cardTopic1);
        cardTopic2 = findViewById(R.id.cardTopic2);
    }

    private void setupClickListeners() {
        ivSearch.setOnClickListener(v -> openSearch());
        ivAvatar.setOnClickListener(v -> openProfile());

        tvSeeAllTopics.setOnClickListener(v ->
                Toast.makeText(this, "Today's topics — see all", Toast.LENGTH_SHORT).show());

        cardRegistered.setOnClickListener(v ->
                Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show());
        cardInquiry.setOnClickListener(v ->
                Toast.makeText(this, "Inquiry", Toast.LENGTH_SHORT).show());
        cardPharmacy.setOnClickListener(v ->
                Toast.makeText(this, "Pharmacy", Toast.LENGTH_SHORT).show());
        cardLecture.setOnClickListener(v ->
                Toast.makeText(this, "Lecture", Toast.LENGTH_SHORT).show());

        cardTopic1.setOnClickListener(v ->
                Toast.makeText(this, "Participation in Extracurricular Activities", Toast.LENGTH_SHORT).show());
        cardTopic2.setOnClickListener(v ->
                Toast.makeText(this, "Understanding Modern Depression Management", Toast.LENGTH_SHORT).show());
    }

    private void openSearch() {
        // TODO: replace with your real search screen once it exists, e.g.:
        // startActivity(new Intent(this, SearchActivity.class));
        Toast.makeText(this, "Open search", Toast.LENGTH_SHORT).show();
    }

    private void openProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Please sign in to view your profile", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO: replace with your real profile/edit screen once it exists, e.g.:
        // startActivity(new Intent(this, ProfileActivity.class));
        Toast.makeText(this, "Open profile for " + user.getUid(), Toast.LENGTH_SHORT).show();
    }

    private void setupBottomNav() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (id == R.id.nav_nearby) {
                startActivity(new Intent(this, NearbyActivity.class));
                return true;
            } else if (id == R.id.nav_booking) {
                startActivity(new Intent(this, SplitterActivity.class));
                return true;
            } else if (id == R.id.nav_offers) {
                startActivity(new Intent(this, OffersActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }
}