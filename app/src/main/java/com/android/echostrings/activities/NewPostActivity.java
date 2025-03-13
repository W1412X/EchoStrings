package com.android.echostrings.activities;

import android.content.Intent;
import android.database.Cursor;
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
import com.android.echostrings.data.PostItem;
import com.android.echostrings.network.ApiService;
import com.android.echostrings.network.RetrofitClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPostActivity extends AppCompatActivity {

    private static final int PICK_MEDIA_REQUEST = 1;
    private Uri imageUri;
    private Uri videoUri;
    private EditText titleInput, textInput;
    private CheckBox privacyCheckbox;
    private ChipGroup chipGroup;
    private ApiService apiService;

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
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        findViewById(R.id.image_button).setOnClickListener(v -> openGallery());

        postButton.setOnClickListener(v -> publishPost());
        draftButton.setOnClickListener(v -> saveDraft());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("*/*"); // 允许选择图片或视频
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
        startActivityForResult(intent, PICK_MEDIA_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_MEDIA_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();
            String fileType = getContentResolver().getType(selectedFileUri);

            if (fileType != null) {
                if (fileType.startsWith("image/")) {
                    imageUri = selectedFileUri;  // 选择的是图片
                } else if (fileType.startsWith("video/")) {
                    videoUri = selectedFileUri;  // 选择的是视频
                }
            }
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

        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody contentBody = RequestBody.create(MediaType.parse("text/plain"), content);

        MultipartBody.Part file1 = null, file2 = null;

        if (imageUri != null) {
            File imageFile = getFileFromUri(imageUri);
            if (imageFile != null) {
                RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
                file1 = MultipartBody.Part.createFormData("file1", imageFile.getName(), imageRequestBody);
            }
        }

        if (videoUri != null) {
            File videoFile = getFileFromUri(videoUri);
            if (videoFile != null) {
                RequestBody videoRequestBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
                file2 = MultipartBody.Part.createFormData("file2", videoFile.getName(), videoRequestBody);
            }
        }

        apiService.createPost(titleBody, contentBody, file1, file2).enqueue(new Callback<PostItem>() {
            @Override
            public void onResponse(Call<PostItem> call, Response<PostItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PostItem postedItem = response.body();
                    Intent intent = new Intent();
                    intent.putExtra("newPost", postedItem);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PostItem> call, Throwable t) {
                Toast.makeText(NewPostActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
    private File getFileFromUri(Uri uri) {
        File file = null;
        try {
            String fileName = System.currentTimeMillis() + ".tmp"; // 生成唯一文件名
            file = new File(getCacheDir(), fileName);

            try (InputStream inputStream = getContentResolver().openInputStream(uri);
                 OutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }




}
