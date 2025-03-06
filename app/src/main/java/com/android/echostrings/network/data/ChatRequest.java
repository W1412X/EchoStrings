package com.android.echostrings.network.data;

// ChatRequest.java
public class ChatRequest {
    private String user_id;
    private String question;

    // 构造函数
    public ChatRequest(String user_id, String question) {
        this.user_id = user_id;
        this.question = question;
    }

    // Getter 和 Setter 方法
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}