package com.android.echostrings.network.data;

public class MusicCreateResponse {
    private String user_id;
    private String answer;

    // 构造函数
    public MusicCreateResponse(String user_id, String answer) {
        this.user_id = user_id;
        this.answer = answer;
    }

    // Getter 和 Setter 方法
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}