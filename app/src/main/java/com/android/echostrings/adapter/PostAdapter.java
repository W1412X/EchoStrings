package com.android.echostrings.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.echostrings.R;
import com.android.echostrings.data.PostItem;
import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private List<PostItem> postList;

    public PostAdapter(Context context, List<PostItem> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_community_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostItem post = postList.get(position);


        holder.postTitle.setText(post.getTitle());


        holder.userName.setText("用户昵称");


        holder.postTime.setText(formatTime(post.getTime()));


        holder.likeCount.setText(String.valueOf(post.getLike_count()));
        holder.commentCount.setText(String.valueOf(post.getComment_count()));


        Glide.with(context)
                .load(post.getId_avatar())
                .placeholder(R.drawable.bottom_icon_self_unselected)
                .into(holder.userAvatar);
//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, PostDetailActivity.class);
//            intent.putExtra("post_id", post.getId());
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postTitle, postTime, likeCount, commentCount, userName;
        ImageView userAvatar;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.postTitle);
            postTime = itemView.findViewById(R.id.postTime);
            likeCount = itemView.findViewById(R.id.likeCount);
            commentCount = itemView.findViewById(R.id.commentCount);
            userAvatar = itemView.findViewById(R.id.userAvatar);
            userName = itemView.findViewById(R.id.userName);
        }
    }


    private String formatTime(int timestamp) {
        // 这里可以改成你想要的格式，比如 "2小时前"
        return timestamp + "小时前";
    }
    public void addPost(PostItem post) {
        postList.add(0, post);
        notifyItemInserted(0);
    }

}
