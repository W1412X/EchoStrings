package com.android.echostrings.data;
public class Work {
    public String title;
    public String duration;
    private String author;
    private String videoUrl;

    public Work( String title,String duration,String author, String videoUrl) {
        this.title=title;
        this.duration=duration;
        this.author = author;
        this.videoUrl = videoUrl;
    }
    public String getTitle() {
        return title;
    }
    public String getDuration() {
        return duration;
    }
    public String getAuthor() {
        return author;
    }
    public String getVideoUrl() {
        return videoUrl;
    }
}

