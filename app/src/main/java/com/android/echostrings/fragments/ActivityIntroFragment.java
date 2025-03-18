package com.android.echostrings.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.echostrings.R;
import com.android.echostrings.data.ActivityItem;
import com.android.echostrings.network.ApiService;
import com.android.echostrings.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityIntroFragment extends Fragment {
    private WebView webView;
    private String activityId;


    public ActivityIntroFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_intro, container, false);
        webView = view.findViewById(R.id.webView);

        // WebView 配置
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        if (getArguments() != null) {
            activityId = getArguments().getString("activityId");
        }

        loadActivityIntroduction();
        return view;
    }

    private void loadActivityIntroduction() {
        if (activityId == null) {
            return;
        }
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getActivityIntroductionById(activityId).enqueue(new Callback<ActivityItem>() {
            @Override
            public void onResponse(Call<ActivityItem> call, Response<ActivityItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ActivityItem data = response.body();
                    webView.loadDataWithBaseURL(null, data.getActivityIntroduction(), "text/html", "UTF-8", null);
                }
            }

            @Override
            public void onFailure(Call<ActivityItem> call, Throwable t) {

            }
        });
    }
}
