package com.android.echostrings.network;

import com.android.echostrings.data.ActivityItem;
import com.android.echostrings.data.PostItem;
import com.android.echostrings.data.Work;
import com.android.echostrings.network.data.ChatRequest;
import com.android.echostrings.network.data.ChatResponse;
import com.android.echostrings.network.data.MusicCreateResponse;
import com.android.echostrings.network.data.MusicCreateRequest;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("/ai/chat")
    Call<ChatResponse> sendMessage(@Body ChatRequest chatRequest);

    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer <your_token_here>"
    })
    @POST("/ai/music_creat")
    Call<MusicCreateResponse> createMusic(@Body MusicCreateRequest musicCreateRequest);
    @GET("/post/list")
    Call<List<PostItem>> getPosts();
    @GET("/activity/list")
    Call<List<ActivityItem>> getEvents();

    @Multipart
    @POST("/post/create")
    Call<PostItem> createPost(
            @Part("title") RequestBody title,
            @Part("content") RequestBody content,
            @Part MultipartBody.Part file1,
            @Part MultipartBody.Part file2
    );
    @GET("activities/{id}/introduction")
    Call<ActivityItem> getActivityIntroductionById(@Path("id") String activityId);

    @GET("activities/{id}/hotworks")
    Call<ActivityItem> getHotWorksByActivityId(@Path("id") String activityId);
    @GET("activities/{id}")
    Call<ActivityItem> getEventDetails(@Path("id") String activityId);

}