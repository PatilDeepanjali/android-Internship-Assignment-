package com.example.aura;

/**
 * Represents one promotional offer/deal card.
 * Same pattern as ServiceItem/LaundryShop: hardcode a List<Offer> for now,
 * swap the data source in OffersActivity.loadOffers() later without touching
 * this class or the adapter.
 */
public class Offer {

    private String id;
    private String badgeText;     // e.g. "FOR A LIMITED TIME"
    private String title;          // e.g. "Get 30% OFF on Dry Cleaning"
    private String description;    // e.g. "Valid on all orders above $25."
    private String promoCode;      // e.g. "LAUNDRY30"
    private String expiryText;     // e.g. "Expires in 3 days" - optional, can be null

    public Offer(String id, String badgeText, String title, String description,
                 String promoCode, String expiryText) {
        this.id = id;
        this.badgeText = badgeText;
        this.title = title;
        this.description = description;
        this.promoCode = promoCode;
        this.expiryText = expiryText;
    }

    public String getId() {
        return id;
    }

    public String getBadgeText() {
        return badgeText;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public String getExpiryText() {
        return expiryText;
    }
}