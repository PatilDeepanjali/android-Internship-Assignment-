package com.example.aura;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aura.LaundryShopAdapter;
import com.example.aura.LaundryShop;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

public class NearbyActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 100;

    // Fallback center (San Francisco) used until we get a real GPS fix or
    // shops get real lat/lng — matches the "San Francisco" location shown
    // on the Home screen header.
    private static final double DEFAULT_LAT = 37.7749;
    private static final double DEFAULT_LNG = -122.4194;
    private static final double DEFAULT_ZOOM = 14.0;

    private MapView mapView;
    private LinearLayout btnUseMyLocation;
    private RecyclerView rvNearbyShops;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // osmdroid requires a configuration load + a user agent before any
        // MapView is touched, or tile requests get silently blocked/throttled.
        Configuration.getInstance().load(getApplicationContext(),
                androidx.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_nearby);

        bindViews();
        setupMap();
        setupClickListeners();
        loadNearbyShops();
        setupBottomNav();
    }

    private void bindViews() {
        mapView = findViewById(R.id.mapView);
        btnUseMyLocation = findViewById(R.id.btnUseMyLocation);
        rvNearbyShops = findViewById(R.id.rvNearbyShops);
        bottomNav = findViewById(R.id.bottomNav);
    }

    private void setupMap() {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(DEFAULT_ZOOM);
        mapView.getController().setCenter(new GeoPoint(DEFAULT_LAT, DEFAULT_LNG));
        // TODO: once LaundryShop has real lat/lng fields, add osmdroid Markers
        // here per shop instead of the static ImageView pins in the XML, e.g.:
        // Marker marker = new Marker(mapView);
        // marker.setPosition(new GeoPoint(shop.getLat(), shop.getLng()));
        // mapView.getOverlays().add(marker);
    }

    private void setupClickListeners() {
        btnUseMyLocation.setOnClickListener(v -> centerOnDeviceLocation());
    }

    private void centerOnDeviceLocation() {
        boolean hasFineLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!hasFineLocation) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
            return;
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location lastKnown = null;
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                lastKnown = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (lastKnown == null && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                lastKnown = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        } catch (SecurityException e) {
            // Permission was revoked between the check above and this call
            // (rare, but possible) — fail quietly rather than crashing.
        }

        if (lastKnown != null) {
            GeoPoint here = new GeoPoint(lastKnown.getLatitude(), lastKnown.getLongitude());
            mapView.getController().animateTo(here);
            mapView.getController().setZoom(16.0);
        } else {
            Toast.makeText(this,
                    "Couldn't get your location yet — make sure GPS is on and try again",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions,
                                           @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            centerOnDeviceLocation();
        } else if (requestCode == REQUEST_LOCATION_PERMISSION) {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // osmdroid requirement — without this the map tiles can freeze or
        // fail to redraw after returning from another activity/tab.
        if (mapView != null) mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // osmdroid requirement — releases tile-loading threads cleanly.
        if (mapView != null) mapView.onPause();
    }

    private void loadNearbyShops() {
        List<LaundryShop> shops = new ArrayList<>();
        shops.add(new LaundryShop("1", "Dhobi Laundry", "1810, Camino Real, New York", 4.8f, null, "0.4 km away"));
        shops.add(new LaundryShop("2", "Quick Wash Co.", "22 Baker Street, New York", 4.5f, null, "0.8 km away"));
        shops.add(new LaundryShop("3", "Sparkle Cleaners", "9 Fifth Avenue, New York", 4.6f, null, "1.2 km away"));
        shops.add(new LaundryShop("4", "FreshFold Laundromat", "310 Lexington Ave, New York", 4.3f, null, "1.6 km away"));
        // TODO: replace with a real location query (Firestore "shops" collection
        // filtered/sorted by distance from the device's current GPS location, or
        // a REST API call) once the map is upgraded from static to live.

        rvNearbyShops.setLayoutManager(new LinearLayoutManager(this));
        rvNearbyShops.setAdapter(new LaundryShopAdapter(shops, new LaundryShopAdapter.OnShopClickListener() {
            @Override
            public void onShopClick(LaundryShop shop) {
                openShopDetail(shop);
            }

            @Override
            public void onBookNowClick(LaundryShop shop) {
                openShopDetail(shop);
            }
        }));

        // item_laundry_shop.xml is now match_parent width by default (Home's
        // list was switched to vertical too), so the manual stretch-after-attach
        // workaround is no longer needed here. Kept as a no-op comment for
        // anyone who sees the old approach in version history.
    }

    /**
     * LaundryShop isn't Parcelable/Serializable, so individual fields are passed
     * as Intent extras instead. Mirrors HomeActivity's identical method.
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
     * Same shared bottom_nav.xml pattern as HomeActivity/OffersActivity.
     * No finish() here — leaving previous tab activities alive in the back
     * stack lets the system back button step back one tab at a time.
     */
    private void setupBottomNav() {
        bottomNav.setSelectedItemId(R.id.nav_nearby);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_nearby) {
                return true; // already here
            } else if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
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