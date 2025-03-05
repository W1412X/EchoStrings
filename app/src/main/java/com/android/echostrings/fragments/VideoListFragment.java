package com.android.echostrings.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.echostrings.R;
import com.android.echostrings.adapter.VideoAdapter;
import com.android.echostrings.data.VideoItem;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



import okhttp3.OkHttpClient;
import okhttp3.Response;

import okhttp3.Request;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class VideoListFragment extends Fragment {

    private static final String ARG_CATEGORY = "category";
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<VideoItem> videoList = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();

    public static VideoListFragment newInstance(String category) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new VideoAdapter(videoList);
        recyclerView.setAdapter(adapter);

        String category = getArguments().getString(ARG_CATEGORY);
        fetchVideos(category);

        return view;
    }

    private void fetchVideos(String category) {
        String url = "https://api.example.com/videos?category=" + category;
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<VideoItem>>(){}.getType();
                    List<VideoItem> videos = gson.fromJson(json, listType);
                    getActivity().runOnUiThread(() -> {
                        videoList.clear();
                        videoList.addAll(videos);
                        adapter.notifyDataSetChanged();
                    });
                }
            }
        });
    }
}
