package com.android.echostrings.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.android.echostrings.ChordLearnActivity;
import com.android.echostrings.R;
import com.google.mediapipe.components.PermissionHelper;

public class PracticeActivity extends AppCompatActivity {
    private String song_id;
    private Button practice_btn,analyze_btn;
    private boolean state;//the composition load state
    private WebView analyze_web,composition_web;
    private MediaRecorder recorder;
    private String audio_path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        setContentView(R.layout.activity_practice);
        initView();
       this.audio_path=getExternalFilesDir(Environment.DIRECTORY_MUSIC)+"/practice_audio_now.wav";
    }
    private void initView(){
        practice_btn=findViewById(R.id.practise_btn);
        analyze_btn=findViewById(R.id.analyze_btn);
        analyze_btn.setVisibility(View.GONE);
        analyze_web=findViewById(R.id.data_analyze_page);
        composition_web=findViewById(R.id.practise_composition_web);
        practice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.checkAndRequestAudioPermissions(PracticeActivity.this);
                if (!PermissionHelper.audioPermissionsGranted(PracticeActivity.this)) {
                    Toast.makeText(PracticeActivity.this,"未授予录音权限",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!state){
                    Toast.makeText(PracticeActivity.this,"正在加载曲谱....",Toast.LENGTH_SHORT).show();
                }else{
                    //start to record the audio
                    startRecording();
                    //start the web play silent
                    composition_web.evaluateJavascript("api.changeTrackMute([api.score.tracks[0]], true);", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //do nothing here
                        }
                    });
                    composition_web.evaluateJavascript("api.play()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //do nothing
                        }
                    });
                    //set the result after play
                }
            }
        });
    }
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setAudioSamplingRate(44100);
        recorder.setOutputFile(this.audio_path);
        try {
            recorder.prepare();
            recorder.start();
            Toast.makeText(PracticeActivity.this, "开始录音...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }
}