package com.example.aura;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aura.LaundryShopAdapter;
import com.example.aura.ServiceAdapter;
import com.example.aura.LaundryShop;
import com.example.aura.ServiceItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private TextView tvGreeting, tvLocation, tvChangeLocation;
    private ImageView ivBack;
    private CircleImageView ivProfile;
    private EditText etSearch;
    private RecyclerView rvServices, rvShops;
    private BottomNavigationView bottomNav;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        bindViews();
        setupClickListeners();
        loadUserProfile();
        setupServicesList();
        setupShopsList();
        setupBottomNav();
    }

    private void bindViews() {
        tvGreeting = findViewById(R.id.tvGreeting);
        tvLocation = findViewById(R.id.tvLocation);
        tvChangeLocation = findViewById(R.id.tvChangeLocation);
        ivBack = findViewById(R.id.ivBack);
        ivProfile = findViewById(R.id.ivProfile);
        etSearch = findViewById(R.id.etSearch);
        rvServices = findViewById(R.id.rvServices);
        rvShops = findViewById(R.id.rvShops);
        bottomNav = findViewById(R.id.bottomNav);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> onBackPressed());

        // TODO: replace with a real location picker once that screen exists, e.g.:
        // startActivityForResult(new Intent(this, LocationPickerActivity.class), REQUEST_LOCATION);
        tvChangeLocation.setOnClickListener(v ->
                android.widget.Toast.makeText(this, "Change location", android.widget.Toast.LENGTH_SHORT).show());
    }

    /**
     * Pulls display name + photo from Firebase Auth directly (fast, no extra read).
     * If you store extra profile fields (e.g. saved address/location) in Firestore,
     * the commented block below shows where to fetch that — Auth alone won't have it.
     */
    private void loadUserProfile() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user == null) {
            // Not logged in somehow — bounce back to LoginActivity rather than
            // showing a broken Home screen.
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String name = user.getDisplayName();
        tvGreeting.setText(getString(R.string.hello_user,
                (name != null && !name.isEmpty()) ? name.split(" ")[0] : "there"));

        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .placeholder(R.drawable.placeholder_avatar)
                    .into(ivProfile);
        }

        // Optional: fetch saved location from Firestore profile doc, e.g.:
        // firestore.collection("users").document(user.getUid()).get()
        //     .addOnSuccessListener(doc -> {
        //         String city = doc.getString("city");
        //         if (city != null) tvLocation.setText(city);
        //     });
    }

    private void setupServicesList() {
        List<ServiceItem> services = new ArrayList<>();
        services.add(new ServiceItem("Wash & Fold", R.drawable.ic_wash));
        services.add(new ServiceItem("Wash & Iron", R.drawable.ic_iron));
        services.add(new ServiceItem("Dry Clean", R.drawable.ic_hanger));
        services.add(new ServiceItem("Premium", R.drawable.ic_star));
        // TODO: replace this hardcoded list with a Firestore "services" collection read
        // once you decide on the backend source — adapter and model already support it.

        rvServices.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvServices.setAdapter(new ServiceAdapter(services, (item, position) -> {
            // TODO: navigate to a filtered shop list for this service type
        }));
    }

    private void setupShopsList() {
        List<LaundryShop> shops = new ArrayList<>();
        shops.add(new LaundryShop("1", "Dhobi Laundry", "1810, Camino Real, New York", 4.8f, null));
        shops.add(new LaundryShop("2", "Quick Wash Co.", "22 Baker Street, New York", 4.5f, null));
        // TODO: replace with Firestore "shops" collection or REST API call.

        // CHANGED: this list is now a full-width vertical stack per the design
        // (was LinearLayoutManager.HORIZONTAL before). nestedScrollingEnabled is
        // set to false in the XML since this RecyclerView lives inside a
        // NestedScrollView and shouldn't scroll independently.
        rvShops.setLayoutManager(new LinearLayoutManager(this));
        rvShops.setAdapter(new LaundryShopAdapter(shops, new LaundryShopAdapter.OnShopClickListener() {
            @Override
            public void onShopClick(LaundryShop shop) {
                openShopDetail(shop);
            }

            @Override
            public void onBookNowClick(LaundryShop shop) {
                openShopDetail(shop);
            }
        }));
    }

    /**
     * LaundryShop isn't Parcelable/Serializable, so individual fields are passed
     * as Intent extras instead. Mirror this exact method in NearbyActivity and
     * any other screen that launches ShopDetailActivity.
     */
    private void openShopDetail(LaundryShop shop) {
        Intent intent = new Intent(this, ShopDetailActivity.class);
        intent.putExtra(ShopDetailActivity.EXTRA_SHOP_ID, shop.getId());
        intent.putExtra(ShopDetailActivity.EXTRA_SHOP_NAME, shop.getName());
        intent.putExtra(ShopDetailActivity.EXTRA_SHOP_ADDRESS, shop.getAddress());
        intent.putExtra(ShopDetailActivity.EXTRA_SHOP_RATING, shop.getRating());
        intent.putExtra(ShopDetailActivity.EXTRA_SHOP_IMAGE_URL, shop.getImageUrl());
        intent.putExtra(ShopDetailActivity.EXTRA_SHOP_OPENING_HOURS, shop.getOpeningHours());
        intent.putExtra(ShopDetailActivity.EXTRA_SHOP_OPEN_NOW, shop.isOpenNow());
        startActivity(intent);
    }

    /**
     * Shared bottom nav. Since this same bottom_nav.xml is included on every
     * activity, repeat this exact block in each one, just changing the "current"
     * case to match that screen. NOTE: no finish() here on purpose — leaving the
     * previous tab activity alive in the back stack is what makes the system
     * back button step back one tab at a time instead of exiting the app.
     */
    private void setupBottomNav() {
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                return true; // already here
            } else if (id == R.id.nav_nearby) {
                startActivity(new Intent(this, NearbyActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_booking) {
                startActivity(new Intent(this, MedicalActivity.class));
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