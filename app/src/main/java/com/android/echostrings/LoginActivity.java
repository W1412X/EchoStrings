package com.android.echostrings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.echostrings.utils.PreferencesUtil;

public class LoginActivity extends AppCompatActivity {
    //control the method that to login
    private View login_username_view,login_email_view;
    private Button switch_username_btn,switch_email_btn;
    private LinearLayout username_linear,email_linear;
    //email login part
    private Button login_email_btn,login_get_code_btn;
    private EditText login_email_input,login_code_input;
    //username login part
    private Button login_username_btn;
    private EditText login_username_input,login_passwd_input;
    //mid part
    private Button to_register_page,to_login_page;
    //register part
    private View register_view;
    private Button register_get_code_btn,register_btn;
    private RadioButton agree_btn;
    private EditText register_username_input,register_passwd_input,register_passwd2_input,register_email_input,register_code_input;
    //some params
    private long lastBackPressedTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }
    //overwrite the back logic
    @Override
    public void onBackPressed() {
        if(this.register_view.getVisibility()==View.VISIBLE){//if register view is visible close the register view first
            this.register_view.setVisibility(View.GONE);
        }else{
            if (lastBackPressedTime + 2000 > SystemClock.elapsedRealtime()) {
                //if press return twice in 2 s,then exit
                finish();
                return;
            }
            //if first return
            lastBackPressedTime = SystemClock.elapsedRealtime();
            Toast.makeText(this, "再次点击返回键退出应用", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews(){
        //bind controls to view
        this.login_username_view=findViewById(R.id.start_login_page_username_view);
        this.login_email_view=findViewById(R.id.start_login_page_email_view);
        this.switch_email_btn=findViewById(R.id.start_login_page_choose_email_button);
        this.switch_username_btn=findViewById(R.id.start_login_page_choose_username_button);
        this.login_email_btn=findViewById(R.id.start_login_page_email_login_button);
        this.login_username_btn=findViewById(R.id.start_login_page_username_login_button);
        this.login_get_code_btn=findViewById(R.id.start_login_page_login_get_code_button);
        this.login_code_input=findViewById(R.id.start_login_page_login_code_input);
        this.login_username_input=findViewById(R.id.start_login_page_username_input);
        this.login_passwd_input=findViewById(R.id.start_login_page_login_passwd_input);
        this.login_email_input=findViewById(R.id.start_login_page_login_email_input);
        this.username_linear=findViewById(R.id.start_login_page_choose_username_linear);
        this.email_linear=findViewById(R.id.start_login_page_choose_email_linear);
        //set listener
        switch_email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_email_view.setVisibility(View.VISIBLE);
                login_username_view.setVisibility((View.GONE));
                username_linear.setBackgroundColor(getResources().getColor(R.color.mainColor));
                email_linear.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });
        switch_username_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_email_view.setVisibility(View.GONE);
                login_username_view.setVisibility((View.VISIBLE));
                email_linear.setBackgroundColor(getResources().getColor(R.color.mainColor));
                username_linear.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });
        this.register_view=findViewById(R.id.start_login_page_register_view);
        this.to_register_page=findViewById(R.id.start_login_page_to_register_page_button);
        this.to_login_page=findViewById(R.id.start_login_page_return_login_button);
        this.to_register_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_view.setVisibility(View.VISIBLE);
            }
        });
        this.to_login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_view.setVisibility(View.GONE);
            }
        });
        //bind the controls of register view
        this.agree_btn=findViewById(R.id.start_register_page_agree_button);
        this.register_btn=findViewById(R.id.start_register_page_register_button);
        this.register_get_code_btn=findViewById(R.id.start_login_page_register_get_code_button);
        this.register_username_input=findViewById(R.id.start_login_page_register_username_input);
        this.register_passwd_input=findViewById(R.id.start_register_page_passwd_input);
        this.register_passwd2_input=findViewById(R.id.start_login_page_register_passwd_twice_input);
        this.register_email_input=findViewById(R.id.start_login_page_register_email_input);
        this.register_code_input=findViewById(R.id.start_login_page_register_code_input);

        //
        this.login_username_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(login_username_input.getText().toString().equals("test")){
                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //set a flag and clear the login page
                    PreferencesUtil.putString(LoginActivity.this,"user_name",login_username_input.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}