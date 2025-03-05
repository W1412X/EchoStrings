package com.android.echostrings.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.echostrings.R;
import com.android.echostrings.adapter.VideoPagerAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VideoCourseActivity extends AppCompatActivity {

    private TabLayout tabCategory;
    private ViewPager viewPager;
    private VideoPagerAdapter videoPagerAdapter;
    private String[] categories = {"推荐", "教学", "初级","高级"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course);
        MaterialButton postButton = findViewById(R.id.btn_return_back);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabCategory = findViewById(R.id.tabCategory);
        viewPager = findViewById(R.id.viewPager);
        videoPagerAdapter = new VideoPagerAdapter(getSupportFragmentManager(), categories);
        viewPager.setAdapter(videoPagerAdapter);

        tabCategory.setupWithViewPager(viewPager);
        postButton.setOnClickListener(v -> finish());
    }
}






