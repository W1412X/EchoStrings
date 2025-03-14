package com.android.echostrings.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.echostrings.R;
import com.android.echostrings.components.SongItem;

public class RecognizeSoundActivity extends AppCompatActivity {
    private LinearLayout finger_play_btn,sing_play_btn;
    private CardView song_list_card;
    private LinearLayout song_list_container;
    public String type="";//finger/sing
    public String song_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize_sound);
        initView();
    }
    private void initView(){
        finger_play_btn=findViewById(R.id.finger_play_button);
        sing_play_btn=findViewById(R.id.sing_play_button);
        song_list_card=findViewById(R.id.song_container_card);
        song_list_container=findViewById(R.id.song_container_linear);
        finger_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="finger";
                //get the song list here

                song_list_card.setVisibility(View.VISIBLE);
            }
        });
        sing_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecognizeSoundActivity.this,"此功能暂未实现",Toast.LENGTH_SHORT).show();
                return;
                //type="sing";
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(song_list_card.getVisibility()==View.VISIBLE){
            song_list_card.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }
}