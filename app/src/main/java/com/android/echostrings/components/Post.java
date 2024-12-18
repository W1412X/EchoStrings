package com.android.echostrings.components;
/*
* 帖子类
* */

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.echostrings.R;

import org.json.JSONObject;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Post extends LinearLayout {
    // 构造函数
    public Post(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Post(@NonNull Context context, Map<String, Object> data) {
        super(context);
        init(context);
    }
    public Post(@NonNull Context context) {
        super(context);
        init(context);
    }
    // 初始化视图
    private void init(Context context) {
        // 通过布局填充器加载布局
        LayoutInflater.from(context).inflate(R.layout.component_post, this, true);
    }
}


