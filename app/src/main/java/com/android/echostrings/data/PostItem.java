package com.android.echostrings.data;

import java.io.Serializable;

public class PostItem implements Serializable {
    private String name;
    private String title;
    private String content;
    private String imageUrl;
    private String videoUrl;
    private int time;
    private int like_count;
    private int comment_count;
    private String id_avatar;
    public PostItem(String name, String title, String content, String imageUrl,String videoUrl,int time,int like_count,int comment_count,String id_avatar) {
        this.name= name;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.time = time;
        this.like_count = like_count;
        this.comment_count = comment_count;
        this.id_avatar = id_avatar;
    }
    public String getName() { return name; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getVideoUrl() { return videoUrl; }
    public String getImageUrl() { return imageUrl; }
    public int getTime() { return time; }
    public int getLike_count() { return like_count; }
    public int getComment_count() { return comment_count; }
    public String getId_avatar() { return id_avatar; }


}

