package com.example.aura;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aura.OfferAdapter;
import com.example.aura.Offer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class OffersActivity extends AppCompatActivity {

    private RecyclerView rvOffers;
    private LinearLayout layoutEmpty;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        bindViews();
        loadOffers();
        setupBottomNav();
    }

    private void bindViews() {
        rvOffers = findViewById(R.id.rvOffers);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        bottomNav = findViewById(R.id.bottomNav);
    }

    private void loadOffers() {
        List<Offer> offers = new ArrayList<>();
        offers.add(new Offer(
                "1",
                "FOR A LIMITED TIME",
                "Get 30% OFF\non Dry Cleaning",
                "Valid on all orders above $25.",
                "LAUNDRY30",
                "Expires in 3 days"
        ));
        offers.add(new Offer(
                "2",
                "NEW USER OFFER",
                "Flat $10 OFF\non your first order",
                "No minimum order value required.",
                "WELCOME10",
                null
        ));
        offers.add(new Offer(
                "3",
                "WEEKEND SPECIAL",
                "20% OFF\non Wash & Iron",
                "Valid Saturday and Sunday only.",
                "WEEKEND20",
                "Expires in 6 days"
        ));
        // TODO: replace this hardcoded list with a Firestore "offers" collection
        // read (or REST call) once the backend source is decided — adapter and
        // model already support swapping this in without other changes.

        if (offers.isEmpty()) {
            rvOffers.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
            return;
        }

        rvOffers.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);
        rvOffers.setLayoutManager(new LinearLayoutManager(this));
        rvOffers.setAdapter(new OfferAdapter(offers, this::copyCodeToClipboard));
    }

    private void copyCodeToClipboard(Offer offer) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("promo_code", offer.getPromoCode());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Code \"" + offer.getPromoCode() + "\" copied", Toast.LENGTH_SHORT).show();
    }

    /**
     * Same shared bottom_nav.xml pattern as HomeActivity. No finish() here —
     * leaving previous tab activities alive in the back stack lets the system
     * back button step back one tab at a time instead of exiting the app.
     */
    private void setupBottomNav() {
        bottomNav.setSelectedItemId(R.id.nav_offers);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_booking) {
                return true; // already here
            } else if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_nearby) {
                startActivity(new Intent(this, NearbyActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_offers) {
                startActivity(new Intent(this, OffersActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, SplitterActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }
}