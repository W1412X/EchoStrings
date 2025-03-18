package com.android.echostrings.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.echostrings.R;
import com.android.echostrings.adapter.ActivityEventAdapter;
import com.android.echostrings.data.ActivityItem;
import com.android.echostrings.data.Work;
import com.android.echostrings.network.ApiService;
import com.android.echostrings.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ActivityEventAdapter adapter;
    private List<ActivityItem> eventList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);


        MaterialButton backButton = findViewById(R.id.btn_return);
        FloatingActionButton fab = findViewById(R.id.fab_create_event);
        recyclerView = findViewById(R.id.recycler_view);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ActivityEventAdapter(this,eventList);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> Toast.makeText(this, "创建活动功能开发中", Toast.LENGTH_SHORT).show());
        backButton.setOnClickListener(v -> finish());

        apiService = RetrofitClient.getInstance().create(ApiService.class);
        loadTestData();

//        fetchEventsFromServer();
    }

    private void fetchEventsFromServer() {
        apiService.getEvents().enqueue(new Callback<List<ActivityItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<ActivityItem>> call, @NonNull Response<List<ActivityItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventList.clear();
                    eventList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ActivityListActivity.this, "获取数据失败: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ActivityItem>> call, @NonNull Throwable t) {
                Toast.makeText(ActivityListActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadTestData() {


        List<Work> hotWorks1 = Arrays.asList(
                new Work("星空", "4:23", "gs", "https://example.com/work1")

        );
        eventList.add(new ActivityItem("1","活动 1", "2035.2.3-2025.6.3","进行中","",100,"",hotWorks1));


        // 通知适配器数据已经更新
        adapter.notifyDataSetChanged();
    }
}
