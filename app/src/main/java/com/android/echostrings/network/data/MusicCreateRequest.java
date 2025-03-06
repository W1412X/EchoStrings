package com.android.echostrings.network.data;

import android.util.Log;

public class MusicCreateRequest {
    private String user_id;
    private String title;
    private String instr_id;
    private String tuning;
    private String tempo;
    private String artist;
    private String time;
    private String style_desc;

    // 构造函数
    public MusicCreateRequest(String user_id, String title, String instr_id, String tuning, String tempo, String artist, String time, String style_desc) {
        this.user_id = user_id;
        this.title = title;
        this.instr_id = instr_id;
        this.tuning = tuning;
        this.tempo = tempo;
        this.artist = artist;
        this.time = time;
        this.style_desc = style_desc;
        Log.e("data",user_id+"|"+title+"|"+instr_id+"|"+tuning+"|"+tempo+"|"+artist+"|"+time+"|"+style_desc);
    }

    // Getter 和 Setter 方法
    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getInstr_id() { return instr_id; }
    public void setInstr_id(String instr_id) { this.instr_id = instr_id; }
    public String getTuning() { return tuning; }
    public void setTuning(String tuning) { this.tuning = tuning; }
    public String getTempo() { return tempo; }
    public void setTempo(String tempo) { this.tempo = tempo; }
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getStyle_desc() { return style_desc; }
    public void setStyle_desc(String style_desc) { this.style_desc = style_desc; }
}