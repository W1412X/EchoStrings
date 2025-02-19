package com.android.echostrings.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.os.Handler;
import android.widget.Toast;

import java.lang.Runnable;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayoutMediator;

import com.android.echostrings.R;
import com.android.echostrings.activities.DiscussionActivity;
import com.android.echostrings.activities.ChatActivity;
import com.android.echostrings.activities.ActivityListActivity;
import com.android.echostrings.activities.MentorActivity;
import com.android.echostrings.adapter.ActivityBannerAdapter;
import com.android.echostrings.data.ActivityItem;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class SPF extends Fragment {

    private ViewPager2 bannerViewPager;
    private TabLayout tabDots;
    private List<ActivityItem> activityItems = new ArrayList<>();
    private LinearLayout quickAccessLayout;
    private Handler handler = new Handler();
    private RecyclerView postRecyclerView;
    private Runnable runnable;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 绑定组件
        bannerViewPager = view.findViewById(R.id.bannerViewPager);
        tabDots = view.findViewById(R.id.tabDots);
        quickAccessLayout = view.findViewById(R.id.quickAccessLayout);
        postRecyclerView = view.findViewById(R.id.postRecyclerView);

        // 设置 ViewPager 和 TabLayout
        setupBannerSlider();


        // 初始化测试数据
        initDemoData();

        // 快速入口按钮点击事件
        setupQuickAccessButtons();
    }
    public void showToast(String message) {

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void initDemoData() {
        activityItems.add(new ActivityItem(
                "7月弹唱大赛：周杰伦专场挑战",
                "2023.07.01 - 2023.07.31",
                1234,
                "进行中",
                "https://example.com/banner1.jpg"
        ));

        activityItems.add(new ActivityItem(
                "吉他新手训练营",
                "2023.08.01 - 2023.08.31",
                856,
                "报名中",
                "https://example.com/banner2.jpg"
        ));
    }

    private void setupBannerSlider() {
        // 初始化数据（示例数据）
        // 配置ViewPager2
        bannerViewPager.setAdapter(new ActivityBannerAdapter(activityItems, item -> {
            // 处理点击事件
            showToast("点击活动：" + item.getTitle());
        }));

        // 设置初始位置
        int startPosition = Integer.MAX_VALUE / 2;
        bannerViewPager.setCurrentItem(startPosition, false);

        // 绑定指示器
        new TabLayoutMediator(tabDots, bannerViewPager, (tab, position) -> {}).attach();

        // 添加自动滚动
        setupAutoScroll();

        // 添加页面切换动画
        ViewPager2.PageTransformer pageTransformer = (page, position) -> {
            float absPos = Math.abs(position);
            page.setAlpha(1 - absPos/3);
            page.setScaleY(1 - absPos/5);
        };
        bannerViewPager.setPageTransformer(pageTransformer);

    }
    private void setupAutoScroll() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentPosition = bannerViewPager.getCurrentItem();
                bannerViewPager.setCurrentItem(currentPosition + 1, true);
                handler.postDelayed(this, 3000); // 3秒切换一次
            }
        };
        handler.postDelayed(runnable, 3000);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 移除回调防止内存泄漏
        
        handler.removeCallbacks(runnable);
    }

    private void setupQuickAccessButtons() {


        // 聊天室按钮点击事件
        LinearLayout chatButton = quickAccessLayout.findViewById(R.id.chatButton);
        chatButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
        });

        // 活动按钮点击事件
        LinearLayout activityButton = quickAccessLayout.findViewById(R.id.activityButton);
        activityButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ActivityListActivity.class);
            startActivity(intent);
        });

        // 师徒按钮点击事件
        LinearLayout mentorButton = quickAccessLayout.findViewById(R.id.mentorButton);
        mentorButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MentorActivity.class);
            startActivity(intent);
        });
    }
}
