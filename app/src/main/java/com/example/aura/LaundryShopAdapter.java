package com.example.aura;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aura.R;
import com.example.aura.LaundryShop;

import java.util.List;
import java.util.Locale;

public class LaundryShopAdapter extends RecyclerView.Adapter<LaundryShopAdapter.ShopViewHolder> {

    public interface OnShopClickListener {
        void onShopClick(LaundryShop shop);

        void onBookNowClick(LaundryShop shop);
    }

    private final List<LaundryShop> shops;
    private final OnShopClickListener listener;

    public LaundryShopAdapter(List<LaundryShop> shops, OnShopClickListener listener) {
        this.shops = shops;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_laundry_shop, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        LaundryShop shop = shops.get(position);

        holder.tvName.setText(shop.getName());
        holder.tvAddress.setText(shop.getAddress());
        holder.tvRating.setText(String.format(Locale.getDefault(), "★ %.1f", shop.getRating()));

        if (shop.getDistanceText() != null && !shop.getDistanceText().isEmpty()) {
            holder.tvDistance.setVisibility(View.VISIBLE);
            holder.tvDistance.setText(shop.getDistanceText());
        } else {
            holder.tvDistance.setVisibility(View.GONE);
        }

        // imageUrl works for a Firestore/REST remote URL — Glide loads that directly.
        // When there's no remote URL (local/dev data), fall back to the bundled
        // "laundry" drawable so real shop photos show instead of a generic placeholder.
        if (shop.getImageUrl() != null && shop.getImageUrl().startsWith("http")) {
            Glide.with(holder.itemView.getContext())
                    .load(shop.getImageUrl())
                    .placeholder(R.drawable.laundry)
                    .into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(R.drawable.laundry);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onShopClick(shop);
        });
        holder.btnBookNow.setOnClickListener(v -> {
            if (listener != null) listener.onBookNowClick(shop);
        });
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    static class ShopViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvAddress, tvRating, tvDistance;
        Button btnBookNow;

        ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivShopImage);
            tvName = itemView.findViewById(R.id.tvShopName);
            tvAddress = itemView.findViewById(R.id.tvShopAddress);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            btnBookNow = itemView.findViewById(R.id.btnBookNow);
        }
    }
}