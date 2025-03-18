package com.android.echostrings.network.data;

public class CompositionRequest {
    public String user_id;
    public String base64_data,title,content;

    // 构造函数
    public CompositionRequest(String user_id, String base64_data,String title,String content) {
        this.user_id = user_id;
        this.title=title;
        this.content=content;
        this.base64_data = base64_data;
    }
}