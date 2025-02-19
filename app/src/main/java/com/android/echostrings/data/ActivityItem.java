package com.android.echostrings.data;

public class ActivityItem {
    private String title;
    private String time;
    private int participants;
    private String status;
    private String coverUrl;

    public ActivityItem(String title, String time, int participants, String status, String coverUrl) {
        this.title = title;
        this.status = status;
        this.coverUrl = coverUrl;
        this.time = time;
        this.participants = participants;
    }
    public String getTitle() {
        return title;
    }
    public String getTime() {
        return time;
    }
    public int getParticipants() {
        return participants;
    }
    public String getCoverUrl() {
        return coverUrl;
    }


    // 构造函数、getters、setters
}