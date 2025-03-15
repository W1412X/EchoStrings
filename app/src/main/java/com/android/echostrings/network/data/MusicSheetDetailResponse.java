package com.android.echostrings.network.data;

public class MusicSheetDetailResponse {
    private String status;
    private Data data;

    // 内部类用于映射"data"对象
    public static class Data {
        private int id;
        private String title;
        private String content;
        private double duration;
        private String audioFilePath;

        // Getter 和 Setter 方法
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public double getDuration() { return duration; }
        public void setDuration(double duration) { this.duration = duration; }
        public String getAudioFilePath() { return audioFilePath; }
        public void setAudioFilePath(String audioFilePath) { this.audioFilePath = audioFilePath; }
    }

    // Getter 和 Setter 方法
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }
}