package com.android.echostrings.data;

public class ActivityItem {
    private String title;
    private String time;

    private String status;
    private String coverUrl;

    public ActivityItem(String title, String time, String status, String coverUrl) {
        this.title = title;
        this.status = status;
        this.coverUrl = coverUrl;
        this.time = time;

    }
    public String getTitle() {
        return title;
    }
    public String getTime() {
        return time;
    }
//    public int getParticipants() {
//        return participants;
//    }
    public String getCoverUrl() {
        return coverUrl;
    }
    public String getStatus() {
        return status;
    }


    // 构造函数、getters、setters
}