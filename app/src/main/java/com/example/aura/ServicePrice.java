package com.example.aura;

/**
 * One priced service row on the Shop Detail screen, e.g. "Wash & Fold - $2.00/kg".
 * Distinct from ServiceItem (the icon row on Home) since this needs a price
 * and unit, not just a name+icon.
 */
public class ServicePrice {

    private String name;
    private double price;
    private String unit; // e.g. "/kg", "/item", "/load"

    public ServicePrice(String name, double price, String unit) {
        this.name = name;
        this.price = price;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }
}