package com.android.echostrings.components;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.android.echostrings.R;
import com.android.echostrings.activities.MusicCompositionActivity;
import com.android.echostrings.network.ApiService;
import com.android.echostrings.network.data.ChatRequest;
import com.android.echostrings.network.data.ChatResponse;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CompositionEditor  extends LinearLayout {
    private Context context;
    private String tex;
    private ImageButton send_btn,show_chat_btn,close_btn;
    private Button confirm_btn;
    private EditText message_input,tex_input;
    private LinearLayout message_container;
    private RelativeLayout progress;
    private CardView chat_view;
    private Boolean if_first_chat=true;

    public CompositionEditor(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CompositionEditor(@NonNull Context context, Map<String, Object> data) {
        super(context);
        init(context);
    }
    public CompositionEditor(@NonNull Context context) {
        super(context);
        init(context);
    }
    public void init(Context context){
        this.context=context;
        LayoutInflater.from(context).inflate(R.layout.component_conposition_editor, this, true);
        send_btn=findViewById(R.id.chat_send);
        show_chat_btn=findViewById(R.id.ai_chat_btn);
        chat_view=findViewById(R.id.chat_card);
        chat_view.setVisibility(View.GONE);
        confirm_btn=findViewById(R.id.confirm_edit_btn);
        message_input=findViewById(R.id.chat_input);
        tex_input=findViewById(R.id.tex_input);
        message_container=findViewById(R.id.chat_message_container);
        progress=findViewById(R.id.chat_load_progress_relative);
        close_btn=findViewById(R.id.close_edit_btn);
        confirm_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * call external method to load the new score
                 */
                ((MusicCompositionActivity)context).updateWeb(tex_input.getText().toString());
                close_btn.callOnClick();
            }
        });
        show_chat_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chat_view.getVisibility()==View.VISIBLE){
                    chat_view.setVisibility(View.GONE);
                }else {
                    chat_view.setVisibility(View.VISIBLE);
                    if(if_first_chat){
                        addMessage("您好，我是您的乐谱创作助手，你可以问我一些关于编辑乐谱的问题! (▰˘◡˘▰)".toString(),"left");
                        if_first_chat=false;
                    }
                }
            }
        });
        send_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * check the input
                 */
                if(message_input.getText().toString().equals("")){
                    Toast.makeText(context,"请输入内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                progress.setVisibility(View.VISIBLE);
                String message=message_input.getText().toString();
                addMessage(message_input.getText().toString(),"right");
                message_input.setText("");
                /**
                 * get the response from server
                 */
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
                ChatRequest chatRequest = new ChatRequest("1", message);
                Log.i("message",message);
                Call<ChatResponse> call = apiService.sendMessage(chatRequest);
                call.enqueue(new Callback<ChatResponse>() {
                    @Override
                    public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                        if (response.isSuccessful()) {
                            ChatResponse chatResponse = response.body();
                            addMessage(chatResponse.getAnswer().toString(),"left");
                        } else {
                            Log.e("network_error",call.toString());
                            Log.e("network_error",response.toString());
                            Toast.makeText(getContext(),"请求错误",Toast.LENGTH_SHORT).show();
                        }
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<ChatResponse> call, Throwable t) {
                        Log.e("network_error",call.toString());
                        Log.e("network_error",t.toString());
                        Toast.makeText(getContext(),"请求错误",Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                    }
                });
            }
        });
        /**
         * call the external function in the MusicCompositionActivity
         */
        close_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MusicCompositionActivity)context).closeEditor();
            }
        });
        //comfirm_btn
    };
    public void setOriTex(String alpha_tex){
        this.tex=alpha_tex;
        this.tex_input.setText(this.tex);
    }
    public void addMessage(String text,String gravity){
        MessageItem msg = new MessageItem(this.context.getApplicationContext());
        msg.setMessage(text);
        msg.setGravity(gravity);
        this.message_container.addView(msg);
    }
}
