package com.android.echostrings.activities;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.fragment.app.Fragment;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.echostrings.R;
import com.android.echostrings.data.ActivityItem;
import com.android.echostrings.data.Work;
import com.android.echostrings.fragments.ActivityIntroFragment;
import com.android.echostrings.fragments.HotWorksFragment;
import com.android.echostrings.network.ApiService;
import com.android.echostrings.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ActivityDetailActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private ApiService apiService;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private MaterialButton btnSubmit;
    private String activityId;

    private TextView eventStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        eventStatus = findViewById(R.id.eventStatus);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        btnSubmit = findViewById(R.id.btnSubmit);
        activityId = getIntent().getStringExtra("activityId");

        List<Fragment> fragments = new ArrayList<>();
        ActivityIntroFragment introFragment = new ActivityIntroFragment();
        HotWorksFragment worksFragment = new HotWorksFragment();

        Bundle bundle = new Bundle();
        bundle.putString("activityId", activityId);  // 传递活动 ID
        introFragment.setArguments(bundle);
        worksFragment.setArguments(bundle);

        fragments.add(introFragment);
        fragments.add(worksFragment);
        apiService = RetrofitClient.getInstance().create(ApiService.class);



        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("活动介绍");
            } else {
                tab.setText("最热作品");
            }
        }).attach();
        btnSubmit.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityDetailActivity.this, SubmitWorkActivity.class);
            startActivity(intent);
        });

        apiService = RetrofitClient.getInstance().create(ApiService.class);


        loadEventDetails(activityId);
        loadMockEventDetails();
    }
    private void loadMockEventDetails() {
        List<Work> hotWorks1 = Arrays.asList(
                new Work("星空", "4:23", "gs", "https://example.com/work1")

        );

        ActivityItem mockEvent = new ActivityItem(
                "1","活动 1", "2035.2.3-2025.6.3","进行中","",100,"",hotWorks1

        );



        String statusText = mockEvent.getStatus() + "  |  " +
                mockEvent.getParticipants() + "人参与  |  " +
                mockEvent.getTime();
        eventStatus.setText(statusText);
    }
        private void loadEventDetails(String activityId) {

            Call<ActivityItem> call = apiService.getEventDetails(activityId);


            call.enqueue(new Callback<ActivityItem>() {
                @Override
                public void onResponse(Call<ActivityItem> call, Response<ActivityItem> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ActivityItem data = response.body();


                        String statusText = data.getStatus() + "  |  " +
                                data.getParticipants() + "人参与  |  " +
                                data.getTime();

                        eventStatus.setText(statusText);


                    } else {
                        eventStatus.setText("活动信息加载失败");
                    }
                }

                @Override
                public void onFailure(Call<ActivityItem> call, Throwable t) {
                    eventStatus.setText("活动信息加载失败，请检查网络连接");
                }
            });
        }





}
