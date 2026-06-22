package com.example.aura;

/**
 * Represents one card in "Popular Laundry Nearby" (Home), the Nearby list,
 * and the source data for Shop Detail.
 * imageUrl is a String so it works the same whether you load it from a local
 * drawable name, a Firestore field, or a REST API response later — Glide/Picasso
 * can load a URL string directly without changing the adapter.
 *
 * distanceText and openingHours are optional (nullable) — Home doesn't show
 * either, Nearby shows distance, Shop Detail shows opening hours.
 */
public class LaundryShop {

    private String id;
    private String name;
    private String address;
    private float rating;
    private String imageUrl;
    private String distanceText;   // e.g. "0.8 km away" — nullable
    private String openingHours;   // e.g. "8:00 AM - 9:00 PM" — nullable
    private boolean openNow;       // only meaningful when openingHours is set

    public LaundryShop(String id, String name, String address, float rating, String imageUrl) {
        this(id, name, address, rating, imageUrl, null);
    }

    public LaundryShop(String id, String name, String address, float rating, String imageUrl, String distanceText) {
        this(id, name, address, rating, imageUrl, distanceText, null, true);
    }

    public LaundryShop(String id, String name, String address, float rating, String imageUrl,
                       String distanceText, String openingHours, boolean openNow) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.distanceText = distanceText;
        this.openingHours = openingHours;
        this.openNow = openNow;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public float getRating() {
        return rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDistanceText() {
        return distanceText;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public boolean isOpenNow() {
        return openNow;
    }
}