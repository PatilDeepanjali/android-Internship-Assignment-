package com.example.aura;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SplitterActivity extends AppCompatActivity {

    private static final String TAG = "SplitterActivity";

    private TextView tvGreeting, tvTotalBill;
    private Button btnSplitNow;
    private RecyclerView rvRecentSplits;
    private BottomNavigationView bottomNav;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private final List<Split> splitList = new ArrayList<>();
    private SplitAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_splitter);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initViews();
        setupBottomNav();
    }


    @Override
    protected void onStart() {
        super.onStart();

//        FirebaseUser user = mAuth.getCurrentUser();
//
//        if (user != null) {
//            loadUserData(user);
//        }
//
//        loadRecentSplits();
    }

    private void initViews() {
        tvGreeting = findViewById(R.id.tvGreeting);
        tvTotalBill = findViewById(R.id.tvTotalBill);
        btnSplitNow = findViewById(R.id.btnSplitNow);
        rvRecentSplits = findViewById(R.id.rvRecentSplits);
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setSelectedItemId(R.id.nav_booking);

//


        adapter = new SplitAdapter(splitList);
        rvRecentSplits.setLayoutManager(new LinearLayoutManager(this));
        rvRecentSplits.setAdapter(adapter);

// Static Data
        splitList.clear();

        splitList.add(new Split("Team Dinner", 3900, "Fox"));
        splitList.add(new Split("Single Dinner", 3000, "Hope"));
        splitList.add(new Split("Birthday Party", 5500, "John"));
        splitList.add(new Split("Office Lunch", 2200, "Smit"));
        splitList.add(new Split("Weekend Trip", 4800, "Fox"));

        adapter.notifyDataSetChanged();

// Total Bill
        tvTotalBill.setText("$19400");

        btnSplitNow.setOnClickListener(v ->
                Toast.makeText(this, "Splitting bill equally!", Toast.LENGTH_SHORT).show());
    }

//    private void loadUserData(FirebaseUser user) {
//        db.collection("users").document(user.getUid()).get()
//                .addOnSuccessListener(doc -> {
//                    if (doc.exists()) {
//                        String name = doc.getString("name");
//                        if (name != null && !name.isEmpty()) {
//                            String firstName = name.split(" ")[0];
//                            tvGreeting.setText(getString(R.string.greeting_format, firstName));
//                        }
//                    } else {
//                        Log.w(TAG, "User document does not exist for uid=" + user.getUid());
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Log.e(TAG, "Failed to load user data", e);
//                    Toast.makeText(this, "Couldn't load your profile: " + e.getMessage(),
//                            Toast.LENGTH_SHORT).show();
//                });
//    }

//    private void loadRecentSplits() {
//
//        Log.d(TAG, "Loading splits...");
//
//        db.collection("splits")
//                .get()
//                .addOnSuccessListener(snapshot -> {
//
//                    Log.d(TAG, "Documents found = " + snapshot.size());
//
//                    splitList.clear();
//
//                    double total = 0;
//
//                    for (QueryDocumentSnapshot doc : snapshot) {
//
//                        Log.d(TAG, "Document: " + doc.getData());
//
//                        String title = doc.getString("title");
//                        String paidBy = doc.getString("paidBy");
//
//                        Number amountNumber = doc.get("amount", Number.class);
//
//                        if (title != null && amountNumber != null) {
//
//                            double amount = amountNumber.doubleValue();
//
//                            splitList.add(new Split(title, amount, paidBy));
//
//                            total += amount;
//                        }
//                    }
//
//                    Log.d(TAG, "List Size = " + splitList.size());
//
//                    adapter.notifyDataSetChanged();
//
//                    tvTotalBill.setText(String.format("$%.2f", total));
//
//                })
//                .addOnFailureListener(e -> {
//                    Log.e(TAG, "Firestore Error", e);
//                });
//    }
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
                startActivity(new Intent(this, MedicalActivity.class));
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