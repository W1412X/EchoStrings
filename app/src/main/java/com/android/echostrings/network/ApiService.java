package com.android.echostrings.network;

import com.android.echostrings.network.data.ChatRequest;
import com.android.echostrings.network.data.ChatResponse;
import com.android.echostrings.network.data.MusicCreateResponse;
import com.android.echostrings.network.data.MusicCreateRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("/ai/chat")
    Call<ChatResponse> sendMessage(@Body ChatRequest chatRequest);

    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer <your_token_here>"
    })
    @POST("/ai/music_creat")
    Call<MusicCreateResponse> createMusic(@Body MusicCreateRequest musicCreatRequest);
}