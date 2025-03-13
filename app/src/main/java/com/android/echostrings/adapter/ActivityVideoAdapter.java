package com.android.echostrings.adapter;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.echostrings.R;
import com.android.echostrings.data.Work;

import java.util.List;

public class ActivityVideoAdapter extends RecyclerView.Adapter<ActivityVideoAdapter.VideoViewHolder> {

    private List<Work> works;

    public ActivityVideoAdapter(List<Work> works) {
        this.works = works;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        Work work = works.get(position);
        holder.titleTextView.setText(work.getTitle());
        holder.authorTextView.setText(work.getAuthor());

        holder.videoView.setVideoURI(Uri.parse(work.getVideoUrl()));
        holder.videoView.start();


    }

    @Override
    public int getItemCount() {
        return works.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorTextView;
        VideoView videoView;

        public VideoViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tv_title);
            authorTextView = itemView.findViewById(R.id.author_name);
            videoView = itemView.findViewById(R.id.vv_video);
        }
    }
    public void updateData(List<Work> newWorks) {
        this.works.clear();
        this.works.addAll(newWorks);
        notifyDataSetChanged();
    }
}
