package com.android.echostrings.adapter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.echostrings.R;
import com.android.echostrings.activities.ActivityDetailActivity;
import com.android.echostrings.data.ActivityItem;
import com.android.echostrings.data.VideoItem;
import com.bumptech.glide.Glide;

import java.util.List;
public class ActivityEventAdapter extends RecyclerView.Adapter<ActivityEventAdapter.ViewHolder> {

    private List<ActivityItem> eventList;
    private Context context;

    public ActivityEventAdapter(Context context,List<ActivityItem> eventList) {
        this.context = context;
        this.eventList = eventList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityItem event = eventList.get(position);
        holder.title.setText(event.getTitle());
        holder.time.setText(event.getTime());
        Glide.with(holder.itemView.getContext())
                .load(event.getCoverUrl())
                .into(holder.coverUrl);
        holder.status.setText(event.getStatus());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityDetailActivity.class);
            intent.putExtra("activityId", event.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, time,status;
        ImageView coverUrl;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_event_title);
            time = itemView.findViewById(R.id.tv_event_date);
            status = itemView.findViewById(R.id.tv_event_status);
             coverUrl= itemView.findViewById(R.id.img_event_cover);

        }
    }
}
