package com.android.echostrings.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.android.echostrings.R;
import com.android.echostrings.activities.AiCorrectionActivity;
import com.android.echostrings.activities.MusicCompositionActivity;
import com.android.echostrings.activities.RecognizeSoundActivity;
import com.android.echostrings.activities.VideoCourseActivity;

public class LPF extends Fragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 绑定功能卡片
        LinearLayout btnRecognizeSound = view.findViewById(R.id.btn_recognize_sound);
        LinearLayout btnAiCorrection = view.findViewById(R.id.btn_AI_correct);
        LinearLayout btnVideoCourse = view.findViewById(R.id.btn_video_class);
        LinearLayout btnMusicComposition = view.findViewById(R.id.btn_create);

        // 识音识谱 按钮点击事件
        btnRecognizeSound.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RecognizeSoundActivity.class);
            startActivity(intent);
        });

        // AI纠正 按钮点击事件
        btnAiCorrection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AiCorrectionActivity.class);
            startActivity(intent);
        });

        // 视频课程 按钮点击事件
        btnVideoCourse.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), VideoCourseActivity.class);
            startActivity(intent);
        });

        // 曲谱创作 按钮点击事件
        btnMusicComposition.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MusicCompositionActivity.class);
            startActivity(intent);
        });
    }
}

