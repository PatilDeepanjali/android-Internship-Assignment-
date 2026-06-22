package com.example.aura;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SplitAdapter extends RecyclerView.Adapter<SplitAdapter.SplitViewHolder> {

    private List<Split> splitList;

    public SplitAdapter(List<Split> splitList) {
        this.splitList = splitList;
    }

    @NonNull
    @Override
    public SplitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_split, parent, false);
        return new SplitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SplitViewHolder holder, int position) {

        Split split = splitList.get(position);

        holder.tvTitle.setText(split.getTitle());

        holder.tvAmount.setText(
                "Total payment $" + (int) split.getAmount());

        if (split.getTitle().toLowerCase().contains("team")) {

            holder.imgType.setImageResource(R.drawable.ic_group);

        } else {

            holder.imgType.setImageResource(R.drawable.placeholder_avatar);

        }

    }

    @Override
    public int getItemCount() { return splitList.size(); }

    static class SplitViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvAmount;
        ImageView imgType;

        SplitViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvSplitTitle);
            tvAmount = itemView.findViewById(R.id.tvSplitAmount);
            imgType = itemView.findViewById(R.id.imgType);
        }
    }
}