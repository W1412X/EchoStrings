package com.android.echostrings.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.echostrings.R;
import com.android.echostrings.components.SongItem;
import com.android.echostrings.network.ApiService;
import com.android.echostrings.network.data.MusicSheetResponse;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecognizeSoundActivity extends AppCompatActivity {
    private LinearLayout finger_play_btn,sing_play_btn;
    private CardView song_list_card;
    private LinearLayout song_list_container;
    private RelativeLayout loading_view;
    public String type="";//finger/sing
    public int song_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize_sound);
        initView();
    }
    private void initView(){
        finger_play_btn=findViewById(R.id.finger_play_button);
        sing_play_btn=findViewById(R.id.sing_play_button);
        song_list_card=findViewById(R.id.song_container_card);
        song_list_container=findViewById(R.id.song_container_linear);
        loading_view=findViewById(R.id.global_loading_view_relative);
        finger_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                song_list_card.setVisibility(View.VISIBLE);
                song_list_container.removeAllViews();
                loading_view.setVisibility(View.VISIBLE);
                type="finger";
                //get the song list here
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
                Call<MusicSheetResponse> call = apiService.getMusicSheets();
                call.enqueue(new Callback<MusicSheetResponse>() {
                    @Override
                    public void onResponse(Call<MusicSheetResponse> call, Response<MusicSheetResponse> response) {
                        Log.e("SSSSSSS","sssssssssssssssssssssssssssssssssssss");
                        if (response.isSuccessful()) {
                            MusicSheetResponse musicSheets = response.body();
                            for(int i=0;i<musicSheets.getData().size();i++){
                                song_list_container.addView(new SongItem(RecognizeSoundActivity.this,musicSheets.getData().get(i).getTitle(),"测试用户",musicSheets.getData().get(i).getId()));
                            }
                            loading_view.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(RecognizeSoundActivity.this,"响应状态异常",Toast.LENGTH_SHORT).show();
                            loading_view.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<MusicSheetResponse> call, Throwable t) {
                        Toast.makeText(RecognizeSoundActivity.this,"获取数据失败",Toast.LENGTH_SHORT).show();
                        loading_view.setVisibility(View.GONE);
                    }
                });
            }
        });
        sing_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecognizeSoundActivity.this,"此功能暂未实现",Toast.LENGTH_SHORT).show();
                return;
                //type="sing";
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(song_list_card.getVisibility()==View.VISIBLE){
            song_list_card.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }
}