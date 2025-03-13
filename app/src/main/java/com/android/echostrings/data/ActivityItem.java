package com.android.echostrings.data;

import java.util.List;

public class ActivityItem {
    private String title;
    private String time;
    private String status;
    private String coverUrl;
    private int participants;
    private String activityIntroduction;
    private List<Work> hotwork;

    public ActivityItem(String title, String time, String status, String coverUrl, int participants, String activityIntroduction, List<Work> hotwork) {
        this.title = title;
        this.time = time;
        this.status = status;
        this.coverUrl = coverUrl;
        this.participants = participants;
        this.activityIntroduction = activityIntroduction;
        this.hotwork = hotwork;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public int getParticipants() {
        return participants;
    }

    public String getActivityIntroduction() {
        return activityIntroduction;
    }

    public List<Work> getHotwork() {
        return hotwork;
    }
}
