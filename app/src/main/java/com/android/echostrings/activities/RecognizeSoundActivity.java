package com.android.echostrings.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.echostrings.R;

public class RecognizeSoundActivity extends AppCompatActivity {
    private Button tune_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize_sound);
        tune_button=findViewById(R.id.tune_button);
        tune_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecognizeSoundActivity.this,TuneActivity.class));
            }
        });

    }
}