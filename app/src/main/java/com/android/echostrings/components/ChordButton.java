package com.android.echostrings.components;


import android.content.Context;
import android.content.res.Resources;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.android.echostrings.ChordLearnActivity;
import com.android.echostrings.R;
import com.android.echostrings.data.ChordLearnData;
import com.google.mediapipe.components.PermissionHelper;

import java.util.Map;
import android.os.Handler;

public class ChordButton extends LinearLayout {
    private TextView little_title,big_title,press_description,play_description;
    private ImageView little_img,big_img;
    private LinearLayout little_linear,big_linear;
    private ImageButton record_btn;
    private String audio_path;
    public MediaRecorder recorder;
    private Context context;
    private RelativeLayout overlay,right_overlay,error_overlay,skeleton_tip_overlay;
    private TextView skeleton_ok_bar_text,skeleton_error_bar_text;
    private TextView overlay_text;
    private Handler handler;
    private int secondsCount = 0;
    private RelativeLayout overlay_model_load;
    private String chord;
    private String major;
    /**
     * denote if the press ok
     */
    private Boolean skeleton_state=false;
    /**
     * if open
     */
    private Boolean select_state=false;
    // 构造函数
    public ChordButton(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChordButton(@NonNull Context context, Map<String, Object> data) {
        super(context);
        init(context);
    }
    public ChordButton(@NonNull Context context) {
        super(context);
        init(context);
    }


    /**
     *
     * @param context
     * @param recorder audio recorder object
     * @param audio_path  save audio path
     */
    public ChordButton(@NonNull Context context, MediaRecorder recorder, String audio_path,String major,String chord) {
        super(context);
        this.recorder=recorder;
        this.audio_path=audio_path;
        this.handler=new Handler();
        this.chord=chord;
        this.major=major;
        init(context);
    }
    // 初始化视图
    private void init(Context context) {
        /**
         * init layout
         */
        this.context=context;
        LayoutInflater.from(context).inflate(R.layout.component_chord_btn, this, true);
        little_title=findViewById(R.id.chord_btn_little_chord_title);
        big_title=findViewById(R.id.chord_btn_big_chord_title);
        press_description=findViewById(R.id.chord_btn_big_chord_press_description);
        play_description=findViewById(R.id.chord_btn_big_chord_play_description);
        little_img=findViewById(R.id.chord_btn_little_chord_img);
        big_img=findViewById(R.id.chord_btn_big_chord_img);
        little_linear=findViewById(R.id.chord_btn_little_view);
        big_linear=findViewById(R.id.chord_btn_big_view);
        record_btn=findViewById(R.id.chord_btn_big_record_btn);
        big_linear.setVisibility(View.GONE);
        overlay=findViewById(R.id.chord_btn_overlay);
        overlay_text=findViewById(R.id.chord_btn_overlay_text);
        overlay_model_load=findViewById(R.id.chord_btn_model_loading);
        right_overlay=findViewById(R.id.chord_btn_right_overlay);
        error_overlay=findViewById(R.id.chord_btn_error_overlay);
        skeleton_tip_overlay=findViewById(R.id.chord_btn_skeleton_tip_overlay);
        skeleton_ok_bar_text=findViewById(R.id.chord_btn_skeleton_result_right);
        skeleton_error_bar_text=findViewById(R.id.chord_btn_skeleton_result_false);
        /**
         * set the data according to the major and chord
         */
        String major_chord=this.major+"_"+this.chord;
        little_title.setText(ChordLearnData.getTitle(major_chord));
        big_title.setText(ChordLearnData.getTitle(major_chord));
        press_description.setText(ChordLearnData.getPressDes(major_chord));
        play_description.setText(ChordLearnData.getPlayDescription(major_chord));
        Resources resources = context.getResources();
        int res_id=resources.getIdentifier(major+"_chord_"+chord, "drawable", context.getPackageName());
        little_img.setImageResource(res_id);
        big_img.setImageResource(res_id);
        /**
         * bind to function
         */
        record_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permission
                PermissionHelper.checkAndRequestAudioPermissions((ChordLearnActivity)context);
                if (!PermissionHelper.audioPermissionsGranted((ChordLearnActivity)context)) {
                    Toast.makeText(context,"未授予录音权限",Toast.LENGTH_SHORT).show();
                    return;
                }
                /**
                 * check record state
                 * in different btn,hard to control the recorder state,
                 * so we appoint here that if record is not null,then the recorder used by other buttons
                 * ensure when use the recorder we init it and release it after use
                 */
                if(recorder!=null){
                    Toast.makeText(context,"其他进程正在使用录音",Toast.LENGTH_SHORT).show();
                    return;
                }
                /**
                 * check the skeleton state
                 */
                if(!skeleton_state){
                    skeleton_tip_overlay.setVisibility(View.VISIBLE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                          skeleton_tip_overlay.setVisibility(View.GONE);
                        }
                    },1000);
                    return;
                }
                /**
                 * setup the recorder and start record
                 */
                recordUpdateUI.run();
                startRecording();
            }
        });
    }

    /**
     * recording view alter
     */
    private final Runnable recordUpdateUI = new Runnable() {
        @Override
        public void run() {
            if(overlay.getVisibility()==View.GONE){
                overlay.setVisibility(View.VISIBLE);
            }
            if(overlay_text.getVisibility()==View.GONE){
                overlay_text.setVisibility(View.VISIBLE);
            }
            if (secondsCount < 50) {
                double remainingTime = (5.0 - ((double) secondsCount / 10));
                overlay_text.setText(String.format("%.1f", remainingTime) + "s");
                secondsCount++;
                handler.postDelayed(this, 100);
            }else if (recorder!=null){
                /**
                 * record is not null,which means the record not finish yet
                 * wait for it
                 */
                handler.postDelayed(this, 100);
            }else{
                /**
                 * now load the model and do the recognition
                 */
                overlay_text.setVisibility(View.GONE);
                overlay_model_load.setVisibility(View.VISIBLE);
                if(true){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            overlay_model_load.setVisibility(View.GONE);
                            right_overlay.setVisibility(View.VISIBLE);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    right_overlay.setVisibility(View.GONE);
                                }
                            },1000);
                        }
                    },2000);
                }else{
                    error_overlay.setVisibility(View.VISIBLE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            error_overlay.setVisibility(View.GONE);
                        }
                    },1000);
                }
                overlay.setVisibility(View.GONE);
                secondsCount=0;
            }
        }
    };
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
            Toast.makeText((ChordLearnActivity)context, "开始录音...", Toast.LENGTH_SHORT).show();
            // Stop recording after 5 seconds
            new Thread(() -> {
                try {
                    Thread.sleep(5000); // 5 seconds
                    stopRecording();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
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

    /**
     * click called by the external context
     */
    public void externalClick(){
        /**
         * check the visibility
         */
        if(little_linear.getVisibility()==View.GONE){
            little_linear.setVisibility(View.VISIBLE);
            big_linear.setVisibility(View.GONE);
            /**
             * set unselect
             * set skeleton state
             */
            this.select_state=false;
            this.skeleton_state=false;
        }else{
            little_linear.setVisibility(View.GONE);
            big_linear.setVisibility(View.VISIBLE);
            this.select_state=true;
        }
    }
    public void setSkeletonState(Boolean state){
        this.skeleton_state=state;
        /**
         * if ok,then show the ok
         */
        if(state){
            skeleton_ok_bar_text.setVisibility(View.VISIBLE);
            skeleton_error_bar_text.setVisibility(View.GONE);
        }else{
            //show not ok
            skeleton_ok_bar_text.setVisibility(View.GONE);
            skeleton_error_bar_text.setVisibility(View.VISIBLE);
        }
    }
    public Boolean getSelectState(){
        return this.select_state;
    }
    public String getChord(){
        return this.chord;
    }
}



