package com.example.aura;

public class Split {

    private String title;
    private double amount;
    private String paidBy;
    private String date;

    public Split() {
        // Required for Firestore
    }

    public Split(String title, double amount, String paidBy) {
        this.title = title;
        this.amount = amount;
        this.paidBy = paidBy;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public String getDate() {
        return date;
    }
}