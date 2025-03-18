package com.android.echostrings.network;

import com.android.echostrings.data.ActivityItem;
import com.android.echostrings.data.PostItem;
import com.android.echostrings.network.data.ChatRequest;
import com.android.echostrings.network.data.ChatResponse;
import com.android.echostrings.network.data.ChordRecognitionResponse;
import com.android.echostrings.network.data.CompositionRequest;
import com.android.echostrings.network.data.CompositionResponse;
import com.android.echostrings.network.data.MusicCreateResponse;
import com.android.echostrings.network.data.MusicCreateRequest;
import com.android.echostrings.network.data.MusicSheetDetailResponse;
import com.android.echostrings.network.data.MusicSheetResponse;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @Multipart
    @POST("/audio/recognize_chord")
    Call<ChordRecognitionResponse> recognizeChord(
            @Part MultipartBody.Part file);
    @Multipart
    @POST("/audio/match")
    Call<ResponseBody> uploadAudioFile(
            @Part("audio_id") int audioId,
            @Part MultipartBody.Part file);
    @GET("/audio/sheet/{id}")
    Call<MusicSheetDetailResponse> getMusicSheetById(@Path("id") int id);
    @GET("/audio/sheet/list")
    Call<MusicSheetResponse> getMusicSheets();
    @Headers("Content-Type: application/json")
    @POST("/ai/chat")
    Call<ChatResponse> sendMessage(@Body ChatRequest chatRequest);
    @Headers("Content-Type: application/json")
    @POST("/audio/sheet/upload")
    Call<CompositionResponse> uploadComposition(@Body CompositionRequest musicCreateRequest);
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