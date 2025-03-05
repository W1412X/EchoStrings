package com.android.echostrings.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.echostrings.ChordLearnActivity;
import com.android.echostrings.R;
import com.android.echostrings.activities.AiCorrectionActivity;
import com.android.echostrings.activities.MusicCompositionActivity;
import com.android.echostrings.activities.RecognizeSoundActivity;
import com.android.echostrings.activities.VideoCourseActivity;
import com.google.android.material.card.MaterialCardView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LearnPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LearnPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LearnPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LearnPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LearnPageFragment newInstance(String param1, String param2) {
        LearnPageFragment fragment = new LearnPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn_page, container, false);


        MaterialCardView btnRecognizeSound = view.findViewById(R.id.btn_recognize_sound);
        MaterialCardView btnAiCorrection = view.findViewById(R.id.btn_AI_correct);
        MaterialCardView btnVideoCourse = view.findViewById(R.id.btn_video_class);
        MaterialCardView btnMusicComposition = view.findViewById(R.id.btn_create);


        btnRecognizeSound.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RecognizeSoundActivity.class);
            startActivity(intent);
        });


        btnAiCorrection.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChordLearnActivity.class);
            startActivity(intent);
        });


        btnVideoCourse.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), VideoCourseActivity.class);
            startActivity(intent);
        });


        btnMusicComposition.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MusicCompositionActivity.class);
            startActivity(intent);
        });

        return view;
    }
}