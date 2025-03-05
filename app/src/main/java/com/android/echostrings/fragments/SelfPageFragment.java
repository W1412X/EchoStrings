package com.android.echostrings.fragments; // 替换为你的包名


import com.android.echostrings.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import de.hdodenhof.circleimageview.CircleImageView;

public class SelfPageFragment extends Fragment {

    private MaterialButton btnSettings, btnEditProfile, btnPosts, btnLikes, btnAttention;
    private CircleImageView ivAvatar;
    private MaterialCardView learningStatsCard;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SelfPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelfPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelfPageFragment newInstance(String param1, String param2) {
        SelfPageFragment fragment = new SelfPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_self_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化视图组件
        initViews(view);

        // 设置点击监听器
        setupClickListeners();

        // 示例：加载用户数据
        loadUserData();
    }

    private void initViews(View view) {
        // 工具栏设置按钮
        btnSettings = view.findViewById(R.id.btn_settings);

        // 个人信息相关组件
        ivAvatar = view.findViewById(R.id.iv_avatar);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);

        // 底部导航按钮
        btnPosts = view.findViewById(R.id.posts);
        btnLikes = view.findViewById(R.id.likes);
        btnAttention = view.findViewById(R.id.attention);

        // 学习数据卡片
        learningStatsCard = view.findViewById(R.id.learning_stats_card);
    }

    private void setupClickListeners() {
        // 设置按钮点击
        btnSettings.setOnClickListener(v -> showToast("打开设置"));

        // 编辑资料按钮
        btnEditProfile.setOnClickListener(v -> showToast("编辑个人资料"));

        // 底部导航按钮
        btnPosts.setOnClickListener(v -> switchTab("posts"));
        btnLikes.setOnClickListener(v -> switchTab("likes"));
        btnAttention.setOnClickListener(v -> switchTab("attention"));
    }

    private void loadUserData() {
//         这里添加实际的数据加载逻辑
//         示例数据：
//         ivAvatar.setImageResource(R.drawable.user_avatar);
//         tvUsername.setText("吉他小王子");
//         tvLevel.setText("Lv.12 指弹达人");
    }

    private void switchTab(String tab) {
        // 实现切换不同内容展示的逻辑
        switch (tab) {
            case "posts":
                showToast("显示我的投稿");
                break;
            case "likes":
                showToast("显示我的收藏");
                break;
            case "attention":
                showToast("显示关注列表");
                break;
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


}