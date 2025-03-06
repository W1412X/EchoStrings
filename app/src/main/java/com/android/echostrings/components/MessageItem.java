package com.android.echostrings.components;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.android.echostrings.R;

public class MessageItem extends LinearLayout {

    private TextView messageTextView;
    private LinearLayout linear;
    private CardView card;

    public MessageItem(Context context) {
        super(context);
        init(context);
    }

    public MessageItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MessageItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.component_message_item, this, true);

        // Find views by ID
        messageTextView = findViewById(R.id.component_message_text);
        linear=findViewById(R.id.component_message_linear);
        card=findViewById(R.id.message_item_card);
        // Inflate the layout

    }

    // Set the text content for the TextView
    public void setMessage(String text) {
        messageTextView.setText(text);
    }
    public void setGravity(String tmp){
        if(tmp.equals("left")){
            linear.setGravity(Gravity.LEFT);
            card.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.teach_page_base_button));
            messageTextView.setTextColor(Color.parseColor("#ffffff"));
        }else{
            linear.setGravity(Gravity.RIGHT);
        }
    }
}