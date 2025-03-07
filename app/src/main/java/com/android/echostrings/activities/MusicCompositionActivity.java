package com.android.echostrings.activities;

import static androidx.camera.core.CameraX.getContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;

import com.android.echostrings.R;
import com.android.echostrings.components.CompositionEditor;
import com.android.echostrings.components.MessageItem;
import com.android.echostrings.data.CompositionWebData;
import com.android.echostrings.network.ApiService;
import com.android.echostrings.network.data.ChatRequest;
import com.android.echostrings.network.data.ChatResponse;
import com.android.echostrings.network.data.MusicCreateRequest;
import com.android.echostrings.network.data.MusicCreateResponse;
import com.google.mediapipe.components.PermissionHelper;

import alphaTab.AlphaTabApiBase;
import alphaTab.AlphaTabView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MusicCompositionActivity extends ComponentActivity {
    private WebView web;
    private EditText score_title,song_style;
    private ImageButton setting_btn,close_setting_btn;
    private Switch beat_switch,loop_switch;
    private Button ai_create_btn,to_pdf_btn;
    private ImageButton upload_btn;
    private String tex_now=CompositionWebData.getDefaultTex();
    private Spinner guitar_tone,guitar_tune,guitar_beat,guitar_time;
    private RelativeLayout edit_view_relative,setting_view,loading_view;
    private LinearLayout ai_create_linear;
    private CompositionEditor editor;
    private Boolean ai_create_show_state=true;
    /**
     * control the visibility of the create bar
     */
    private ImageButton ai_create_show_btn,edit_show_btn;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_composition);
        init();
        initWeb();
    }

    /**
     * set the views
     */
    private void init(){
        score_title=findViewById(R.id.title_input);
        song_style=findViewById(R.id.style_input);
        web=findViewById(R.id.composition_web);
        editor=findViewById(R.id.edit_view_object);
        edit_view_relative=findViewById(R.id.edit_view);
        ai_create_linear=findViewById(R.id.composition_ai_linear);
        ai_create_show_btn=findViewById(R.id.show_ai_create_linear);
        ai_create_btn=findViewById(R.id.ai_get_composition);
        loading_view=findViewById(R.id.global_loading_view_relative);
        guitar_tone=findViewById(R.id.tone_spinner);
        guitar_beat=findViewById(R.id.beat_spinner);
        guitar_tune=findViewById(R.id.tune_spinner);
        guitar_time=findViewById(R.id.time_spinner);
        upload_btn=findViewById(R.id.composition_upload_btn);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading_view.setVisibility(View.VISIBLE);
                /**
                 * upload the composition message
                 * to do
                 */
                loading_view.setVisibility(View.GONE);
            }
        });
        ai_create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(score_title.getText().toString().equals("")||song_style.getText().toString().equals("")){
                    Toast.makeText(MusicCompositionActivity.this,"请补全信息",Toast.LENGTH_SHORT).show();
                    return;
                }
                loading_view.setVisibility(View.VISIBLE);
                /**
                 * get the response from server
                 */
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(600, java.util.concurrent.TimeUnit.SECONDS)
                        .writeTimeout(600, java.util.concurrent.TimeUnit.SECONDS)
                        .readTimeout(600, java.util.concurrent.TimeUnit.SECONDS)
                        .build();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.102.33.19:8000")
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService apiService = retrofit.create(ApiService.class);
                Log.i("test",guitar_beat.getSelectedItem().toString());

                MusicCreateRequest musicCreateRequest = new MusicCreateRequest("1001".toString(),
                        score_title.getText().toString(),
                        CompositionWebData.getId(guitar_tone.getSelectedItem().toString()),
                        guitar_tune.getSelectedItem().toString(),
                        guitar_beat.getSelectedItem().toString(),
                        "测试用户".toString(),
                        guitar_time.getSelectedItem().toString(),
                        song_style.getText().toString());
                Call<MusicCreateResponse> call = apiService.createMusic(musicCreateRequest);
                call.enqueue(new Callback<MusicCreateResponse>() {
                    @Override
                    public void onResponse(Call<MusicCreateResponse> call, Response<MusicCreateResponse> response) {
                        if (response.isSuccessful()) {
                            MusicCreateResponse musicCreateResponse = response.body();
                            updateWeb(musicCreateResponse.getAnswer());
                        } else {
                            Log.e("network_error",call.toString());
                            Log.e("network_error",response.toString());
                            Toast.makeText(MusicCompositionActivity.this,"请求错误",Toast.LENGTH_SHORT).show();
                        }
                        loading_view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<MusicCreateResponse> call, Throwable t) {
                        Log.e("network_error",call.toString());
                        Log.e("network_error",t.toString());
                        Toast.makeText(MusicCompositionActivity.this,"请求错误",Toast.LENGTH_SHORT).show();
                        loading_view.setVisibility(View.GONE);
                    }
                });
            }
        });
        ai_create_show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ai_create_show_state){
                    ai_create_linear.setVisibility(View.GONE);
                    ai_create_show_state=false;
                    ai_create_show_btn.setImageResource(R.drawable.icon_down);
                }else{
                    ai_create_linear.setVisibility(View.VISIBLE);
                    ai_create_show_state=true;
                    ai_create_show_btn.setImageResource(R.drawable.icon_up);
                }
            }
        });
        /**
         * setting part
         */
        setting_view=findViewById(R.id.composition_setting_relative);
        setting_btn=findViewById(R.id.composition_settings_btn);
        close_setting_btn=findViewById(R.id.close_setting_btn);
        to_pdf_btn=findViewById(R.id.to_pdf_btn);
        beat_switch=findViewById(R.id.composition_beat_switch);
        loop_switch=findViewById(R.id.composition_loop_switch);
        to_pdf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createWebPagePdf(web);
            }
        });
        beat_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beatClick();
            }
        });
        loop_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loopClick();
            }
        });
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting_view.setVisibility(View.VISIBLE);
            }
        });
        close_setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting_view.setVisibility(View.GONE);
            }
        });
        /**
         * edit part
         */
        edit_view_relative=findViewById(R.id.edit_view);
        editor=findViewById(R.id.edit_view_object);
        edit_show_btn=findViewById(R.id.show_edit_btn);
        edit_show_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_view_relative.setVisibility(View.VISIBLE);
                editor.setOriTex(tex_now);
            }
        });
    }
    /**
     * init the web
     * do some special settings
     */
    private void initWeb(){
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
    /**
     * some function to set the web
     */
    private void beatClick(){
        web.evaluateJavascript("var tmp=document.getElementById(\"beat\")\n" +
                "tmp.click()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }
    private void loopClick(){
        web.evaluateJavascript("var tmp=document.getElementById(\"loop\")\n" +
                "tmp.click()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }
    private void createWebPagePdf(WebView webView) {
        /**
         * set invisible
         */
        web.evaluateJavascript("var tmp=document.getElementById(\"control-bar\")\n" +
                "tmp.style.display = \"none\"", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter("document-name");
            String jobName = getString(R.string.app_name) + " Document";
            PrintAttributes.Builder builder = new PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS);

            if (printManager != null) {
                printManager.print(jobName, printAdapter, builder.build());
            }
        } else {
            // 对于API级别低于21的处理方式
            Toast.makeText(this, "此功能需要Android 5.0及以上版本", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * update the web,call the loadUrl method
     */
    public void updateWeb(String tex){
        this.tex_now=tex;
        web.loadDataWithBaseURL(null,CompositionWebData.getUpdatedHtml(this.tex_now), "text/html", "UTF-8",null);
    }
    /**
     * which can be called by the Editor class
     */
    public void closeEditor(){
        edit_view_relative.setVisibility(View.GONE);
    }

    /**
     * do some optimize
     */
    public void onBackPressed() {
        if (edit_view_relative.getVisibility()==View.VISIBLE) {
            edit_view_relative.setVisibility(View.GONE);
        }else if(setting_view.getVisibility()==View.VISIBLE){
            setting_view.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
        }
    }

}
