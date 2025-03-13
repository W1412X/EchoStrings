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
import com.android.echostrings.network.ApiService;
import com.android.echostrings.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityIntroFragment extends Fragment {
    private WebView webView;


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

        loadActivityIntroduction();
        return view;
    }

    private void loadActivityIntroduction() {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getActivityIntroduction().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    webView.loadDataWithBaseURL(null, response.body(), "text/html", "utf-8", null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
