package com.example.aura;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aura.R;
import com.example.aura.Offer;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.OfferViewHolder> {

    public interface OnOfferActionListener {
        void onCopyCode(Offer offer);
    }

    private final List<Offer> offers;
    private final OnOfferActionListener listener;

    public OfferAdapter(List<Offer> offers, OnOfferActionListener listener) {
        this.offers = offers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offer, parent, false);
        return new OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
        Offer offer = offers.get(position);

        holder.tvBadge.setText(offer.getBadgeText());
        holder.tvTitle.setText(offer.getTitle());
        holder.tvDescription.setText(offer.getDescription());
        holder.tvPromoCode.setText("Use code: " + offer.getPromoCode());

        if (offer.getExpiryText() != null && !offer.getExpiryText().isEmpty()) {
            holder.tvExpiry.setVisibility(View.VISIBLE);
            holder.tvExpiry.setText(offer.getExpiryText());
        } else {
            holder.tvExpiry.setVisibility(View.GONE);
        }

        holder.btnCopyCode.setOnClickListener(v -> {
            if (listener != null) listener.onCopyCode(offer);
        });
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    static class OfferViewHolder extends RecyclerView.ViewHolder {
        TextView tvBadge, tvExpiry, tvTitle, tvDescription, tvPromoCode;
        Button btnCopyCode;

        OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBadge = itemView.findViewById(R.id.tvBadge);
            tvExpiry = itemView.findViewById(R.id.tvExpiry);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPromoCode = itemView.findViewById(R.id.tvPromoCode);
            btnCopyCode = itemView.findViewById(R.id.btnCopyCode);
        }
    }
}
