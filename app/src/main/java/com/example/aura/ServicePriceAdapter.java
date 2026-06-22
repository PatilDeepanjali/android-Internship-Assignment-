package com.example.aura;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aura.R;
import com.example.aura.ServicePrice;

import java.util.List;
import java.util.Locale;

public class ServicePriceAdapter extends RecyclerView.Adapter<ServicePriceAdapter.PriceViewHolder> {

    private final List<ServicePrice> services;

    public ServicePriceAdapter(List<ServicePrice> services) {
        this.services = services;
    }

    @NonNull
    @Override
    public PriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_price, parent, false);
        return new PriceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceViewHolder holder, int position) {
        ServicePrice service = services.get(position);
        holder.tvName.setText(service.getName());
        holder.tvPrice.setText(String.format(Locale.getDefault(), "$%.2f%s", service.getPrice(), service.getUnit()));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    static class PriceViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;

        PriceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvServicePriceName);
            tvPrice = itemView.findViewById(R.id.tvServicePriceValue);
        }
    }
}