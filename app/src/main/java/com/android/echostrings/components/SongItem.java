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

import com.android.echostrings.MainActivity;
import com.android.echostrings.R;
import com.android.echostrings.activities.PracticeActivity;
import com.android.echostrings.activities.RecognizeSoundActivity;

import org.json.JSONObject;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SongItem extends LinearLayout {
    private TextView name_text,author_text;
    private String id;
    // 构造函数
    public SongItem(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SongItem(@NonNull Context context, Map<String, Object> data) {
        super(context);
        init(context);
    }
    public SongItem(@NonNull Context context) {
        super(context);
        init(context);
    }
    public SongItem(@NonNull Context context,String song,String author,String id){
        super(context);
        init(context);
        name_text.setText(song);
        author_text.setText(author);
        this.id=id;
    }
    // 初始化视图
    private void init(Context context) {
        // 通过布局填充器加载布局
        LayoutInflater.from(context).inflate(R.layout.component_song_item, this, true);
        name_text=findViewById(R.id.song_name);
        author_text=findViewById(R.id.song_author);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RecognizeSoundActivity)context).song_id=id;
                Intent intent=new Intent(context, PracticeActivity.class);
                intent.putExtra("song_id",id);
                context.startActivity(intent);
            }
        });
    }
}


