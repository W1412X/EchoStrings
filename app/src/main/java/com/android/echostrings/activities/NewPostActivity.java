package com.android.echostrings.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.echostrings.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class NewPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private EditText titleInput, textInput;
    private CheckBox privacyCheckbox;
    private ChipGroup chipGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_post_page);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        MaterialButton btnSettings = findViewById(R.id.btn_settings);
        Button postButton = findViewById(R.id.post_button);
        Button draftButton = findViewById(R.id.draft_button);
        titleInput = findViewById(R.id.title_input);
        textInput = findViewById(R.id.text_input);
        privacyCheckbox = findViewById(R.id.public_checkbox);
        chipGroup = findViewById(R.id.location_chip_group);

        btnSettings.setOnClickListener(v -> finish());

        findViewById(R.id.image_button).setOnClickListener(v -> openGallery());

        postButton.setOnClickListener(v -> publishPost());
        draftButton.setOnClickListener(v -> saveDraft());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Toast.makeText(this, "图片已选择", Toast.LENGTH_SHORT).show();
        }
    }

    private void publishPost() {
        String title = titleInput.getText().toString();
        String content = textInput.getText().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "标题和内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!privacyCheckbox.isChecked()) {
            Toast.makeText(this, "请勾选隐私协议", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder tags = new StringBuilder();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                tags.append(chip.getText()).append(", ");
            }
        }

        Toast.makeText(this, "帖子已发布: " + title, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void saveDraft() {
        String title = titleInput.getText().toString();
        String content = textInput.getText().toString();
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
            Toast.makeText(this, "草稿不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "草稿已保存", Toast.LENGTH_SHORT).show();
    }
}
