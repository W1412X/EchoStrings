package com.android.echostrings.activities;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.echostrings.R;
import com.android.echostrings.data.ActivityItem;
import com.android.echostrings.fragments.ActivityIntroFragment;
import com.android.echostrings.fragments.HotWorksFragment;
import com.android.echostrings.network.ApiService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
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
    private TextView eventStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        eventStatus = findViewById(R.id.eventStatus);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        btnSubmit = findViewById(R.id.btnSubmit);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ActivityIntroFragment());
        fragments.add(new HotWorksFragment());
        loadEventStatus();

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
    }
    private void loadEventStatus() {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<ActivityItem> call = apiService.getEventsStatus();

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
