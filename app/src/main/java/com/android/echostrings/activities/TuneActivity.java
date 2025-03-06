package com.android.echostrings.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.echostrings.R;
import com.google.mediapipe.components.PermissionHelper;

public class TuneActivity extends AppCompatActivity {
    private WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tune);
        web=findViewById(R.id.tune_activity_web);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (!PermissionHelper.audioPermissionsGranted(TuneActivity.this)) {
            Toast.makeText(TuneActivity.this,"未授予录音权限",Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        //set client
        web.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage msg){
                return super.onConsoleMessage(msg);
            }
            @Override
            public void onPermissionRequest(PermissionRequest request){
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                    request.grant(request.getResources());
                }
            }
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result){
                result.confirm();
                return true;
            }
        });
        //load local file
        web.loadUrl("file:///android_asset/tune.html");
    }
}