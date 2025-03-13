package com.android.echostrings.activities;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.echostrings.R;
import com.android.echostrings.fragments.ActivityIntroFragment;
import com.android.echostrings.fragments.HotWorksFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class ActivityDetailActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private MaterialButton btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        btnSubmit = findViewById(R.id.btnSubmit);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ActivityIntroFragment());
        fragments.add(new HotWorksFragment());

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
}
