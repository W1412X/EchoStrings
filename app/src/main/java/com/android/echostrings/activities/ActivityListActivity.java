package com.android.echostrings.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.echostrings.R;
import com.android.echostrings.adapter.ActivityEventAdapter;
import com.android.echostrings.data.ActivityItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ActivityEventAdapter adapter;
    private List<ActivityItem> eventList = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        MaterialButton backButton = findViewById(R.id.btn_return);

        recyclerView = findViewById(R.id.recycler_view);
        FloatingActionButton fab = findViewById(R.id.fab_create_event);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ActivityEventAdapter(eventList);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> Toast.makeText(ActivityListActivity.this, "创建活动功能开发中", Toast.LENGTH_SHORT).show());
        backButton.setOnClickListener(v -> finish());

        fetchEventsFromServer();
    }

    private void fetchEventsFromServer() {
        Request request = new Request.Builder()
                .url("https://api.example.com/events")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(ActivityListActivity.this, "获取活动失败", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonData = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(jsonData);
                        eventList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String title = jsonObject.getString("title");

                            String coverUrl = jsonObject.getString("coverUrl");
                            String time = jsonObject.getString("time");
                            String status = jsonObject.getString("status");

                            eventList.add(new ActivityItem(title,time,status,coverUrl));
                        }
                        runOnUiThread(() -> adapter.notifyDataSetChanged());
                    } catch (Exception e) {
                        runOnUiThread(() -> Toast.makeText(ActivityListActivity.this, "数据解析错误", Toast.LENGTH_SHORT).show());
                    }
                }
            }
        });
    }
}