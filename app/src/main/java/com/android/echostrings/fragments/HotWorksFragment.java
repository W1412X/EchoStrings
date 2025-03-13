package com.android.echostrings.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.echostrings.R;
import com.android.echostrings.adapter.ActivityVideoAdapter;
import com.android.echostrings.data.Work;
import com.android.echostrings.network.ApiService;
import com.android.echostrings.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotWorksFragment extends Fragment {
    private RecyclerView recyclerView;
    private ActivityVideoAdapter workAdapter;

    public HotWorksFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot_works, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        workAdapter = new ActivityVideoAdapter(new ArrayList<>());
        recyclerView.setAdapter(workAdapter);

        loadHotWorks();
        return view;
    }

    private void loadHotWorks() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getHotWorks().enqueue(new Callback<List<Work>>() {
            @Override
            public void onResponse(Call<List<Work>> call, Response<List<Work>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    workAdapter.updateData(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Work>> call, Throwable t) {
                // 处理请求失败
            }
        });
    }
}

