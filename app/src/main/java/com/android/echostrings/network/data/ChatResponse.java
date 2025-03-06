package com.android.echostrings.network.data;

public class ChatResponse {
    private int user_id;
    private String answer;

    // 构造函数
    public ChatResponse(int user_id, String answer) {
        this.user_id = user_id;
        this.answer = answer;
    }

    // Getter 和 Setter 方法
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}