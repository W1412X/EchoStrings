package com.android.echostrings.activities;

import static okhttp3.MultipartBody.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.android.echostrings.R;
import com.android.echostrings.data.CompositionWebData;
import com.android.echostrings.network.ApiService;
import com.android.echostrings.network.data.MusicSheetDetailResponse;
import com.google.gson.Gson;
import com.google.mediapipe.components.PermissionHelper;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PracticeActivity extends AppCompatActivity {
    private int song_id;

    private int duration;
    private Button practice_btn,analyze_btn;
    private boolean state;//the composition load state
    private WebView analyze_web,composition_web;
    private MediaRecorder recorder;
    private RelativeLayout loading_view;
    private String audio_path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        song_id=intent.getIntExtra("song_id",0);
        setContentView(R.layout.activity_practice);
        initView();
       this.audio_path=getExternalFilesDir(Environment.DIRECTORY_MUSIC)+"/practice_audio_now.wav";
    }
    private void initWeb(WebView web){
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view,request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loading_view.setVisibility(View.VISIBLE);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("UUUUUUUUUUU",url);
                if (url.startsWith("blob:")) {
                    // 处理blob协议的下载
                    Log.e("UUUUUUUUUUU",url);
                    return true;
                } else {
                    // 处理其他协议的请求
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loading_view.setVisibility(View.GONE);
            }
        });

        //set client
        web.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage msg){
                // 可以在这里处理控制台消息
                return super.onConsoleMessage(msg);
            }

            @Override
            public void onPermissionRequest(PermissionRequest request){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
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
        web.loadDataWithBaseURL(null,CompositionWebData.getUpdatedHtml(CompositionWebData.getDefaultTex()),"text/html", "UTF-8",null);
    }
    private void initView(){
        practice_btn=findViewById(R.id.practise_btn);
        analyze_btn=findViewById(R.id.analyze_btn);
        analyze_btn.setVisibility(View.GONE);
        analyze_web=findViewById(R.id.data_analyze_page);
        loading_view=findViewById(R.id.global_loading_view_relative);
        composition_web=findViewById(R.id.practise_composition_web);
        loading_view.setVisibility(View.VISIBLE);
        initWeb(composition_web);
        initWeb(analyze_web);
        practice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionHelper.checkAndRequestAudioPermissions(PracticeActivity.this);
                if (!PermissionHelper.audioPermissionsGranted(PracticeActivity.this)) {
                    Toast.makeText(PracticeActivity.this,"未授予录音权限",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!state){
                    Toast.makeText(PracticeActivity.this,"正在加载曲谱....",Toast.LENGTH_SHORT).show();
                }else{
                    //start to record the audio
                    practice_btn.setEnabled(false);
                    startRecording();
                    new CountDownTimer((long)duration, 1000) {
                        public void onTick(long millisUntilFinished) {
                            int seconds = (int) (millisUntilFinished / 1000) % 60 ;
                            int minutes = (int) ((millisUntilFinished / (1000*60)) % 60);
                            practice_btn.setText(String.format("%02d:%02d", minutes, seconds));
                        }

                        public void onFinish() {
                            practice_btn.setText("练习完成");
                            practice_btn.setEnabled(true);
                            stopRecording();
                            analyze_btn.setVisibility(View.VISIBLE);
                        }
                    }.start();
                    //start the web play silent
                    composition_web.evaluateJavascript("api.changeTrackMute([api.score.tracks[0]], true);", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //do nothing here
                        }
                    });
                    composition_web.evaluateJavascript("api.play()", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //do nothing
                        }
                    });
                }
            }
        });
        //get the song by the id
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.server_url))
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<MusicSheetDetailResponse> call = apiService.getMusicSheetById(song_id);
        call.enqueue(new retrofit2.Callback<MusicSheetDetailResponse>() {
            @Override
            public void onResponse(Call<MusicSheetDetailResponse> call, retrofit2.Response<MusicSheetDetailResponse> response) {
                if (response.isSuccessful()) {
                    MusicSheetDetailResponse.Data musicSheetDetail = response.body().getData();
                    updateWeb(musicSheetDetail.getContent());
                    duration=(int)musicSheetDetail.getDuration()*1000;
                    state=true;
                    loading_view.setVisibility(View.GONE);
                } else {
                    Toast.makeText(PracticeActivity.this,"响应状态异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MusicSheetDetailResponse> call, Throwable t) {
                Toast.makeText(PracticeActivity.this,"获取数据失败",Toast.LENGTH_SHORT).show();
            }
        });
        analyze_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading_view.setVisibility(View.VISIBLE);
                File file = new File(audio_path);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                Part body = Part.createFormData("file", file.getName(), requestFile);
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
                        .writeTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
                        .readTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
                        .build();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(getResources().getString(R.string.server_url))
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiService apiService = retrofit.create(ApiService.class);
                Call<ResponseBody> call = apiService.uploadAudioFile(song_id, body);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()) {
                            analyze_web.setWebViewClient(new WebViewClient() {
                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                    return super.shouldOverrideUrlLoading(view,request);
                                }

                                @Override
                                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                    super.onPageStarted(view, url, favicon);
                                }
                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    return true;
                                }
                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    super.onPageFinished(view, url);
                                    String tmp="";
                                    try{
                                        tmp=response.body().string();
                                    }catch (IOException e){
                                        Log.e("nn","mmm");
                                    }
                                    Log.e("STRING",tmp);
                                    analyze_web.evaluateJavascript("document.getElementById(\"jsonInput\").value=`" + tmp + "`;" +
                                            "document.getElementById(\"convertButton\").click();", new ValueCallback<String>() {
                                        @Override
                                        public void onReceiveValue(String value) {
                                            Log.e("RESULT",value);
                                        }
                                    });
                                }
                            });
                            analyze_web.loadUrl("file:///android_asset/analyze.html");
                            analyze_web.setVisibility(View.VISIBLE);
                            loading_view.setVisibility(View.GONE);
                        } else {
                            loading_view.setVisibility(View.GONE);
                            Toast.makeText(PracticeActivity.this,"响应状态异常",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading_view.setVisibility(View.GONE);
                        Log.e("ERROR",t.toString());
                        Log.e("ERROR",call.toString());
                        Toast.makeText(PracticeActivity.this,"无法获取数据",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setAudioSamplingRate(44100);
        recorder.setOutputFile(this.audio_path);
        try {
            recorder.prepare();
            recorder.start();
            Toast.makeText(PracticeActivity.this, "开始录音...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }
    public void updateWeb(String tex){
        Log.d("TEX",tex);
        composition_web.loadDataWithBaseURL(null, CompositionWebData.getUpdatedHtml(tex), "text/html", "UTF-8",null);
    }
}