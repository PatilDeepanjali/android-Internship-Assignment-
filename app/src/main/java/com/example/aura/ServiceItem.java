package com.example.aura;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ServiceItem {

    private String name;
    private int iconRes;

    public ServiceItem(String name, int iconRes) {
        this.name = name;
        this.iconRes = iconRes;
    }

    public String getName() {
        return name;
    }

    public int getIconRes() {
        return iconRes;
    }
}
