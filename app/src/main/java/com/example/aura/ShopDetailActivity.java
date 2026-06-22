package com.example.aura;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aura.ServicePriceAdapter;
import com.example.aura.ServicePrice;

import java.util.ArrayList;
import java.util.List;

public class ShopDetailActivity extends AppCompatActivity {

    // Intent extra keys — used by HomeActivity/NearbyActivity when launching this screen
    public static final String EXTRA_SHOP_ID = "shop_id";
    public static final String EXTRA_SHOP_NAME = "shop_name";
    public static final String EXTRA_SHOP_ADDRESS = "shop_address";
    public static final String EXTRA_SHOP_RATING = "shop_rating";
    public static final String EXTRA_SHOP_IMAGE_URL = "shop_image_url";
    public static final String EXTRA_SHOP_OPENING_HOURS = "shop_opening_hours";
    public static final String EXTRA_SHOP_OPEN_NOW = "shop_open_now";

    private ImageView ivHeaderImage, btnBack;
    private TextView tvHeaderRating, tvShopName, tvShopAddress, tvOpenStatus, tvOpeningHours;
    private RecyclerView rvServicePrices;
    private android.widget.Button btnBookNow;

    private String shopId, shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        bindViews();
        loadShopFromIntent();
        setupServicePrices();
        setupActions();
    }

    private void bindViews() {
        ivHeaderImage = findViewById(R.id.ivShopHeaderImage);
        btnBack = findViewById(R.id.btnBack);
        tvHeaderRating = findViewById(R.id.tvHeaderRating);
        tvShopName = findViewById(R.id.tvShopName);
        tvShopAddress = findViewById(R.id.tvShopAddress);
        tvOpenStatus = findViewById(R.id.tvOpenStatus);
        tvOpeningHours = findViewById(R.id.tvOpeningHours);
        rvServicePrices = findViewById(R.id.rvServicePrices);
        btnBookNow = findViewById(R.id.btnBookNow);
    }

    /**
     * Reads shop details passed via Intent extras from HomeActivity/NearbyActivity.
     * LaundryShop itself isn't Parcelable/Serializable, so individual fields are
     * passed instead — simplest, least error-prone for a model this small.
     * If LaundryShop grows more fields later, consider implementing Parcelable
     * on it directly rather than adding more extras here.
     */
    private void loadShopFromIntent() {
        Intent intent = getIntent();

        shopId = intent.getStringExtra(EXTRA_SHOP_ID);
        shopName = intent.getStringExtra(EXTRA_SHOP_NAME);
        String address = intent.getStringExtra(EXTRA_SHOP_ADDRESS);
        float rating = intent.getFloatExtra(EXTRA_SHOP_RATING, 0f);
        String imageUrl = intent.getStringExtra(EXTRA_SHOP_IMAGE_URL);
        String openingHours = intent.getStringExtra(EXTRA_SHOP_OPENING_HOURS);
        boolean openNow = intent.getBooleanExtra(EXTRA_SHOP_OPEN_NOW, true);

        tvShopName.setText(shopName != null ? shopName : "Laundry Shop");
        tvShopAddress.setText(address != null ? address : "");
        tvHeaderRating.setText(String.format(java.util.Locale.getDefault(), "★ %.1f", rating));

        if (openingHours != null && !openingHours.isEmpty()) {
            tvOpeningHours.setText(openingHours);
        }

        if (openNow) {
            tvOpenStatus.setText("Open Now");
            tvOpenStatus.setTextColor(getResources().getColor(R.color.green));
        } else {
            tvOpenStatus.setText("Closed");
            tvOpenStatus.setTextColor(getResources().getColor(R.color.red));
        }

        if (imageUrl != null && imageUrl.startsWith("http")) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder_laundry)
                    .into(ivHeaderImage);
        }
        // else: stays on the default placeholder_laundry already set in XML
    }

    private void setupServicePrices() {
        List<ServicePrice> services = new ArrayList<>();
        services.add(new ServicePrice("Wash & Fold", 2.00, "/kg"));
        services.add(new ServicePrice("Wash & Iron", 3.00, "/kg"));
        services.add(new ServicePrice("Dry Clean", 6.50, "/item"));
        services.add(new ServicePrice("Premium Care", 9.00, "/item"));
        // TODO: replace with a Firestore subcollection read keyed by shopId,
        // e.g. shops/{shopId}/services, once the backend source is decided.

        rvServicePrices.setLayoutManager(new LinearLayoutManager(this));
        rvServicePrices.setAdapter(new ServicePriceAdapter(services));
    }

    private void setupActions() {
        btnBack.setOnClickListener(v -> finish());

        btnBookNow.setOnClickListener(v -> {
            // TODO: replace with real booking flow once BookingFlowActivity exists.
            // Intent intent = new Intent(this, BookingFlowActivity.class);
            // intent.putExtra(ShopDetailActivity.EXTRA_SHOP_ID, shopId);
            // intent.putExtra(ShopDetailActivity.EXTRA_SHOP_NAME, shopName);
            // startActivity(intent);
            Toast.makeText(this, "Booking flow coming soon for " + shopName, Toast.LENGTH_SHORT).show();
        });
    }
}