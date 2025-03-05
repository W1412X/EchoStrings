package com.android.echostrings.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.echostrings.R;
import com.android.echostrings.data.ActivityItem;
import com.google.android.material.button.MaterialButton;
import com.bumptech.glide.Glide;

import java.util.List;

public class ActivityBannerAdapter extends RecyclerView.Adapter<ActivityBannerAdapter.ViewHolder> {

    private final List<ActivityItem> items;
    private final OnItemClickListener listener;

    public ActivityBannerAdapter(List<ActivityItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity_banner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityItem item = items.get(position % items.size());

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            holder.itemView.setLayoutParams(layoutParams);
        }
        // 绑定数据示例
        holder.tvTitle.setText(item.getTitle());
        holder.tvTime.setText(item.getTime());


        // 加载封面图（使用Glide）
        Glide.with(holder.itemView)
                .load(item.getCoverUrl())
                .into(holder.ivCover);

        // 设置点击事件
        holder.btnJoin.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvStatus, tvTitle, tvTime, tvParticipants;
        MaterialButton btnJoin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvParticipants = itemView.findViewById(R.id.tv_participants);
            btnJoin = itemView.findViewById(R.id.btn_join);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ActivityItem item);
    }
}